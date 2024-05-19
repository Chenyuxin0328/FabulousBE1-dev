package com.creatorsn.fabulous.service.impl;

import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.entity.*;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.mapper.*;
import com.creatorsn.fabulous.service.NoteBookService;
import com.creatorsn.fabulous.util.RandomUtil;
import com.creatorsn.fabulous.util.configuration.MinioConfiguration;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 笔记服务实现
 */
@Service
public class NoteBookServiceImpl implements NoteBookService {

    private final NoteBookMapper notebookMapper;
    private final NotebookGroupMapper dataGroupMapper;

    private final ConfigMapper configMapper;

    private final RedissonClient redissonClient;

    private final Logger logger = LoggerFactory.getLogger(NoteBookServiceImpl.class);
    private final ModelMapper modelMapper;

    private final DataStructMapper dataStructMapper;

    private final ConcurrentHashMap<String, Vector<SseEmitter>> subscribers = new ConcurrentHashMap<>();

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final UserMapper userMapper;

    private MinioClient minioClient;
    @Autowired
    private MinioConfiguration configuration;

    private MinioClient getClientInstance() {
        if (this.minioClient == null) {
            this.minioClient = MinioClient.builder()
                    .endpoint(configuration.getHost(), configuration.getPort(), false)
                    .credentials(configuration.getAccessKey(), configuration.getSecretKey())
                    .build();
        }
        return this.minioClient;
    }

    public NoteBookServiceImpl(
            UserMapper userMapper,
            NotebookGroupMapper dataGroupMapper,
            NoteBookMapper notebookMapper,
            ConfigMapper configMapper,
            RedissonClient redissonClient,
            ModelMapper modelMapper,
            DataStructMapper dataStructMapper,
            ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.dataGroupMapper = dataGroupMapper;
        this.notebookMapper = notebookMapper;
        this.configMapper = configMapper;
        this.redissonClient = redissonClient;
        this.modelMapper = modelMapper;
        this.dataStructMapper = dataStructMapper;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.userMapper = userMapper;
    }

    /**
     * 获取路径
     *
     * @param groupId 分组的Id
     * @return 返回分组的路径
     */
    private String getGroupParentPath(String groupId) {
        var paths = new ArrayList<String>();
        var group = dataGroupMapper.getById(groupId);
        String parent;
        while (group != null) {
            paths.add(group.getId());
            parent = group.getParent();
            if (parent == null)
                break;
            group = dataGroupMapper.getById(parent);
        }
        Collections.reverse(paths);
        return String.join("/", paths);
    }

    /**
     * 发送Chokidar 事件
     *
     * @param sourceId 源Id
     * @param event    事件
     */
    protected void sendChokidarEvent(String sourceId, ChokidarEvent event) {
        if (!subscribers.containsKey(sourceId))
            return;
        var removes = new ArrayList<SseEmitter>();
        var container = subscribers.get(sourceId);
        for (var emitter : container) {
            try {
                logger.info(MessageFormat.format("发送事件{1} 路径 {2} 给源 {0}", sourceId, event.getEvent(), event.getPath()));
                var builder = SseEmitter.event().data(event, MediaType.APPLICATION_JSON);
                builder.id(RandomUtil.DateTimeUUID());
                emitter.send(builder);
            } catch (IOException e) {
                emitter.completeWithError(e);
                removes.add(emitter);
            }
        }
        for (var remove : removes) {
            container.remove(remove);
        }
    }

    /**
     * @param group 要创建的分组
     * @return 返回创建后的分组
     */
    @Override
    public DataGroup createGroup(DataGroup group) throws UserException, ExecutionException, InterruptedException {
        if (group.getParent() == null)
            throw new UserException(StatusCode.PermissionDeny);
        // 判断
        boolean check = false;
        var source = dataStructMapper.getById(group.getParent());
        if (source != null) {
            if (!group.getOwner().equals(source.getUserId())) {
                throw new UserException(StatusCode.PermissionDeny);
            }
            if (!Objects.equals(group.getSourceId(), source.getId()))
                throw new UserException(StatusCode.PermissionDeny);
            check = true;
        }
        if (!check) {
            checkGroupPermission(group.getParent(), group.getOwner());
        }
        group.setId(RandomUtil.DateTimeUUID());
        group = dataGroupMapper.create(group);
        if (group != null) {
            // 触发创建操作
            var finalGroup = group;
            var path = getGroupParentPath(finalGroup.getId());
            var sourcePath = finalGroup.getSourceId() +
                    "/" +
                    path;
            var event = new ChokidarEvent().setEvent(ChangeType.addDir).setPath(
                    sourcePath).setDir(true).setName(group.getName()).setRelativePath(finalGroup.getId());
            threadPoolTaskExecutor.submit(() -> {
                try {
                    sendChokidarEvent(finalGroup.getSourceId(), event);
                } catch (Exception ignored) {
                }
            });
        }
        return group;
    }

    /**
     * 检查权限是否满足
     *
     * @param groupId 分组的Id
     * @param owner   分组的拥有者
     * @throws UserException 用户异常
     */
    private void checkGroupPermission(String groupId, String owner) throws UserException {
        var group = dataGroupMapper.getById(groupId);
        if (group == null) {
            throw new UserException(StatusCode.GroupNotExists);
        }
        if (!group.getOwner().equals(owner)) {
            throw new UserException(StatusCode.PermissionDeny);
        }
    }

    /**
     * @param id 分组的Id
     * @return 如果成功则返回true，否则返回false
     */
    @Override
    public boolean deleteGroup(String owner, String id) throws UserException {
        checkGroupPermission(id, owner);
        var finalGroup = dataGroupMapper.getById(id);
        if (finalGroup == null || !dataGroupMapper.delete(id))
            return false;
        var path = getGroupParentPath(finalGroup.getParent());
        var sourcePath = finalGroup.getSourceId() + "/" + path + "/" + id;
        if (!StringUtils.hasText(path)) {
            sourcePath = finalGroup.getSourceId() + "/" + id;
        }
        String finalSourcePath = sourcePath;
        threadPoolTaskExecutor.submit(new Thread(() -> {
            try {
                sendChokidarEvent(finalGroup.getSourceId(),
                        new ChokidarEvent().setEvent(ChangeType.unlinkDir)
                                .setPath(finalSourcePath).setDir(true).setRelativePath(id)
                                .setName(finalGroup.getName()));
            } catch (Exception ignored) {
            }
        }));
        return true;
    }

    /**
     * @param group 分组
     * @return 如果成功则返回更新后的分组，否则返回false
     */
    @Override
    public DataGroup updateGroup(String owner, DataGroup group) throws UserException {
        checkGroupPermission(group.getId(), owner);
        // 检查是否和sourceId一致
        if (group.getParent() == null)
            throw new UserException(StatusCode.PermissionDeny);
        var parent = group.getParent();
        var source = dataStructMapper.getById(parent);
        if (source == null)
            checkGroupPermission(group.getParent(), owner);
        else if (!Objects.equals(source.getUserId(), owner)) {
            throw new UserException(StatusCode.PermissionDeny);
        }
        var old = dataGroupMapper.getById(group.getId());
        if (old == null)
            throw new UserException(StatusCode.GroupNotExists);
        var sourceId = old.getSourceId();
        var events = new ArrayList<ChokidarEvent>();
        if (!Objects.equals(old.getParent(), group.getParent())) {
            // 触发unlinkDir
            var path = getGroupParentPath(old.getParent());
            var oldPath = StringUtils.hasText(path) ? sourceId + "/" + path + "/" + old.getId()
                    : sourceId + "/" + old.getId();
            events.add(
                    new ChokidarEvent().setEvent(ChangeType.unlinkDir).setPath(oldPath)
                            .setDir(true).setRelativePath(old.getId()).setName(old.getName()));
            // 触发addDir
            path = getGroupParentPath(group.getParent());
            path = StringUtils.hasText(path) ? sourceId + "/" + path + "/" + group.getId()
                    : sourceId + "/" + group.getId();
            events.add(new ChokidarEvent().setEvent(ChangeType.addDir).setPath(path)
                    .setName(old.getName()).setDir(true).setRelativePath(group.getId()));

        }
        group = dataGroupMapper.update(group);
        // 判断是否有成功修改目录，如果成功修改则正常触发，否则不触发
        if (group != null) {
            for (var event : events) {
                threadPoolTaskExecutor.submit(new Thread(() -> {
                    try {
                        sendChokidarEvent(sourceId, event);
                    } catch (Exception ignored) {
                    }
                }));
            }
        }
        return group;
    }

    /**
     * @param id 分组的Id
     * @return 返回获取的分组信息
     * @throws UserException 用户异常
     */
    @Override
    public DataGroup getGroup(String id) throws UserException {
        return dataGroupMapper.getById(id);
    }

    /**
     * @param id 分组的Id
     * @return 返回分组的所有子节点
     */
    @Override
    public List<DataGroup> getGroupChildren(String id, String owner) throws UserException {
        // 检查是否有查阅权限
        return dataGroupMapper.listByParent(id);
    }

    /**
     * @param noteBook 笔记
     * @return 返回创建后的笔记本
     */
    @Override
    public Notebook createNoteBook(Notebook noteBook) throws UserException {
        noteBookPermissionCheck(noteBook);
        noteBook.setId(RandomUtil.DateTimeUUID());
        var finalNoteBook = notebookMapper.create(noteBook);
        if (finalNoteBook != null) {
            var sourceId = noteBook.getSourceId();
            var path = getGroupParentPath(noteBook.getParent());
            var sourcePath = sourceId + "/" + path + "/" + noteBook.getId();
            if (!StringUtils.hasText(path)) {
                sourcePath = sourceId + "/" + noteBook.getId();
            }
            String finalSourcePath = sourcePath;
            // 如果包含内容，则创建内容
            if (noteBook.getContent() != null) {
                noteBook.getContent().setNotebookId(finalNoteBook.getId());
                finalNoteBook.setContent(noteBook.getContent());
                updateNotebookContent(finalNoteBook);
            }
            threadPoolTaskExecutor.submit(new Thread(() -> {
                sendChokidarEvent(sourceId, new ChokidarEvent().setEvent(ChangeType.add).setPath(finalSourcePath)
                        .setFile(true).setRelativePath(noteBook.getId()).setName(noteBook.getName()));
            }));
        }
        return finalNoteBook;
    }

    /**
     * 列出所有的笔记本和分组
     *
     * @param id    分组的Id
     * @param owner 用户的Id
     * @return 返回笔记本信息
     * @throws UserException 用户异常
     */
    @Override
    public List<DataGroupItem> listChildren(String id, String owner) throws UserException {
        var groups = getGroupChildren(id, owner);
        var notebooks = getGroupNoteBooks(id, owner, false);
        var items = new ArrayList<DataGroupItem>();
        for (var group : groups) {
            items.add(modelMapper.map(group, DataGroupItem.class).setType(DataGroupItem.DataGroupItemType.group));
        }
        for (var notebook : notebooks) {
            items.add(modelMapper.map(notebook, DataGroupItem.class).setType(DataGroupItem.DataGroupItemType.notebook));
        }
        return items;
    }

    /**
     * @param noteBook 笔记
     * @return 更新后的笔记本
     */
    @Override
    public Notebook updateNoteBook(Notebook noteBook) throws UserException {
        var old = notebookMapper.getById(noteBook.getId());
        if (old == null)
            throw new UserException(StatusCode.NotebookNotExists);
        if (!Objects.equals(old.getOwner(), noteBook.getOwner()))
            throw new UserException(StatusCode.PermissionDeny);
        var events = new ArrayList<ChokidarEvent>();
        var sourceId = old.getSourceId();
        // 更新了路径或者更新了名字
        if ((noteBook.getParent() != null && !Objects.equals(old.getParent(), noteBook.getParent()))
                || (noteBook.getName() != null && !noteBook.getName().equals(old.getName()))) {
            var path = getGroupParentPath(old.getParent());
            path = StringUtils.hasText(path) ? sourceId + "/" + path + "/" + old.getId() : sourceId + "/" + old.getId();
            events.add(new ChokidarEvent().setEvent(ChangeType.unlink).setPath(path).setRelativePath(old.getId())
                    .setFile(true).setName(old.getName()));
            path = getGroupParentPath(noteBook.getParent());
            path = StringUtils.hasText(path) ? sourceId + "/" + path + "/" + noteBook.getId()
                    : sourceId + "/" + noteBook.getId();
            events.add(new ChokidarEvent().setEvent(ChangeType.add).setPath(path).setRelativePath(noteBook.getId())
                    .setFile(true)
                    .setName(noteBook.getName() == null ? old.getName() : noteBook.getName()));
        }
        noteBook = notebookMapper.update(noteBook);
        if (noteBook != null) {
            for (var event : events) {
                threadPoolTaskExecutor.submit(new Thread(() -> {
                    try {
                        sendChokidarEvent(sourceId, event);
                    } catch (Exception ignored) {
                    }
                }));
            }
        }
        return noteBook;
    }

    /**
     * 检查笔记本的权限是否正确
     *
     * @param noteBook 笔记本
     * @throws UserException 用户异常
     */
    private void noteBookPermissionCheck(Notebook noteBook) throws UserException {
        if (noteBook.getParent() == null)
            throw new UserException(StatusCode.GroupNotExists);
        var group = dataGroupMapper.getById(noteBook.getParent());
        if (group == null) {
            var source = dataStructMapper.getById(noteBook.getParent());
            if (source == null)
                throw new UserException(StatusCode.PermissionDeny);
        } else {
            if (!Objects.equals(group.getOwner(), noteBook.getOwner())) {
                throw new UserException(StatusCode.PermissionDeny);
            }
            if (!Objects.equals(group.getSourceId(), noteBook.getSourceId())) {
                throw new UserException(StatusCode.PermissionDeny);
            }
        }
    }

    /**
     * @param groupId     分组的Id
     * @param withContent 是否包含内容
     * @return 获取笔记本列表
     */
    @Override
    public List<Notebook> getGroupNoteBooks(String groupId, String owner, boolean withContent) throws UserException {
        // checkGroupPermission(groupId, owner);
        var notebooks = notebookMapper.listByParent(groupId);
        if (withContent) {
            for (var notebook : notebooks) {
                notebook.setContent(notebookMapper.getContent(notebook.getId()));
            }
        }
        return notebooks;
    }

    /**
     * @param id          Notebook的Id
     * @param withContent 是否包含内容
     * @return 返回指定Notebook
     */
    @Override
    public Notebook getNoteBook(String id, String owner, boolean withContent) throws UserException {
        // noteBookPermissionCheck(new NoteBook().setId(id).setOwner(owner));
        var notebooks = notebookMapper.list(Collections.singletonList(id));
        var notebook = notebooks.get(0);
        if (withContent) {
            notebook.setContent(notebookMapper.getContent(notebook.getId()));
        }
        return notebook;
    }

    /**
     * @param noteBook 更新笔记本
     * @return 返回更新后的笔记本
     */
    @Override
    public Notebook updateNotebookContent(Notebook noteBook) throws UserException {
        var lock = redissonClient.getLock(MessageFormat.format("notebook-content-{0}", noteBook.getId()));
        // 尝试锁住更新
        lock.lock(10, TimeUnit.MINUTES);
        try {
            // 获取数据库的最新版本
            var latestNotebookContent = notebookMapper.getContent(noteBook.getId());
            // 比对版本是不是一致
            if (latestNotebookContent != null
                    && !Objects.equals(latestNotebookContent.getVersionId(), noteBook.getContent().getVersionId())) {
                noteBook.setContent(latestNotebookContent);
                throw new UserException(StatusCode.NotUpToDate, noteBook);
            }
            // 更新版本
            noteBook.getContent().setVersionId(RandomUtil.DateTimeUUID());
            var updatedContent = notebookMapper.updateContent(noteBook.getContent());
            if (updatedContent == null) {
                return null;
            }
            noteBook.setContent(updatedContent);
            threadPoolTaskExecutor.submit(new Thread(() -> {
                try {
                    var finalNoteBooks = notebookMapper.list(Collections.singletonList(noteBook.getId()));
                    if (finalNoteBooks.isEmpty())
                        return;
                    var finalNoteBook = finalNoteBooks.get(0);
                    var path = getGroupParentPath(finalNoteBook.getParent());
                    var sourcePath = finalNoteBook.getSourceId() + "/" + path + "/" + finalNoteBook.getId();
                    if (!StringUtils.hasText(path)) {
                        sourcePath = finalNoteBook.getSourceId() + "/" + finalNoteBook.getId();
                    }
                    sendChokidarEvent(finalNoteBook.getSourceId(),
                            new ChokidarEvent().setEvent(ChangeType.change).setPath(sourcePath)
                                    .setName(finalNoteBook.getName()).setRelativePath(finalNoteBook.getId())
                                    .setFile(true));
                } catch (Exception ignored) {
                }
            }));
            return noteBook;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param id            笔记的唯一编码
     * @param pageSize      每页的数量
     * @param lastVersionId 最后的版本
     * @return 返回笔记的历史版本内容
     */
    @Override
    public List<NoteBookContent> listNotebookHistoryVersion(String id, int pageSize, String lastVersionId) {
        var versions = notebookMapper.listContentVersions(id, pageSize, lastVersionId);
        var userIds = versions.stream().map(e -> e.getAuthor()).toList();
        var users = userMapper.list(userIds);
        var userMaps = new HashMap<String, User>();
        for (var user : users) {
            userMaps.put(user.getId(), user);
        }
        for (var version : versions) {
            if (userMaps.containsKey(version.getAuthor()))
                version.setEmail(userMaps.get(version.getAuthor()).getEmail());
        }
        return versions;
    }

    /**
     * @param id 笔记本的id
     * @return 符合符合条件的所有版本号
     */
    @Override
    public List<NoteBookContent> listNotebookHistoryVersionIds(String id) {
        var versions = notebookMapper.listContentVersionWithoutContent(id);
        var userIds = versions.stream().map(e -> e.getAuthor()).toList();
        var users = userMapper.list(userIds);
        var userMaps = new HashMap<String, User>();
        for (var user : users) {
            userMaps.put(user.getId(), user);
        }
        for (var version : versions) {
            if (userMaps.containsKey(version.getAuthor()))
                version.setEmail(userMaps.get(version.getAuthor()).getEmail());
        }
        return versions;
    }

    /**
     * @param notebookId 笔记本的Id
     * @param ids        版本号Id列表
     * @return 返回符合条件的列表
     */
    @Override
    public List<NoteBookContent> listNotebookHistoryVersionByIds(String notebookId, List<String> ids) {
        if (ids == null || ids.isEmpty())
            return new ArrayList<>();
        return notebookMapper.listContentVersionByIds(notebookId, ids);
    }

    /**
     * @param id    笔记本的Id
     * @param owner 拥有者
     * @return 如果成功删除则返回true，否则返回false
     * @throws UserException 用户异常
     */
    @Override
    public boolean deleteNotebook(String id, String owner) throws UserException {
        var notebooks = notebookMapper.list(Collections.singletonList(id));
        if (notebooks.isEmpty())
            return false;
        var finalNotebook = notebooks.get(0);
        noteBookPermissionCheck(finalNotebook.setOwner(owner));
        if (!notebookMapper.delete(id))
            return false;
        var path = getGroupParentPath(finalNotebook.getParent());
        var sourcePath = finalNotebook.getSourceId() + "/" + path + "/" + finalNotebook.getId();
        if (!StringUtils.hasText(path)) {
            sourcePath = finalNotebook.getSourceId() + "/" + finalNotebook.getId();
        }
        String finalSourcePath = sourcePath;
        threadPoolTaskExecutor.submit(new Thread(() -> {
            try {
                sendChokidarEvent(finalNotebook.getSourceId(),
                        new ChokidarEvent().setEvent(ChangeType.unlink).setPath(finalSourcePath)
                                .setFile(true).setRelativePath(finalNotebook.getId()).setName(finalNotebook.getName()));
            } catch (Exception ignored) {
            }
        }));
        return true;
    }

    /**
     * 订阅笔记本变动
     *
     * @param userId   用户的Id
     * @param sourceId 源Id
     * @param emitter  触发器
     */
    public void subscribeDataSource(String userId, String sourceId, SseEmitter emitter) throws UserException {
        // 检查权限
        // 添加到订阅中
        logger.info(MessageFormat.format("用户 {0} 开始监听 {1} 数据源", userId, sourceId));
        if (!subscribers.containsKey(sourceId)) {
            subscribers.put(sourceId, new Vector<>());
        }
        var vector = subscribers.get(sourceId);
        vector.add(emitter);
        emitter.onTimeout(() -> {
            try {
                vector.remove(emitter);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
        emitter.onCompletion(() -> {
            logger.info(MessageFormat.format("用户 {0} 监听 {1} 数据源结束", userId, sourceId));
            try {
                vector.remove(emitter);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
        emitter.onError((exception) -> {
            logger.error(exception.getMessage());
            try {
                vector.remove(emitter);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
        threadPoolTaskExecutor.submit(new Thread(() -> {
            try {
                while (true) {
                    var builder = SseEmitter.event().id(RandomUtil.DateTimeUUID());
                    builder.data(new ChokidarEvent().setEvent(ChangeType.healthCheck));
                    emitter.send(builder);
                    TimeUnit.SECONDS.sleep(10);
                }
            } catch (Exception e) {
                vector.remove(emitter);
                emitter.completeWithError(e);
            }
        }));
    }

    /**
     * @param path 路径，以/分割的Id编码
     * @return 如果成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    @Override
    public boolean existsPath(String path) throws UserException {
        if (!StringUtils.hasText(path))
            return false;
        var ids = path.split("/");
        if (ids.length < 2)
            return false;
        // 第一个默认是数据源
        var sourceId = ids[0];
        var source = dataStructMapper.getById(sourceId);
        if (source == null)
            return false;
        // 中间是目录的Id
        var groupIds = new ArrayList<String>(Arrays.asList(ids).subList(1, ids.length - 1));
        var groups = new ArrayList<DataGroup>();
        if (!groupIds.isEmpty()) {
            var tempGroups = dataGroupMapper.list(groupIds);
            var groupsMap = new HashMap<String, DataGroup>();
            for (var group : tempGroups) {
                groupsMap.put(group.getId(), group);
            }
            for (var id : groupIds) {
                try {
                    groups.add(groupsMap.get(id));
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    return false;
                }
            }
        }
        if (groups.size() != Math.max(0, ids.length - 2))
            return false;
        for (var i = groups.size() - 1; i > 0; --i) {
            if (!Objects.equals(groups.get(i).getParent(), groups.get(i - 1).getId()))
                return false;
        }
        if (!groups.isEmpty() && !Objects.equals(groups.get(0).getParent(), sourceId)) {
            return false;
        }
        // 最后一个可能是目录可能是笔记本的Id
        // 先判断是不是目录的Id
        var lastGroup = dataGroupMapper.getById(ids[ids.length - 1]);
        if (lastGroup == null) {
            var notebook = notebookMapper.getById(ids[ids.length - 1]);
            if (notebook == null)
                return false;
            return Objects.equals(notebook.getParent(), sourceId)
                    || (groupIds.isEmpty() || Objects.equals(notebook.getParent(), groupIds.get(groupIds.size() - 1)));
        } else {
            return Objects.equals(lastGroup.getParent(), sourceId)
                    || (groupIds.isEmpty() || Objects.equals(lastGroup.getParent(), groupIds.get(groupIds.size() - 1)));
        }
    }

    @Override
    public boolean copyDirectory(String sourceId, String oldPath, String newPath) throws UserException {
        // 检测是否是同一个数据源
        var oldPaths = Arrays.stream(oldPath.split("/")).toList();
        var newPaths = new ArrayList<>(Arrays.stream(newPath.split("/")).toList());
        if (!sourceId.equals(oldPaths.get(0)) || !sourceId.equals(newPaths.get(0)))
            throw new UserException(StatusCode.PermissionDeny);
        if (oldPaths.size() < 2)
            throw new UserException(StatusCode.PermissionDeny);
        if (newPaths.size() >= 2) {
            if (newPaths.get(newPaths.size() - 1).equals(oldPaths.get(oldPaths.size() - 1))) {
                newPaths.remove(newPaths.size() - 1);
            }
        }
        var lastId = oldPaths.get(oldPaths.size() - 1);
        var lastGroup = dataGroupMapper.getById(lastId);
        Optional<Notebook> lastNotebook = Optional.empty();
        if (lastGroup == null) {
            lastNotebook = notebookMapper.list(Collections.singletonList(lastId)).stream().findFirst();
        }
        // 检查路径是否正确
        if (oldPaths.size() >= 3) {
            var groupIds = oldPaths.subList(1, oldPaths.size() - 2);
            if (!groupIds.isEmpty()) {
                checkGroupPaths(groupIds);
            }
        }
        if (newPaths.size() >= 2) {
            var groupIds = newPaths.subList(1, newPaths.size() - 1);
            if (!groupIds.isEmpty()) {
                checkGroupPaths(groupIds);
            }
        }
        if (lastNotebook.isPresent()) {
            // 在新的目录中创建新的笔记本
            var newNotebook = lastNotebook.get().clone();
            // changeDirectory
            newNotebook.setId(null);
            newNotebook.setParent(newPaths.get(newPaths.size() - 1));
            newNotebook.setId(RandomUtil.DateTimeUUID());
            newNotebook = notebookMapper.create(newNotebook);
            if (newNotebook == null)
                return false;
            var lastContent = notebookMapper.getContent(lastNotebook.get().getId());
            lastContent.setNotebookId(newNotebook.getId());
            lastContent.setVersionId(RandomUtil.DateTimeUUID());
            lastContent = notebookMapper.updateContent(lastContent);
            newNotebook.setContent(lastContent);
            var path = getGroupParentPath(newNotebook.getParent());
            var sourcePath = newNotebook.getSourceId() + "/" + path + "/" + newNotebook.getId();
            if (!StringUtils.hasText(path)) {
                sourcePath = newNotebook.getSourceId() + "/" + newNotebook.getId();
            }
            Notebook finalNewNotebook = newNotebook;
            String finalSourcePath = sourcePath;
            threadPoolTaskExecutor.submit(new Thread(() -> {
                sendChokidarEvent(finalNewNotebook.getSourceId(), new ChokidarEvent().setEvent(ChangeType.add)
                        .setFile(true).setPath(finalSourcePath).setRelativePath(finalNewNotebook.getId())
                        .setName(finalNewNotebook.getName()));
            }));
            return true;
        } else if (lastGroup != null) {
            // 判断目录是否内嵌
            if (newPaths.size() >= oldPaths.size()) {
                boolean isSame = true;
                for (var i = 1; i < oldPaths.size(); ++i) {
                    if (!oldPaths.get(i).equals(newPaths.get(i))) {
                        isSame = false;
                        break;
                    }
                }
                if (isSame)
                    throw new UserException(StatusCode.PathRepeat);
            }
            // 复制目录下的所有目录的和笔记
            var groups = new ArrayList<DataGroup>();
            groups.add(lastGroup);
            groups.addAll(getDirectoryTree(lastGroup.getId()));
            var mapper = new HashMap<String, String>();
            // 更新更目录
            mapper.put(lastGroup.getParent(), newPaths.get(newPaths.size() - 1));
            for (var group : groups) {
                var newGroup = group.clone();
                newGroup.setParent(mapper.get(group.getParent()));
                newGroup.setId(RandomUtil.DateTimeUUID());
                mapper.put(group.getId(), newGroup.getId());
                newGroup = dataGroupMapper.create(newGroup);
                if (newGroup == null)
                    continue;
                // 添加新的分组消息
                var path = getGroupParentPath(newGroup.getParent());
                path = StringUtils.hasText(path) ? sourceId + "/" + path + "/" + newGroup.getId()
                        : sourceId + "/" + newGroup.getId();
                DataGroup finalNewGroup = newGroup;
                String finalPath = path;
                threadPoolTaskExecutor.submit(new Thread(() -> {
                    sendChokidarEvent(finalNewGroup.getSourceId(), new ChokidarEvent()
                            .setEvent(ChangeType.addDir)
                            .setName(finalNewGroup.getName())
                            .setPath(finalPath)
                            .setRelativePath(finalNewGroup.getId())
                            .setDir(true));
                }));
                var notebooks = notebookMapper.listByParent(group.getId());
                for (var notebook : notebooks) {
                    var newNotebook = notebook.clone();
                    newNotebook.setId(RandomUtil.DateTimeUUID());
                    newNotebook.setParent(newGroup.getId());
                    newNotebook = notebookMapper.create(newNotebook);
                    if (newNotebook != null) {
                        var content = notebookMapper.getContent(notebook.getId());
                        if (content != null) {
                            content.setVersionId(RandomUtil.DateTimeUUID());
                            content.setNotebookId(newNotebook.getId());
                            notebookMapper.updateContent(content);
                        }
                        Notebook finalNewNotebook = newNotebook;
                        var notebookPath = getGroupParentPath(newNotebook.getParent());
                        var sourcePath = newNotebook.getSourceId() + "/" + notebookPath + "/" + newNotebook.getId();
                        if (!StringUtils.hasText(notebookPath)) {
                            sourcePath = newNotebook.getSourceId() + "/" + newNotebook.getId();
                        }
                        String finalSourcePath = sourcePath;
                        threadPoolTaskExecutor.submit(new Thread(() -> {
                            sendChokidarEvent(finalNewNotebook.getSourceId(), new ChokidarEvent()
                                    .setEvent(ChangeType.add)
                                    .setName(finalNewNotebook.getName())
                                    .setPath(finalSourcePath)
                                    .setRelativePath(finalNewNotebook.getId())
                                    .setFile(true));
                        }));
                    }
                }
            }
            return true;
        }
        return false;
    }

    private List<DataGroup> getDirectoryTree(String parentId) {
        var groups = dataGroupMapper.listByParent(parentId);
        var children = new ArrayList<DataGroup>();
        for (var group : groups) {
            children.addAll(dataGroupMapper.listByParent(group.getId()));
        }
        groups.addAll(children);
        return groups;
    }

    /**
     * 检查Id的路径是否正确，是否满足前后相连的条件，如果不满足则会抛出PathNotFound的用户异常
     *
     * @param groupIds 分组的Id
     * @throws UserException PathNotFound
     */
    private void checkGroupPaths(List<String> groupIds) throws UserException {
        var groups = dataGroupMapper.list(groupIds);
        var groupMapper = new HashMap<String, DataGroup>();
        for (var group : groups) {
            groupMapper.put(group.getId(), group);
        }
        for (var i = 1; i < groupIds.size(); ++i) {
            try {
                if (!groupMapper.get(groupIds.get(i)).getParent().equals(groupIds.get(i - 1))) {
                    throw new UserException(StatusCode.PathNotFound);
                }
            } catch (UserException ex) {
                throw ex;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new UserException(StatusCode.PathNotFound);
            }
        }
    }

    @Override
    public boolean moveDirectory(String sourceId, String oldPath, String newPath) throws UserException {
        // 检测是否是同一个数据源
        var oldPaths = Arrays.stream(oldPath.split("/")).toList();
        var newPaths = new ArrayList<>(Arrays.stream(newPath.split("/")).toList());
        if (oldPaths.size() < 2)
            throw new UserException(StatusCode.PermissionDeny);
        if (!sourceId.equals(oldPaths.get(0)) || !sourceId.equals(newPaths.get(0)))
            throw new UserException(StatusCode.PermissionDeny);
        if (newPaths.size() >= 2) {
            if (newPaths.get(newPaths.size() - 1).equals(oldPaths.get(oldPaths.size() - 1))) {
                newPaths.remove(newPaths.size() - 1);
            }
        }
        // 检查路径是否正确
        if (oldPaths.size() >= 3) {
            var groupIds = oldPaths.subList(1, oldPaths.size() - 2);
            if (!groupIds.isEmpty()) {
                checkGroupPaths(groupIds);
            }
        }
        if (newPaths.size() >= 2) {
            var groupIds = newPaths.subList(1, newPaths.size() - 1);
            if (!groupIds.isEmpty()) {
                checkGroupPaths(groupIds);
            }
        }
        var lastId = oldPaths.get(oldPaths.size() - 1);
        var lastGroup = dataGroupMapper.getById(lastId);
        Notebook lastNoteBook = null;
        if (lastGroup != null) {
            lastNoteBook = notebookMapper.getById(lastId);
        }
        if (lastNoteBook != null) {
            lastNoteBook.setParent(newPaths.get(newPaths.size() - 1));
            if (notebookMapper.update(lastNoteBook) != null) {
                Notebook finalLastNoteBook = lastNoteBook;
                threadPoolTaskExecutor.submit(() -> {
                    sendChokidarEvent(sourceId, new ChokidarEvent()
                            .setEvent(ChangeType.unlink)
                            .setPath(oldPath)
                            .setName(finalLastNoteBook.getName())
                            .setRelativePath(finalLastNoteBook.getId())
                            .setFile(true));
                });
                threadPoolTaskExecutor.submit(() -> {
                    sendChokidarEvent(sourceId, new ChokidarEvent()
                            .setEvent(ChangeType.add)
                            .setPath(newPath + "/" + finalLastNoteBook.getId())
                            .setName(finalLastNoteBook.getName())
                            .setFile(true)
                            .setRelativePath(finalLastNoteBook.getId()));
                });
                return true;
            }
        } else if (lastGroup != null) {
            lastGroup.setParent(newPaths.get(newPaths.size() - 1));
            if (dataGroupMapper.update(lastGroup) != null) {
                var finalLastGroup = lastGroup;
                threadPoolTaskExecutor.submit(() -> {
                    sendChokidarEvent(sourceId, new ChokidarEvent()
                            .setEvent(ChangeType.unlinkDir)
                            .setPath(oldPath)
                            .setName(finalLastGroup.getName())
                            .setDir(true)
                            .setRelativePath(finalLastGroup.getId()));
                });
                threadPoolTaskExecutor.submit(() -> {
                    sendChokidarEvent(sourceId, new ChokidarEvent()
                            .setEvent(ChangeType.addDir)
                            .setPath(newPath + "/" + finalLastGroup.getId())
                            .setRelativePath(finalLastGroup.getId())
                            .setName(finalLastGroup.getName())
                            .setDir(true));
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public String idsPathToNamePath(String idsPath) throws UserException {
        var ids = new ArrayList<>(List.of(idsPath.split("/")));
        if (ids.isEmpty())
            return "";
        var sourceId = ids.get(0);
        var names = new ArrayList<String>();
        var source = dataStructMapper.getById(sourceId);
        if (source == null)
            throw new UserException(StatusCode.DataSourceNotExists);
        names.add(source.getName());
        for (int i = 2; i < ids.size() - 1; ++i) {
            var group = dataGroupMapper.getById(ids.get(i));
            if (group == null)
                throw new UserException(StatusCode.GroupNotExists);
            names.add(group.getName());
        }
        if (ids.size() > 1) {
            var lastId = ids.get(ids.size() - 1);
            var group = dataGroupMapper.getById(lastId);
            if (group != null) {
                names.add(group.getName());
            } else {
                var notebook = notebookMapper.getById(lastId);
                if (notebook == null)
                    throw new UserException(StatusCode.PathNotFound);
                names.add(notebook.getName());
            }
        }
        return String.join("/", names);
    }

    @Override
    // 上传Base64编码的图片
    public String uploadBase64Image(String base64Image) {

        try {
            String[] imageName={"png","jpg"};
            int name=0; //默认为png格式文件
            String base64EncodedImage;
            if (base64Image.startsWith("data:image/png;base64,")) {
                base64EncodedImage = base64Image.substring("data:image/png;base64,".length());
                System.out.println(base64EncodedImage);
                name=0;
            }else if (base64Image.startsWith("data:image/jpg;base64,")){
                base64EncodedImage = base64Image.substring("data:image/jpg;base64,".length());
                System.out.println(base64EncodedImage);
                name=1;

            }else {
                base64EncodedImage = base64Image;
            }

            // Base64解码
            byte[] imageBytes = Base64.decodeBase64(base64EncodedImage.getBytes(StandardCharsets.UTF_8));
//            byte[] imageBytes = Base64.getDecoder().decode(base64EncodedImage);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);


            // 生成唯一的文件名
            String fileName = RandomUtil.DateTimeUUID().toString() + "."+imageName[name];
            //实例化minio
            MinioClient minioClient=getClientInstance();
//            try (FileOutputStream fos = new FileOutputStream("E:\\Adataset\\Ftest\\local_test.png")) {
//                fos.write(imageBytes);
//            }

            // 保存到MinIO
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(configuration.getBucket()).object(fileName)
                            .stream(bais, imageBytes.length, -1)
                            .contentType("image/"+imageName[name])
                            .build());

            // 返回文件名或者路径
            logger.info("成功将guid:{}上传到:{}",fileName,configuration.getBucket());
            return fileName;
        }catch (MinioException e) {
            System.err.println("错误: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("错误: " + e);
            e.printStackTrace();
        }

        return null;
    }

    public enum ChangeType {
        add,
        addDir,
        unlinkDir,
        unlink,
        change,
        healthCheck
    }

    /**
     * Chokidar 事件
     */
    public class ChokidarEvent {

        /**
         * 事件
         */
        private ChangeType event;

        /**
         * 路径
         */
        private String path;

        /**
         * 相对路径
         */
        private String relativePath;

        /**
         * 名字
         */
        private String name;

        /**
         * 是否是目录
         */
        private Boolean isDir;

        /**
         * 是否是文件
         */
        private Boolean isFile;

        public ChangeType getEvent() {
            return event;
        }

        public ChokidarEvent setEvent(ChangeType event) {
            this.event = event;
            return this;
        }

        public String getPath() {
            return path;
        }

        public ChokidarEvent setPath(String path) {
            this.path = path;
            return this;
        }

        public String getRelativePath() {
            return relativePath;
        }

        public ChokidarEvent setRelativePath(String relativePath) {
            this.relativePath = relativePath;
            return this;
        }

        public String getName() {
            return name;
        }

        public ChokidarEvent setName(String name) {
            this.name = name;
            return this;
        }

        public Boolean getDir() {
            return isDir;
        }

        public ChokidarEvent setDir(Boolean dir) {
            isDir = dir;
            return this;
        }

        public Boolean getFile() {
            return isFile;
        }

        public ChokidarEvent setFile(Boolean file) {
            isFile = file;
            return this;
        }
    }

}

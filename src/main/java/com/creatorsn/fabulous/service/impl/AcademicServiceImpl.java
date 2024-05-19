package com.creatorsn.fabulous.service.impl;

import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.entity.*;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.mapper.*;
import com.creatorsn.fabulous.service.AcademicService;
import com.creatorsn.fabulous.util.FileManager;
import com.creatorsn.fabulous.util.RandomUtil;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author minskiter
 * @date 24/8/2023 12:09
 * @description 学术服务实现
 */
@Service
public class AcademicServiceImpl implements AcademicService {

    final private DataStructMapper dataStructMapper;
    final private DataGroupMapper dataGroupMapper;
    final private DataPartitionMapper dataPartitionMapper;
    private final ItemMetadataMapper itemMetadataMapper;
    private final DataItemMapper dataItemMapper;
    private final DataPageMapper dataPageMapper;
    private final RedissonClient redissonClient;

    private final Logger logger = LoggerFactory.getLogger(AcademicServiceImpl.class);
    private final FileManager fileManager;
    private final DataTemplateMapper dataTemplateMapper;
    private final UserMapper userMapper;

    public AcademicServiceImpl(
            DataStructMapper dataStructMapper,
            DataGroupMapper dataGroupMapper,
            DataPartitionMapper dataPartitionMapper,
            DataItemMapper dataItemMapper,
            ItemMetadataMapper itemMetadataMapper,
            DataPageMapper dataPageMapper,
            DataTemplateMapper dataTemplateMapper,
            RedissonClient redissonClient,
            FileManager fileManager,
            UserMapper userMapper
    ) {
        this.dataGroupMapper = dataGroupMapper;
        this.dataStructMapper = dataStructMapper;
        this.dataPartitionMapper = dataPartitionMapper;
        this.dataItemMapper = dataItemMapper;
        this.itemMetadataMapper = itemMetadataMapper;
        this.dataPageMapper = dataPageMapper;
        this.redissonClient = redissonClient;
        this.fileManager = fileManager;
        this.dataTemplateMapper = dataTemplateMapper;
        this.userMapper = userMapper;
    }

    /**
     * @param sourceId 数据源的Id
     * @return 返回对应的分组列表
     */
    @Override
    public List<DataGroup> getDataGroupBySourceId(String sourceId) throws UserException {
        // 判断sourceId是否存在
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        return dataGroupMapper.listByParent(source.getId());
    }

    /**
     * @param parentId 父亲节点的Id
     * @return 返回对应的分组列表
     */
    @Override
    public List<DataGroup> getDataGroupByParentId(String parentId) throws UserException {
        var parentGroup = dataGroupMapper.getById(parentId);
        if (parentGroup == null) throw new UserException(StatusCode.GroupNotExists);
        return dataGroupMapper.listByParent(parentGroup.getId());
    }

    /**
     * @param dataGroup 数据分组
     * @return 返回创建后的数据分组
     */
    @Override
    public DataGroup createGroup(DataGroup dataGroup) throws UserException {
        // 判断这个分组下的名称是否重复
        var parentId = dataGroup.getParent();
        if (dataGroupMapper.existsByName(dataGroup.getName(), parentId)) {
            throw new UserException(StatusCode.GroupNameRepeated);
        }
        dataGroup.setId(RandomUtil.DateTimeUUID());
        return dataGroupMapper.create(dataGroup);
    }

    /**
     * @param dataGroup 数据分组
     * @return 更新数据分组的信息
     */
    @Override
    public DataGroup updateGroup(DataGroup dataGroup) throws UserException {
        // 判断分组的名称是否重复
        var old = dataGroupMapper.getById(dataGroup.getId());
        if (old == null) throw new UserException(StatusCode.GroupNotExists);
        var parentId = old.getParent();
        if (dataGroup.getParent() != null) {
            parentId = dataGroup.getParent();
        }
        if (!Objects.equals(parentId, old.getParent()) || (dataGroup.getName() != null && !Objects.equals(dataGroup.getName(), old.getName()))) {
            // 确保名称没有重复
            if (dataGroupMapper.existsByName(dataGroup.getName(), parentId)) {
                throw new UserException(StatusCode.GroupNameRepeated);
            }
            if (!Objects.equals(parentId, old.getParent())) {
                // 确保移动后仍然在当前数据源下
                if (!Objects.equals(parentId, old.getSourceId())) {
                    var parent = dataGroupMapper.getById(parentId);
                    if (!Objects.equals(parent.getSourceId(), old.getSourceId())) {
                        throw new UserException(StatusCode.PermissionDeny);
                    }
                }
            }
        }
        return dataGroupMapper.update(dataGroup);
    }

    /**
     * @param sourceId 数据源
     * @param groupId  分组的Id
     * @param userId   用户的Id
     * @return 删除指定的分组
     */
    @Override
    public boolean deleteGroup(String sourceId, String groupId, String userId) throws UserException {
        var group = dataGroupMapper.getById(groupId);
        if (!Objects.equals(group.getSourceId(), sourceId) || !Objects.equals(group.getOwner(), userId)) {
            throw new UserException(StatusCode.PermissionDeny);
        }
        return dataGroupMapper.delete(groupId);
    }

    /**
     * @param sourceId 数据源
     * @return 返回获取到的数据分区
     */
    @Override
    public List<DataPartition> getPartitionsBySourceId(String sourceId) throws UserException {
        // 判断数据源是否存在
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        return dataPartitionMapper.listByParent(sourceId);
    }

    /**
     * @param parentId 分区的Id
     * @return 返回指定的分区列表
     */
    @Override
    public List<DataPartition> getPartitionsByParentId(String parentId) throws UserException {
        var group = dataGroupMapper.getById(parentId);
        if (group == null) throw new UserException(StatusCode.GroupNotExists);
        return dataPartitionMapper.listByParent(group.getId());
    }

    /**
     * @param sourceId 数据源的Id
     * @param id       数据分区的Id
     * @return 返回指定的数据分区
     * @throws UserException 用户异常
     */
    @Override
    public DataPartition getPartitionById(String sourceId, String id) throws UserException {
        var partition = dataPartitionMapper.getById(id);
        if (partition == null) throw new UserException(StatusCode.PartitionNotExists);
        if (!Objects.equals(partition.getSourceId(), sourceId)) {
            throw new UserException(StatusCode.PartitionNotExists);
        }
        return partition;
    }

    /**
     * @param partition 分区
     * @return 返回创建后的分区信息
     */
    @Override
    public DataPartition createPartition(DataPartition partition) throws UserException {
        var source = dataStructMapper.getById(partition.getSourceId());
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        if (!Objects.equals(partition.getParent(), partition.getSourceId())) {
            var group = dataGroupMapper.getById(partition.getParent());
            if (group == null) throw new UserException(StatusCode.GroupNotExists);
            if (!Objects.equals(group.getSourceId(), source.getId()))
                throw new UserException(StatusCode.PermissionDeny);
        }
        partition.setId(RandomUtil.DateTimeUUID());
        return dataPartitionMapper.create(partition);
    }

    /**
     * @param partition 分区
     * @return 返回更新后的分区信息
     */
    @Override
    public DataPartition updatePartition(DataPartition partition) throws UserException {
        var old = dataPartitionMapper.getById(partition.getId());
        if (old == null || !Objects.equals(old.getSourceId(), partition.getSourceId()))
            throw new UserException(StatusCode.PermissionDeny);
        if (partition.getParent() != null && !Objects.equals(old.getParent(), partition.getParent())) {
            var group = dataGroupMapper.getById(partition.getId());
            if (!Objects.equals(group.getSourceId(), old.getSourceId())) {
                throw new UserException(StatusCode.PermissionDeny);
            }
        }
        return dataPartitionMapper.update(partition);
    }

    /**
     * @param sourceId 源的Id
     * @param groupId  分组的Id
     * @param id       分区的Id
     * @return 如果成功删除则返回true，否则返回false
     */
    @Override
    public boolean deletePartition(String sourceId, String groupId, String id) throws UserException {
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        var partition = dataPartitionMapper.getById(id);
        if (!Objects.equals(partition.getParent(), groupId)) {
            throw new UserException(StatusCode.PermissionDeny);
        }
        return dataPartitionMapper.delete(id);
    }

    /**
     * @param item 数据项
     * @return 返回数据项
     */
    @Override
    public DataItem createItem(DataItem item) throws UserException {
        item.setId(RandomUtil.DateTimeUUID());
        var metadata = new ItemMetadata()
                .setId(RandomUtil.DateTimeUUID());
        item.setMetadataId(metadata.getId());
        item = dataItemMapper.create(item);
        if (item == null) return null;
        metadata = itemMetadataMapper.create(metadata);
        item.setMetadata(metadata);
        return item;
    }

    /**
     * @param item 数据项
     * @return 返回更新后的数据项
     */
    @Override
    public DataItem updateItem(DataItem item) throws UserException {
        var older = dataItemMapper.getById(item.getId());
        if (older == null) {
            throw new UserException(StatusCode.ItemNotExists);
        }
        item = dataItemMapper.update(item);
        return item;
    }

    /**
     * @param id 数据项的Id
     * @return 返回对应的数据项信息
     */
    @Override
    public DataItem getItem(String id) {
        return dataItemMapper.getById(id);
    }

    /**
     * @param id 数据项的Id
     * @return 如果删除则返回true，否则返回false
     */
    @Override
    public boolean deleteItem(String id) {
        return dataItemMapper.delete(id);
    }

    /**
     * @param ids 数据项的Id
     * @return 返回数据项的列表
     */
    @Override
    public List<DataItem> listItems(List<String> ids) {
        return dataItemMapper.list(ids);
    }

    /**
     * @param parentId 分区的Id
     * @param offset   偏移量
     * @param length   页数
     * @param sort     排序
     * @param desc     是否递减
     * @return 返回数据项的列表
     */
    @Override
    public List<DataItem> listItemsByParent(String parentId, long offset, int length, String sort, boolean desc) {
        var items = dataItemMapper.listByParent(parentId, offset, length, sort, desc);
        for (var item : items) {
            item.setPages(new ArrayList<>());
        }
        var ids = items.stream().map(DataItem::getId).toList();
        if (!ids.isEmpty()) {
            var pages = dataPageMapper.listByParents(ids);
            var mapper = new HashMap<String, DataItem>();
            for (var item : items) {
                mapper.put(item.getId(), item);
            }
            for (var page : pages) {
                var item = mapper.get(page.getParent());
                if (item != null) {
                    item.getPages().add(page);
                }
            }
        }
        return items;
    }

    /**
     * @param parentId 分区的id
     * @return 返回数据项的数量
     */
    @Override
    public long getItemsCountByParent(String parentId) {
        return dataItemMapper.getCountByParent(parentId);
    }

    /**
     * @param sourceId 数据源
     * @param offset   偏移量
     * @param length   长度
     * @param sort     排序的关键字
     * @param desc     是否递减
     * @return 返回指定数据源下的所有数据项
     */
    @Override
    public List<DataItem> listItemsBySource(String sourceId, long offset, int length, String sort, boolean desc) throws UserException {
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        var items = dataItemMapper.listBySourceId(sourceId, offset, length, sort, desc);
        for (var item : items) {
            item.setPages(new ArrayList<>());
        }
        var ids = items.stream().map(DataItem::getId).toList();
        if (!ids.isEmpty()) {
            var pages = dataPageMapper.listByParents(ids);
            var mapper = new HashMap<String, DataItem>();
            for (var item : items) {
                mapper.put(item.getId(), item);
            }
            for (var page : pages) {
                var item = mapper.get(page.getParent());
                if (item != null) {
                    item.getPages().add(page);
                }
            }
        }
        return items;
    }

    /**
     * @param sourceId 数据源
     * @return 返回指定数据源下的所有数据项的数目
     */
    @Override
    public long getItemsCountBySource(String sourceId) throws UserException {
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        return dataItemMapper.getCountBySourceId(sourceId);
    }

    /**
     * @param sourceId 数据源的Id
     * @param ids      数据项的id列表
     * @return 返回删除的数据项id列表
     */
    @Override
    public List<String> deleteItemList(String sourceId, List<String> ids) {
        if (ids.isEmpty()) return new ArrayList<>();
        return dataItemMapper.deleteList(sourceId, ids);
    }

    /**
     * @param id          数据项的Id
     * @param partitionId 分区的Id
     * @param operator    操作人
     * @return 如果成功则返回true，否则返回false
     */
    @Override
    public boolean addToPartition(String id, String partitionId, String operator) {
        return dataItemMapper.addToPartition(id, partitionId, operator);
    }

    /**
     * @param ids         数据项的Id列表
     * @param partitionId 分区的Id
     * @param operator    操作人
     * @return 返回添加成功的数据项Id
     */
    @Override
    public List<String> addListToPartition(List<String> ids, String partitionId, String operator) {
        if (ids.isEmpty()) return new ArrayList<>();
        return dataItemMapper.addListToPartition(ids, partitionId, operator);
    }

    /**
     * @param id          数据项的Id
     * @param partitionId 分区的Id
     * @return 如果成功则返回true，否则返回false
     */
    @Override
    public boolean removeFromPartition(String id, String partitionId) {
        return dataItemMapper.removeFromPartition(id, partitionId);
    }

    /**
     * @param ids         数据项的Id列表
     * @param partitionId 分区的Id
     * @return 返回批量数据项的Id
     */
    @Override
    public List<String> removeListFromPartition(List<String> ids, String partitionId) {
        if (ids.isEmpty()) return new ArrayList<>();
        return dataItemMapper.removeListFromPartition(ids, partitionId);
    }

    /**
     * @param sourceId 数据源的Id
     * @param parentId 分区的Id
     * @param keyword  关键字
     * @param offset   偏移量
     * @param length   长度
     * @param sort     排序关键字
     * @param desc     是否递减
     * @return 返回符合条件的数据项
     */
    @Override
    public List<DataItem> queryItems(String sourceId, String parentId, String keyword, long offset, int length, String sort, boolean desc) {
        var items = dataItemMapper.query(parentId, sourceId, keyword, offset, length, sort, desc);
        for (var item : items) {
            item.setPages(new ArrayList<>());
        }
        var ids = items.stream().map(DataItem::getId).toList();
        if (!ids.isEmpty()) {
            var pages = dataPageMapper.listByParents(ids);
            var mapper = new HashMap<String, DataItem>();
            for (var item : items) {
                mapper.put(item.getId(), item);
            }
            for (var page : pages) {
                var item = mapper.get(page.getParent());
                if (item != null) {
                    item.getPages().add(page);
                }
            }
        }
        return items;
    }

    /**
     * @param sourceId 源的Id
     * @param itemId   数据项的信息
     * @param metadata 元数据
     * @return 返回更新后的数据项信息
     */
    @Override
    public ItemMetadata updateItemMetadata(String sourceId, String itemId, ItemMetadata metadata) throws UserException {
        var item = dataItemMapper.getById(itemId);
        if (item == null || !Objects.equals(sourceId, item.getSourceId()))
            throw new UserException(StatusCode.PermissionDeny);
        if (metadata.getId() == null) {
            metadata.setId(item.getMetadataId());
        }
        if (!Objects.equals(item.getMetadataId(), metadata.getId())) {
            throw new UserException(StatusCode.PermissionDeny);
        }
        return itemMetadataMapper.update(metadata);
    }

    /**
     * @param sourceId 数据源
     * @param page     数据项笔记元信息
     * @return 返回创建后的数据项笔记
     */
    @Override
    public DataPage createItemPage(String sourceId, DataPage page) throws UserException {
        // 检查数据项是否存在
        if (page.getParent() == null) throw new UserException(StatusCode.PermissionDeny);
        var item = dataItemMapper.getById(page.getParent());
        if (item == null) throw new UserException(StatusCode.ItemNotExists);
        page.setId(RandomUtil.DateTimeUUID());
        var content = page.getContent();
        page = dataPageMapper.create(page);
        if (page == null) return null;
        page.setContent(content);
        if (page.getContent() != null) {
            var contentEntity = dataPageMapper.updateContent(
                    RandomUtil.DateTimeUUID(),
                    page.getId(),
                    page.getOwner(),
                    page.getContent()
            );
            page.setVersionId(contentEntity.getVersionId());
        }
        return page;
    }

    /**
     * @param sourceId 数据源的Id
     * @param page     数据项的笔记的Id
     * @return 返回更新后的数据笔记
     */
    @Override
    public DataPage updateItemPage(String sourceId, DataPage page) throws UserException {
        var older = dataPageMapper.getById(page.getId());
        if (older == null) throw new UserException(StatusCode.PageNotExists);
        var oldItem = dataItemMapper.getById(older.getParent());
        if (!Objects.equals(oldItem.getSourceId(), sourceId)) throw new UserException(StatusCode.PermissionDeny);
        if (!Objects.equals(page.getParent(), oldItem.getId())) {
            var newItem = dataItemMapper.getById(page.getParent());
            if (newItem == null || !newItem.getSourceId().equals(sourceId))
                throw new UserException(StatusCode.PermissionDeny);
        }
        return dataPageMapper.update(page);
    }

    /**
     * @param sourceId 数据源的Id
     * @param pageId   数据项笔记的Id
     * @return 如果成功删除则返回true，否则返回false
     */
    @Override
    public boolean deleteItemPage(String sourceId, String pageId) throws UserException {
        var page = dataPageMapper.getById(pageId);
        if (page == null) throw new UserException(StatusCode.PageNotExists);
        var item = dataItemMapper.getById(page.getParent());
        if (item == null) throw new UserException(StatusCode.PageNotExists);
        if (!item.getSourceId().equals(sourceId)) throw new UserException(StatusCode.PermissionDeny);
        return dataPageMapper.delete(pageId);
    }

    /**
     * @param sourceId 数据源
     * @param itemId   数据项的笔记
     * @return 返回符合条件的所有数据笔记信息
     */
    @Override
    public List<DataPage> listItemPagesByItemId(String sourceId, String itemId) {
        var pages = dataPageMapper.listByParent(itemId);
        return pages;
    }

    /**
     * @param sourceId     数据源的Id
     * @param sourcePageId 数据项的笔记本Id
     * @return 返回复制后的数据项的笔记
     */
    @Override
    public DataPage duplicateItemPage(String sourceId, String sourcePageId) throws UserException {
        var older = dataPageMapper.getById(sourcePageId);
        older.setId(RandomUtil.DateTimeUUID());
        var oldContent = dataPageMapper.getLatestContent(sourcePageId);
        if (oldContent == null) throw new UserException(StatusCode.ContentEmpty);
        // 复制内容
        var newer = dataPageMapper.create(older);
        oldContent.setVersionId(RandomUtil.DateTimeUUID());
        var newContent = dataPageMapper.updateContent(
                oldContent.getVersionId(),
                newer.getId(),
                newer.getOwner(),
                oldContent.getContent()
        );
        newer.setContent(newContent.getContent());
        newer.setVersionId(newContent.getVersionId());
        return newer;
    }

    /**
     * @param sourceId 数据源的Id
     * @param content  要更新的笔记本内容
     * @return 更新数据笔记的内容
     */
    @Override
    public DataPageContent updateItemPageContent(String sourceId, DataPageContent content) throws UserException {
        var page = dataPageMapper.getById(content.getPageId());
        if (page == null) throw new UserException(StatusCode.PageNotExists);
        // lock
        var lock = redissonClient.getLock("updated:" + page.getId());
        lock.lock(10, TimeUnit.MINUTES);
        try {
            var old = dataPageMapper.getLatestContent(content.getPageId());
            if (old == null || !old.getVersionId().equals(content.getVersionId())) {
                throw new UserException(StatusCode.NotUpToDate, old);
            }
            content.setVersionId(RandomUtil.DateTimeUUID());
            content = dataPageMapper.updateContent(
                    content.getVersionId(),
                    content.getPageId(),
                    content.getAuthor(),
                    content.getContent()
            );
            return content;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            if (ex instanceof UserException) {
                throw ex;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param sourceId 数据源的Id
     * @param pageId   笔记的Id
     * @return 返回最新的数据源
     */
    @Override
    public DataPageContent getItemPageContent(String sourceId, String pageId) {
        var latest = dataPageMapper.getLatestContent(pageId);
        return latest;
    }

    /**
     * @param sourceId  数据源的Id
     * @param pageId    笔记的Id
     * @param versionId 版本的Id
     * @return
     */
    @Override
    public DataPageContent getItemPageContentByVersionId(String sourceId, String pageId, String versionId) throws UserException {
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        var content = dataPageMapper.getContentById(pageId, versionId);
        return content;
    }

    @Override
    public List<DataPageContent> getItemPageContentVersions(String sourceId, String pageId) throws UserException {
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        var versions = dataPageMapper.listContentIds(pageId);
        // 根据前端需要增加用户邮箱信息
        var authors = versions.stream().map(e -> e.getAuthor()).toList();
        var users = userMapper.list(authors);
        var userMappers = new HashMap<String, User>();
        for (var user : users) {
            userMappers.put(user.getId(), user);
        }
        for (var version : versions) {
            version.setEmail(userMappers.get(version.getAuthor()).getEmail());
        }
        return versions;
    }

    /**
     * @param sourceId 数据源的Id
     * @param itemId   数据项的Id
     * @param file     文件
     * @return 如果成功则返回pdf的Id
     */
    @Override
    public String updateItemPDF(String sourceId, String itemId, String pdfId, MultipartFile file) throws UserException {
        var item = dataItemMapper.getById(itemId);
        if (item == null) throw new UserException(StatusCode.ItemNotExists);
        if (!item.getSourceId().equals(sourceId)) {
            throw new UserException(StatusCode.PermissionDeny);
        }
        if (pdfId != null && !item.getPdf().equals(pdfId))
            throw new UserException(StatusCode.PermissionDeny);
        if (pdfId == null) {
            try {
                pdfId = fileManager.create(file);
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
                return null;
            }
            item.setPdf(pdfId);
            if (dataItemMapper.update(item) == null) return null;
            return pdfId;
        } else {
            try {
                fileManager.replace(pdfId, file);
                return pdfId;
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
                return null;
            }
        }
    }

    /**
     * @param sourceId 数据源的Id
     * @param itemId   数据项的Id
     * @param pdfId    Pdf的Id
     * @return 返回数据项的Pdf的Id
     */
    @Override
    public MultipartFile getItemPDF(String sourceId, String itemId, String pdfId) throws UserException {
        var item = dataItemMapper.getById(itemId);
        if (item == null) throw new UserException(StatusCode.ItemNotExists);
        if (!item.getSourceId().equals(sourceId) || !item.getPdf().equals(pdfId)) {
            throw new UserException(StatusCode.PermissionDeny);
        }
        try {
            return fileManager.get(pdfId);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * @param sourceId 数据源
     * @return 返回模版的列表
     */
    @Override
    public List<DataTemplate> getTemplates(String sourceId) throws UserException {
        var source = dataStructMapper.getById(sourceId);
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        var templates = dataTemplateMapper.list(sourceId);
        var templateIds = templates.stream().map(DataTemplate::getId).toList();
        if (templateIds.isEmpty()) return templates;
        var contents = dataTemplateMapper.listLatestContent(templateIds);
        var mapper = new HashMap<String, DataTemplate>();
        for (var template : templates) {
            mapper.put(template.getId(), template);
        }
        for (var content : contents) {
            var template = mapper.get(content.getTemplateId());
            template.setContent(content.getContent());
            template.setVersionId(content.getVersionId());
        }
        return templates;
    }

    /**
     * @param sourceId   数据源
     * @param templateId 模版的Id
     * @return 返回模版的内容
     */
    @Override
    public DataTemplateContent getLatestTemplateContent(String sourceId, String templateId) throws UserException {
        var template = dataTemplateMapper.getById(templateId);
        if (template == null) throw new UserException(StatusCode.DataTemplateNotFound);
        if (!template.getParent().equals(sourceId)) throw new UserException(StatusCode.PermissionDeny);
        return dataTemplateMapper.getLatestContent(templateId);
    }

    /**
     * @param template 模版
     * @return 返回模版
     */
    @Override
    public DataTemplate createTemplate(DataTemplate template) throws UserException {
        var source = dataStructMapper.getById(template.getParent());
        if (source == null) throw new UserException(StatusCode.DataSourceNotExists);
        template.setId(RandomUtil.DateTimeUUID());
        template = dataTemplateMapper.create(template);
        if (template == null) return null;
        var latestContent = dataTemplateMapper.updateContent(
                RandomUtil.DateTimeUUID(),
                template.getId(),
                template.getOwner(),
                ""
        );
        if (latestContent != null) {
            template.setContent(latestContent.getContent());
            template.setVersionId(latestContent.getVersionId());
        }
        return template;
    }

    /**
     * @param template 模版
     * @return 返回更新后的模版
     */
    @Override
    public DataTemplate updateTemplate(DataTemplate template) throws UserException {
        var older = dataTemplateMapper.getById(template.getId());
        if (older == null) throw new UserException(StatusCode.DataTemplateNotFound);
        if (!older.getParent().equals(template.getParent())) throw new UserException(StatusCode.PermissionDeny);
        template = dataTemplateMapper.update(template);
        return template;
    }

    /**
     * @param sourceId   数据源的Id
     * @param templateId 模版的Id
     * @return 如果删除成功则返回true, 否则返回false
     */
    @Override
    public boolean deleteTemplate(String sourceId, String templateId) throws UserException {
        var older = dataTemplateMapper.getById(templateId);
        if (older == null || !older.getParent().equals(sourceId)) throw new UserException(StatusCode.PermissionDeny);
        return dataTemplateMapper.delete(templateId);
    }

    /**
     * @param sourceId   数据源的Id
     * @param templateId 模版的Id
     * @param versionId  版本的Id
     * @param userId     用户的Id
     * @param content    内容
     * @return 返回最新更新的模版内容
     */
    @Override
    public DataTemplateContent updateTemplateContent(String sourceId, String templateId, String versionId, String userId, String content) throws UserException {
        // 检测权限是否正确
        var template = dataTemplateMapper.getById(templateId);
        if (template == null || !template.getParent().equals(sourceId))
            throw new UserException(StatusCode.PermissionDeny);
        // 更新请求锁
        var lock = redissonClient.getLock("source:" + sourceId + ":template:" + templateId);
        try {
            lock.lock(10, TimeUnit.MINUTES);
            // 检测是否是最新的
            var latest = dataTemplateMapper.getLatestContent(templateId);
            if (!latest.getVersionId().equals(versionId)) {
                throw new UserException(StatusCode.NotUpToDate, latest);
            }
            return dataTemplateMapper.updateContent(RandomUtil.DateTimeUUID(), templateId, userId, content);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            if (ex instanceof UserException) {
                throw ex;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }


}

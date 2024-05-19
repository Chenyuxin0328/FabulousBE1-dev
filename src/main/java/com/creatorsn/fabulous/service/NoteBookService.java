package com.creatorsn.fabulous.service;

import com.creatorsn.fabulous.entity.DataGroup;
import com.creatorsn.fabulous.entity.DataGroupItem;
import com.creatorsn.fabulous.entity.NoteBookContent;
import com.creatorsn.fabulous.entity.Notebook;
import com.creatorsn.fabulous.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 笔记服务
 */
@Service
public interface NoteBookService {

    /**
     * 创建分组
     *
     * @param group 分组
     * @return 返回创建的分组
     */
    DataGroup createGroup(DataGroup group) throws UserException, ExecutionException, InterruptedException;

    /**
     * 删除分组
     *
     * @param owner 分组的拥有者
     * @param id    分组的Id
     * @return 如果成功删除分组，则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean deleteGroup(String owner, String id) throws UserException;

    /**
     * 更新分组信息
     *
     * @param owner 分组的拥有者
     * @param group 指定的分组
     * @return 返回更新后的分组信息
     * @throws UserException 用户异常
     */
    DataGroup updateGroup(String owner, DataGroup group) throws UserException;

    /**
     * 获取指定的分组信息
     *
     * @param id 分组的Id
     * @return 返回获取到分组信息
     * @throws UserException 用户异常
     */
    DataGroup getGroup(String id) throws UserException;

    /**
     * 获取分组的子分组
     *
     * @param id 分组的Id
     * @return 返回分组的所有子分组
     * @throws UserException 用户异常
     */
    List<DataGroup> getGroupChildren(String id, String owner) throws UserException;

    /**
     * 创建笔记本
     *
     * @param noteBook 笔记本
     * @return 返回创建后的笔记本
     * @throws UserException 用户异常
     */
    Notebook createNoteBook(Notebook noteBook) throws UserException;

    /**
     * 更新笔记本
     *
     * @param noteBook 笔记本
     * @return 返回更新后的笔记本
     * @throws UserException 用户异常
     */
    Notebook updateNoteBook(Notebook noteBook) throws UserException;

    /**
     * 获取分组下的所有笔记本
     *
     * @param groupId     分组的Id
     * @param owner       所属者
     * @param withContent 是否包含内容
     * @return 返回所有符合的笔记本列表
     * @throws UserException 用户异常
     */
    List<Notebook> getGroupNoteBooks(String groupId, String owner, boolean withContent) throws UserException;

    /**
     * 获取笔记本
     *
     * @param id          笔记本的Id
     * @param owner       所属者
     * @param withContent 是否包含内容
     * @return 返回符合条件的笔记本
     * @throws UserException 用户异常
     */
    Notebook getNoteBook(String id, String owner, boolean withContent) throws UserException;

    /**
     * 更新笔记本内容
     *
     * @param noteBook 指定的笔记本
     * @return 返回更新后的笔记本
     * @throws UserException 用户异常
     */
    Notebook updateNotebookContent(Notebook noteBook) throws UserException;

    /**
     * 列出该笔记本的所有历史版本
     *
     * @param id            笔记本的Id
     * @param pageSize      页数
     * @param lastVersionId 最新版本的Id
     * @return 返回笔记本所有的历史内容的列表
     */
    List<NoteBookContent> listNotebookHistoryVersion(String id, int pageSize, String lastVersionId);

    /**
     * 列出该笔记本的所有历史版本
     *
     * @param id 笔记本的id
     * @return 返回笔记本的所有历史版本Id
     */
    List<NoteBookContent> listNotebookHistoryVersionIds(String id);

    /**
     * 列出该笔记本的指定Id历史版本
     *
     * @param notebookId 笔记本的Id
     * @param ids        版本号Id列表
     * @return 返回所有符合条件的笔记本历史版本
     */
    List<NoteBookContent> listNotebookHistoryVersionByIds(String notebookId, List<String> ids);

    /**
     * 删除指定的笔记本
     *
     * @param id    笔记本的Id
     * @param owner 拥有者
     * @return 如果成功删除则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean deleteNotebook(String id, String owner) throws UserException;

    /**
     * 获取笔记本包含笔记
     *
     * @param id    分组的Id
     * @param owner 用户的Id
     * @return 返回分组下的所有内容
     * @throws UserException 用户异常
     */
    List<DataGroupItem> listChildren(String id, String owner) throws UserException;

    /**
     * 订阅源
     *
     * @param userId   用户的Id
     * @param sourceId 源Id
     * @param emitter  触发器
     * @throws UserException 用户异常
     */
    void subscribeDataSource(String userId, String sourceId, SseEmitter emitter) throws UserException;

    /**
     * 判断指定路径是否存在
     *
     * @param path 路径，以/分割的Id编码
     * @return 如果存在则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean existsPath(String path) throws UserException;

    /**
     * 复制一个旧的路径到新的路径
     *
     * @param sourceId 数据源
     * @param oldPath  旧的目录
     * @param newPath  新的目录
     * @return 如果成功则返回true，否则返回false
     */
    boolean copyDirectory(String sourceId, String oldPath, String newPath) throws UserException;

    /**
     * 移动一个旧的路径到新的路径
     *
     * @param sourceId 数据源
     * @param oldPath  旧的目录
     * @param newPath  新的目录
     * @return 如果成功则返回true，否则返回false
     */
    boolean moveDirectory(String sourceId, String oldPath, String newPath) throws UserException;

    /**
     * 转换GUID的到命名路径
     *
     * @param idsPath ids的路径
     * @return 返回命名路径
     */
    String idsPathToNamePath(String idsPath) throws UserException;

    /**
     * 上传图片格式文件
     * @param base64Image 前端传来的base字符串
     * @return
     */
    String uploadBase64Image(String base64Image);
}

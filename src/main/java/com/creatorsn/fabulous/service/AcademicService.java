package com.creatorsn.fabulous.service;

import com.creatorsn.fabulous.entity.*;
import com.creatorsn.fabulous.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author minskiter
 * @date 22/8/2023 21:30
 * @description
 */
@Service
public interface AcademicService {

    /**
     * 获取数据源下的所有数据分组
     *
     * @param sourceId 数据源的Id
     * @return 返回对应的数据分组
     */
    List<DataGroup> getDataGroupBySourceId(String sourceId) throws UserException;

    /**
     * 获取指定父亲节点下的数据
     *
     * @param parentId 父亲节点的Id
     * @return 返回对应的数据分组
     */
    List<DataGroup> getDataGroupByParentId(String parentId) throws UserException;

    /**
     * 创建一个数据分组
     *
     * @param dataGroup 数据分组
     * @return 返回对应的数据分组
     */
    DataGroup createGroup(DataGroup dataGroup) throws UserException;

    /**
     * 更新一个数据分组
     *
     * @param dataGroup 数据分组
     * @return 返回更新后的数据分组
     */
    DataGroup updateGroup(DataGroup dataGroup) throws UserException;

    /**
     * 删除指定的数据分组
     *
     * @param sourceId 数据源
     * @param groupId  分组的Id
     * @param userId   用户的Id
     * @return 如果删除成功则返回true，否则返回false
     */
    boolean deleteGroup(String sourceId, String groupId, String userId) throws UserException;

    /**
     * 获取数据源下的数据分区
     *
     * @param sourceId 数据源
     * @return 返回对应的分区信息
     */
    List<DataPartition> getPartitionsBySourceId(String sourceId) throws UserException;

    /**
     * 获取指定分组下的数据分区
     *
     * @param parentId 分区的Id
     * @return 返回对应的分区信息
     */
    List<DataPartition> getPartitionsByParentId(String parentId) throws UserException;

    /**
     * 获取指定数据分区的信息
     *
     * @param sourceId 数据源的Id
     * @param id       数据分区的Id
     * @return 返回指定的数据源
     * @throws UserException 用户异常
     */
    DataPartition getPartitionById(String sourceId, String id) throws UserException;

    /**
     * 创建分区
     *
     * @param partition 分区
     * @return 返回创建后的分区
     */
    DataPartition createPartition(DataPartition partition) throws UserException;

    /**
     * 更新分区
     *
     * @param partition 分区
     * @return 返回更新后的分区
     */
    DataPartition updatePartition(DataPartition partition) throws UserException;

    /**
     * 删除分区
     *
     * @param sourceId 源的Id
     * @param groupId  分组的Id
     * @param id       分区的Id
     * @return 如果删除成功
     */
    boolean deletePartition(String sourceId, String groupId, String id) throws UserException;

    /**
     * 创建数据项
     *
     * @param item 数据项
     * @return 返回创建的数据项
     */
    DataItem createItem(DataItem item) throws UserException;

    /**
     * 更新数据项
     *
     * @param item 数据项
     * @return 返回更新的数据项
     */
    DataItem updateItem(DataItem item) throws UserException;

    /**
     * 数据项的Id
     *
     * @param id 获取对应的数据项信息
     * @return 返回数据项
     */
    DataItem getItem(String id);

    /**
     * 删除数据项
     *
     * @param id 数据项的Id
     * @return 如果删除成功则返回ture，否则返回false
     */
    boolean deleteItem(String id);

    /**
     * 列出指定id的数据项列表
     *
     * @param ids 数据项的Id
     * @return 返回数据项的列表
     */
    List<DataItem> listItems(List<String> ids);

    /**
     * 获取指定分区下的数据项列表
     *
     * @param parentId 分区的Id
     * @param offset   偏移量
     * @param length   页数
     * @param sort     排序
     * @param desc     是否递减
     * @return 返回数据项列表
     */
    List<DataItem> listItemsByParent(String parentId, long offset, int length, String sort, boolean desc);

    /**
     * 获取指定分区下的数据项数量
     *
     * @param parentId 分区的id
     * @return 返回数据项的数量
     */
    long getItemsCountByParent(String parentId);

    /**
     * 列出指定数据源下的所有数据项
     *
     * @param sourceId 数据源
     * @param offset   偏移量
     * @param length   长度
     * @param sort     排序的关键字
     * @param desc     是否递减
     * @return 返回指定数据源下的所有数据项
     */
    List<DataItem> listItemsBySource(String sourceId, long offset, int length, String sort, boolean desc) throws UserException;

    /**
     * 获取指定数据源下所有数据项的数目
     *
     * @param sourceId 数据源
     * @return 返回指定数据源下的所有数据项的数目
     */
    long getItemsCountBySource(String sourceId) throws UserException;

    /**
     * 删除数据项的列表
     *
     * @param sourceId 数据源的Id
     * @param ids      数据项的id列表
     * @return 返回被删除的数据项Id列表
     */
    List<String> deleteItemList(String sourceId, List<String> ids);

    /**
     * 添加数据项到指定的分区
     *
     * @param id          数据项的Id
     * @param partitionId 分区的Id
     * @param operator    操作人
     * @return 如果成功返回true，否则返回false
     */
    boolean addToPartition(String id, String partitionId, String operator);

    /**
     * 批量添加数据项到指定的分区中
     *
     * @param ids         数据项的Id列表
     * @param partitionId 分区的Id
     * @param operator    操作人
     * @return 返回添加成功的数据项Id
     */
    List<String> addListToPartition(List<String> ids, String partitionId, String operator);

    /**
     * 从指定的分区中删除目标数据项
     *
     * @param id          数据项的Id
     * @param partitionId 分区的Id
     * @return 如果成功则返回true，否则返回false
     */
    boolean removeFromPartition(String id, String partitionId);

    /**
     * 从指定的分区中批量删除数据项
     *
     * @param ids         数据项的Id列表
     * @param partitionId 分区的Id
     * @return 返回被删除的数据项Id列表
     */
    List<String> removeListFromPartition(List<String> ids, String partitionId);

    /**
     * 从指定的分区中搜索匹配的
     *
     * @param sourceId 数据源的Id
     * @param parentId 分区的Id
     * @param keyword  关键字
     * @param offset   偏移量
     * @param length   长度
     * @param sort     排序关键字
     * @param desc     是否递减
     * @return 返回符合条件的数据项
     */
    List<DataItem> queryItems(String sourceId, String parentId, String keyword, long offset, int length, String sort, boolean desc);

    /**
     * 更新指定数据项的元信息
     *
     * @param sourceId 源的Id
     * @param itemId   数据项的信息
     * @param metadata 元数据
     * @return 返回数据项的元信息
     */
    ItemMetadata updateItemMetadata(String sourceId, String itemId, ItemMetadata metadata) throws UserException;

    /**
     * 创建数据项的笔记
     *
     * @param page 数据项笔记元信息
     * @return 返回数据项的笔记
     */
    DataPage createItemPage(String sourceId, DataPage page) throws UserException;

    /**
     * 更新数据项的笔记
     *
     * @param page 数据项的笔记的Id
     * @return 返回更新后的数据项笔记的Id
     */
    DataPage updateItemPage(String sourceId, DataPage page) throws UserException;

    /**
     * 删除指定的数据项的笔记
     *
     * @param sourceId 数据源的Id
     * @param pageId   数据项笔记的Id
     * @return 如果成功删除则返回true，否则返回false
     */
    boolean deleteItemPage(String sourceId, String pageId) throws UserException;

    /**
     * 列出指定数据项下的笔记
     *
     * @param sourceId 数据源
     * @param itemId   数据项的笔记
     * @return 返回指定数据项下的笔记
     */
    List<DataPage> listItemPagesByItemId(String sourceId, String itemId);

    /**
     * 复制一个数据项笔记
     *
     * @param sourceId     数据源的Id
     * @param sourcePageId 数据项的笔记本Id
     * @return 返回复制后新的数据项的笔记
     */
    DataPage duplicateItemPage(String sourceId, String sourcePageId) throws UserException;

    /**
     * 更新指定的笔记本的内容
     *
     * @param sourceId 数据源的Id
     * @param content  要更新的笔记本内容
     * @return 返回更新后的数据源
     */
    DataPageContent updateItemPageContent(String sourceId, DataPageContent content) throws UserException;

    /**
     * 获取指定笔记的最新内容
     *
     * @param sourceId 数据源的Id
     * @param pageId   笔记的Id
     * @return 返回笔记的最新内容，如果没有则返回null
     */
    DataPageContent getItemPageContent(String sourceId, String pageId);

    /**
     * 获取指定版本的内容
     *
     * @param sourceId  数据源的Id
     * @param pageId    笔记的Id
     * @param versionId 版本的Id
     * @return 返回笔记指定版本的内容，如果没有则返回null
     */
    DataPageContent getItemPageContentByVersionId(String sourceId, String pageId, String versionId) throws UserException;

    /**
     * 获取指定笔记的历史版本内容
     *
     * @param sourceId 数据源
     * @param pageId   笔记的Id
     * @return 返回笔记的历史版本
     */
    List<DataPageContent> getItemPageContentVersions(String sourceId, String pageId) throws UserException;

    /**
     * 更新数据项的Pdf
     *
     * @param sourceId 数据源的Id
     * @param itemId   数据项的Id
     * @param pdfId    Pdf文件的Id
     * @param file     文件
     * @return 返回更新数据项的Pdf的Id
     */
    String updateItemPDF(String sourceId, String itemId, String pdfId, MultipartFile file) throws UserException;

    /**
     * 获取数据项的Pdf
     *
     * @param sourceId 数据源的Id
     * @param itemId   数据项的Id
     * @param pdfId    Pdf的Id
     * @return 返回Pdf文件
     */
    MultipartFile getItemPDF(String sourceId, String itemId, String pdfId) throws UserException;


    /**
     * 获取数据源下的模版
     *
     * @param sourceId 数据源
     * @return 返回对应的模版列表
     */
    List<DataTemplate> getTemplates(String sourceId) throws UserException;


    /**
     * 获取最新的模版内容
     *
     * @param sourceId   数据源
     * @param templateId 模版的Id
     * @return 返回最新的模版内容
     */
    DataTemplateContent getLatestTemplateContent(String sourceId, String templateId) throws UserException;

    /**
     * 创建模版
     *
     * @param template 模版
     * @return 返回创建后的模版
     */
    DataTemplate createTemplate(DataTemplate template) throws UserException;

    /**
     * 更新模版
     *
     * @param template 模版
     * @return 返回更新后的模版
     */
    DataTemplate updateTemplate(DataTemplate template) throws UserException;

    /**
     * 删除对应的模版
     *
     * @param sourceId   数据源的Id
     * @param templateId 模版的Id
     * @return 如果成功删除则返回true，否则返回false
     */
    boolean deleteTemplate(String sourceId, String templateId) throws UserException;

    /**
     * 更新模版的内容
     *
     * @param sourceId   数据源的Id
     * @param templateId 模版的Id
     * @param versionId  版本的Id
     * @param userId     用户的Id
     * @param content    内容
     * @return 返回更新的模版的内容
     */
    DataTemplateContent updateTemplateContent(String sourceId, String templateId, String versionId, String userId, String content) throws UserException;
}

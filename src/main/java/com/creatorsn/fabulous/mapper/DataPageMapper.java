package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.DataPage;
import com.creatorsn.fabulous.entity.DataPageContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author minskiter
 * @date 30/8/2023 08:20
 * @description 笔记本描述
 */
@Mapper
public interface DataPageMapper {

    String table = "QDataPage";

    String contentTable = "QDataPageContent";

    /**
     * 创建数据项笔记
     *
     * @param page 笔记
     * @return 返回创建后的数据项笔记
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "create")
    DataPage create(DataPage page);

    /**
     * 更新数据项笔记
     *
     * @param page 数据项笔记
     * @return 返回更新后的数据项笔记
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "update")
    DataPage update(DataPage page);

    /**
     * 获取数据项笔记
     *
     * @param id 数据项笔记的Id
     * @return 返回获取的数据项笔记
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "getById")
    DataPage getById(String id);

    /**
     * 列出所有符合要求的数据项
     *
     * @param ids 数据项的id列表
     * @return 返回所有符合要求的数据项列表
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "list")
    List<DataPage> list(List<String> ids);

    /**
     * 列出所有符合要求的数据项列表
     *
     * @param parent 数据项的Id
     * @return 返回符合要求的数据项列表
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "listByParent")
    List<DataPage> listByParent(String parent);

    /**
     * 删除指定的数据项笔记
     *
     * @param id 数据项笔记的Id
     * @return 如果删除成功则返回true，否则返回false
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "delete")
    boolean delete(String id);

    /**
     * 更新笔记本的内容
     *
     * @param versionId 版本的Id
     * @param pageId    笔记本的Id
     * @param userId    用户的Id
     * @param content   笔记本的内容
     * @return 返回最新的笔记本的内容
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "updateContent")
    DataPageContent updateContent(String versionId, String pageId, String userId, String content);

    /**
     * 获取最新的内容
     *
     * @param pageId 笔记本的Id
     * @return 返回最新的内容
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "getLatestContent")
    DataPageContent getLatestContent(String pageId);

    /**
     * 获取笔记本的内容历史版本
     *
     * @param pageId 笔记本的Id
     * @return 返回笔记本下的历史内容列表
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "listContentIds")
    List<DataPageContent> listContentIds(String pageId);

    /**
     * 获取指定版本的笔记本内容
     *
     * @param pageId    笔记本的Id
     * @param versionId 版本的Id
     * @return 返回指定版本的笔记本内容
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "getContentById")
    DataPageContent getContentById(String pageId, String versionId);

    /**
     * 列出所有的笔记本历史内容
     *
     * @param pageId 笔记本的Id
     * @return 返回符合条件的笔记本内容列表
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "listContent")
    List<DataPageContent> listContent(String pageId);

    /**
     * 列出所有的父节点下的笔记本
     *
     * @param parents 父节点
     * @return 返回所有的笔记本
     */
    @SelectProvider(type = DataPageMapperProvider.class, method = "listByParents")
    List<DataPage> listByParents(List<String> parents);

    class DataPageMapperProvider {

        public String create(DataPage page) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            if (StringUtils.hasText(page.getName()))
                sql.VALUES("name", "#{name}");
            if (StringUtils.hasText(page.getEmoji())) {
                sql.VALUES("emoji", "#{emoji}");
            }
            sql.VALUES("owner", "#{owner}");
            sql.VALUES("parent", "#{parent}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            return sql.toString();
        }

        public String update(DataPage page) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            if (StringUtils.hasText(page.getName())) {
                sql.SET("name=#{name}");
            }
            if (StringUtils.hasText(page.getEmoji())) {
                sql.SET("emoji=#{emoji}");
            }
            if (StringUtils.hasText(page.getParent())) {
                sql.SET("parent=#{parent}");
            }
            sql.SET("updateDate=getdate()");
            return sql.toString();
        }

        public String getById(String id) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String list(List<String> ids) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("id in ('" + String.join("','", ids) + "')");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listByParent(String parent) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("parent=#{parent}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listByParents(List<String> parents) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("parent in ('" + String.join("','", parents) + "')");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String delete(String id) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.EXISTS();
            sql.SET("deleteDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String updateContent(String versionId, String pageId, String userId, String content) {
            var sql = new MSSQL();
            sql.INSERT_INTO(contentTable);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("versionId", "#{versionId}");
            sql.VALUES("pageId", "#{pageId}");
            sql.VALUES("author", "#{userId}");
            sql.VALUES("[content]", "#{content}");
            sql.VALUES("createDate", "getdate()");
            return sql.toString();
        }

        public String getLatestContent(String pageId) {
            var sql = new MSSQL();
            sql.SELECT("top 1 *");
            sql.FROM(contentTable);
            sql.WHERE("pageId=#{pageId}");
            sql.ORDER_BY("createDate desc");
            return sql.toString();
        }

        public String listContentIds(String pageId) {
            var sql = new MSSQL();
            sql.SELECT("versionId,author,pageId,createDate");
            sql.FROM(contentTable);
            sql.WHERE("pageId=#{pageId}");
            sql.ORDER_BY("createDate desc");
            return sql.toString();
        }

        public String getContentById(String pageId, String versionId) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(contentTable);
            sql.WHERE("pageId=#{pageId}");
            sql.WHERE("versionId=#{versionId}");
            return sql.toString();
        }

        public String listContent(String pageId) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(contentTable);
            sql.WHERE("pageId=#{pageId}");
            sql.ORDER_BY("createDate desc");
            return sql.toString();
        }
    }

}

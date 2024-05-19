package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.NoteBookContent;
import com.creatorsn.fabulous.entity.Notebook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 笔记本DAO
 */
@Mapper
public interface NoteBookMapper {

    String table = "QNotebook";
    String contentTable = "QNotebookContent";

    @SelectProvider(type = NoteBookMapperProvider.class, method = "create")
    Notebook create(Notebook noteBook);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "delete")
    boolean delete(String id);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "update")
    Notebook update(Notebook noteBook);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "list")
    List<Notebook> list(List<String> ids);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "listByParent")
    List<Notebook> listByParent(String parent);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "getContent")
    NoteBookContent getContent(String id);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "updateContent")
    NoteBookContent updateContent(NoteBookContent content);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "getContentLatestVersionId")
    String getContentLatestVersionId(String id);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "listContentVersions")
    List<NoteBookContent> listContentVersions(String id, int pageSize, String lastContentVersion);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "listContentVersionWithoutContent")
    List<NoteBookContent> listContentVersionWithoutContent(String id);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "listContentVersionByIds")
    List<NoteBookContent> listContentVersionByIds(String notebookId, List<String> ids);

    @SelectProvider(type = NoteBookMapperProvider.class, method = "getById")
    Notebook getById(String id);

    class NoteBookMapperProvider {

        public String create(Notebook noteBook) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            if (StringUtils.hasText(noteBook.getDescription()))
                sql.VALUES("description", "#{description}");
            if (StringUtils.hasText(noteBook.getBanner())) {
                sql.VALUES("banner", "#{banner}");
            }
            if (StringUtils.hasText(noteBook.getParent())) {
                sql.VALUES("parent", "#{parent}");
            }
            sql.VALUES("owner", "#{owner}");
            sql.VALUES("updateDate", "getdate()");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("sourceId", "#{sourceId}");
            return sql.toString();
        }

        public String exists(String id) {
            var sql = new MSSQL();
            sql.SELECT("count(*) as result");
            sql.FROM(table);
            sql.WHERE("id=#{id}");
            return sql.toString();
        }

        public String delete(String id) {
            var sql = new MSSQL();
            sql.EXISTS();
            sql.UPDATE(table);
            sql.SET("deleteDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String update(Notebook noteBook) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (StringUtils.hasText(noteBook.getName()))
                sql.SET("name=#{name}");
            if (StringUtils.hasText(noteBook.getBanner()))
                sql.SET("banner=#{banner}");
            if (StringUtils.hasText(noteBook.getDescription())) {
                sql.SET("description=#{description}");
            }
            if (StringUtils.hasText(noteBook.getParent())) {
                sql.SET("parent=#{parent}");
            }
            sql.SET("updateDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getById(String id) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("*");
            sql.WHERE("deleteDate is null");
            sql.WHERE("id=#{id}");
            return sql.toString();
        }

        public String list(List<String> ids) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("*");
            sql.WHERE("id in ('" + String.join("','", ids) + "')");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listByParent(String parent) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("*");
            sql.WHERE("parent=#{parent}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getContent(String id) {
            var sql = new MSSQL();
            sql.FROM(contentTable);
            sql.SELECT("top 1 *");
            sql.WHERE("notebookId=#{id}");
            sql.ORDER_BY("CONVERT(NVARCHAR(36),versionId) desc");
            return sql.toString();
        }

        public String updateContent(NoteBookContent content) {
            var sql = new MSSQL();
            sql.INSERT_INTO(contentTable);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("versionId", "#{versionId}");
            sql.VALUES("author", "#{author}");
            sql.VALUES("content", "#{content}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("notebookId", "#{notebookId}");
            return sql.toString();
        }

        public String getContentLatestVersionId(String id) {
            var sql = new MSSQL();
            sql.FROM(contentTable);
            sql.SELECT("top 1 versionId");
            sql.WHERE("notebookId=#{id}");
            sql.ORDER_BY("versionId desc");
            return sql.toString();
        }

        public String listContentVersions(String id, int pageSize, String lastContentVersion) {
            var sql = new MSSQL();
            sql.FROM(contentTable);
            sql.SELECT("*");
            sql.WHERE("notebookId=#{id}");
            if (StringUtils.hasText(lastContentVersion))
                sql.WHERE("CONVERT(NVARCHAR(36),versionId)<CONVERT(NVARCHAR(36),#{lastContentVersion})");
            sql.OFFSET(0L);
            sql.FETCH_FIRST_ROWS_ONLY(pageSize);
            sql.ORDER_BY("CONVERT(NVARCHAR(36),versionId) desc");
            return sql.toString();
        }

        public String listContentVersionWithoutContent(String id) {
            var sql = new MSSQL();
            sql.FROM(contentTable);
            sql.SELECT("versionId,author,createDate,notebookId");
            sql.WHERE("notebookId=#{id}");
            sql.ORDER_BY("CONVERT(NVARCHAR(36),versionId) desc");
            return sql.toString();
        }

        public String listContentVersionByIds(String notebookId, List<String> ids) {
            var sql = new MSSQL();
            sql.FROM(contentTable);
            sql.SELECT("*");
            sql.WHERE("notebookId=#{notebookId}");
            sql.WHERE("versionId in ('" + String.join("','", ids) + "')");
            sql.ORDER_BY("CONVERT(NVARCHAR(36),versionId) desc");
            return sql.toString();
        }

    }


}

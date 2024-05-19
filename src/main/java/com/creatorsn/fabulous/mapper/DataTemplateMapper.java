package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.DataTemplate;
import com.creatorsn.fabulous.entity.DataTemplateContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author minskiter
 * @date 31/8/2023 14:23
 * @description
 */
@Mapper
public interface DataTemplateMapper {

    String table = "QDataTemplate";

    String contentTable = "QDataTemplateContent";

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "create")
    DataTemplate create(DataTemplate dataTemplate);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "update")
    DataTemplate update(DataTemplate dataTemplate);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "delete")
    boolean delete(String id);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "getById")
    DataTemplate getById(String id);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "list")
    List<DataTemplate> list(String parent);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "getLatestContent")
    DataTemplateContent getLatestContent(String id);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "getContentHistory")
    List<DataTemplateContent> getContentHistory(String id);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "updateContent")
    DataTemplateContent updateContent(String versionId, String templateId, String userId, String content);

    @SelectProvider(type = DataTemplateMapperProvider.class, method = "listLatestContent")
    List<DataTemplateContent> listLatestContent(List<String> ids);

    class DataTemplateMapperProvider {

        public String create(DataTemplate dataTemplate) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            if (StringUtils.hasText(dataTemplate.getEmoji()))
                sql.VALUES("emoji", "#{emoji}");
            sql.VALUES("owner", "#{owner}");
            sql.VALUES("parent", "#{parent}");
            sql.VALUES("updateDate", "getdate()");
            sql.VALUES("createDate", "getdate()");
            sql.OUTPUT_INSERT("*");
            return sql.toString();
        }

        public String update(DataTemplate dataTemplate) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            if (StringUtils.hasText(dataTemplate.getName()))
                sql.SET("name=#{name}");
            if (StringUtils.hasText(dataTemplate.getEmoji())) {
                sql.SET("emoji=#{emoji}");
            }
            if (StringUtils.hasText(dataTemplate.getParent())) {
                sql.SET("parent=#{parent}");
            }
            if (StringUtils.hasText(dataTemplate.getOwner())) {
                sql.SET("owner=#{owner}");
            }
            sql.SET("updateDate=getdate()");
            sql.OUTPUT_INSERT("*");
            sql.WHERE("id=#{id}");
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

        public String getById(String id) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String list(String parent) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("parent=#{parent}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getLatestContent(String id) {
            var sql = new MSSQL();
            sql.SELECT("top 1 *");
            sql.FROM(contentTable);
            sql.WHERE("templateId=#{id}");
            sql.ORDER_BY("createDate desc");
            return sql.toString();
        }

        public String getContentHistory(String id) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(contentTable);
            sql.WHERE("templateId=#{id}");
            return sql.toString();
        }

        public String listLatestContent(List<String> ids) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(contentTable);
            sql.INNER_JOIN("(select templateId,max(createDate) as max_create_date from "
                    + contentTable + " group by templateId) as t2 on " + contentTable + ".templateId=t2.templateId and "
                    + contentTable + ".createDate=t2.max_create_date");
            sql.WHERE(contentTable + ".templateId in ('" + String.join("','", ids) + "')");
            return sql.toString();
        }

        public String updateContent(String versionId, String templateId, String userId, String content) {
            var sql = new MSSQL();
            sql.INSERT_INTO(contentTable);
            sql.VALUES("versionId", "#{versionId}");
            sql.VALUES("templateId", "#{templateId}");
            sql.VALUES("author", "#{userId}");
            sql.VALUES("content", "#{content}");
            sql.VALUES("createDate", "getdate()");
            sql.OUTPUT_INSERT("*");
            return sql.toString();
        }
    }


}

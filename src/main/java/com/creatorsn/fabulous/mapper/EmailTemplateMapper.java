package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.EmailTemplate;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface EmailTemplateMapper {

    static final String table = "QEmailTemplate";

    public class EmailTemplateMapperProvider {

        public String add(EmailTemplate emailTemplate) {
            MSSQL sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            sql.VALUES("creator", "#{creator}");
            sql.VALUES("subject", "#{subject}");
            sql.VALUES("content", "#{content}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            sql.VALUES("variables", "#{variables}");
            return sql.toString();
        }

        public String update(EmailTemplate emailTemplate) {
            MSSQL sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (emailTemplate.getName() != null)
                sql.SET("name=#{name}");
            if (emailTemplate.getSubject() != null)
                sql.SET("subject=#{subject}");
            if (emailTemplate.getContent() != null)
                sql.SET("content=#{content}");
            if (emailTemplate.getVariables() != null)
                sql.SET("variables=#{variables}");
            sql.SET("updateDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String delete(String id) {
            MSSQL sql = new MSSQL();
            sql.UPDATE(table);
            sql.SET("deleteDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String existsName(String name) {
            MSSQL sql = new MSSQL();
            sql.SELECT("count(*)");
            sql.FROM(table);
            sql.WHERE("name=#{name}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getByName(String name) {
            MSSQL sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("name=#{name}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String list(OffsetDateTime before, long offset, int pageSize) {
            MSSQL sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            if (before != null)
                sql.WHERE("createDate<#{before}");
            sql.WHERE("deleteDate is null");
            sql.OFFSET(offset);
            sql.FETCH_FIRST_ROWS_ONLY(pageSize);
            sql.ORDER_BY("createDate desc");
            return sql.toString();
        }
    }

    @SelectProvider(type = EmailTemplateMapperProvider.class, method = "add")
    EmailTemplate add(EmailTemplate emailTemplate);

    @SelectProvider(type = EmailTemplateMapperProvider.class, method = "update")
    EmailTemplate update(EmailTemplate emailTemplate);

    @DeleteProvider(type = EmailTemplateMapperProvider.class, method = "delete")
    EmailTemplate delete(String id);

    @SelectProvider(type = EmailTemplateMapperProvider.class, method = "list")
    List<EmailTemplate> list(OffsetDateTime before, long offset, int pageSize);

    @SelectProvider(type = EmailTemplateMapperProvider.class, method = "getByName")
    EmailTemplate getByName(String name);

    @SelectProvider(type = EmailTemplateMapperProvider.class, method = "existsName")
    boolean existsName(String name);


}

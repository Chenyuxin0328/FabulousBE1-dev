package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.EmailMessage;
import com.creatorsn.fabulous.entity.EmailUsed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * EmailMessage的Mapper
 */
@Mapper
public interface EmailMessageMapper {

    static final String table = "QEmailMessage";

    class EmailMessageMapperProvider {

        final Logger logger = LoggerFactory.getLogger(EmailMessageMapperProvider.class);

        public String add(EmailMessage emailMessage) {
            MSSQL sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("emailId", "#{emailId}");
            sql.VALUES("[to]", "#{to}");
            sql.VALUES("subject", "#{subject}");
            sql.VALUES("content", "#{content}");
            if (emailMessage.getAttachments() != null) {
                sql.VALUES("attachments", "#{attachments}");
            }
            sql.VALUES("status", "#{status}");
            sql.VALUES("err", "#{err}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
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

        public String updateStatus(String id, EmailMessage.EmailStatus status, String err) {
            MSSQL sql = new MSSQL();
            sql.UPDATE(table);
            sql.SET("status=#{status}");
            if (err != null)
                sql.SET("err=#{err}");
            sql.SET("updateDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getEmailMessages(String query, long offset, int pageSize, String lastKey) {
            MSSQL sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("deleteDate is null");
            if (query != null) {
                sql.WHERE("subject like '%'+#{query}+'%'");
            }
            if (lastKey != null) {
                sql.WHERE("id>#{lastKey}");
            }
            sql.ORDER_BY("id");
            sql.OFFSET(offset);
            sql.LIMIT(pageSize);
            return sql.toString();
        }

        public String getEmailUsedCounter(OffsetDateTime start, OffsetDateTime end) {
            MSSQL sql = new MSSQL();
            sql.SELECT(MessageFormat.format("isnull(count({0}.emailId),0) as count, {1}.id", table, EmailMapper.table));
            sql.FROM(EmailMapper.table);
            sql.LEFT_OUTER_JOIN(MessageFormat.format("{0} on {1}.id={0}.emailId", table, EmailMapper.table));
//            使用单引号可以防止{}被替换，使用#防止被sql注入，注意这里不能直接使用字符串注入
//            sql.WHERE(MessageFormat.format("{0}.createDate >= #'{start}'", table));
//            sql.WHERE(MessageFormat.format("{0}.createDate <= #'{end}'",table));
//            sql.WHERE(MessageFormat.format("{0}.deleteDate is null",table));
            sql.WHERE(MessageFormat.format("{0}.deleteDate is null", EmailMapper.table));
            sql.GROUP_BY(MessageFormat.format("{0}.id", EmailMapper.table));
            sql.ORDER_BY("count asc");
            return sql.toString();
        }
    }

    /**
     * 添加EmailMessage
     *
     * @param emailMessage EmailMessage
     * @return EmailMessage
     */
    @SelectProvider(type = EmailMessageMapperProvider.class, method = "add")
    EmailMessage add(EmailMessage emailMessage);

    /**
     * 删除EmailMessage
     *
     * @param id EmailMessage的id
     * @return 是否成功
     */
    @UpdateProvider(type = EmailMessageMapperProvider.class, method = "delete")
    boolean delete(String id);

    /**
     * 更新EmailMessage
     *
     * @param id     EmailMessage的id
     * @param status EmailMessage的状态
     * @param err    EmailMessage的错误信息
     * @return 是否成功
     */
    @UpdateProvider(type = EmailMessageMapperProvider.class, method = "updateStatus")
    boolean updateStatus(String id, EmailMessage.EmailStatus status, String err);

    /**
     * 获取EmailMessage
     *
     * @param query    查询条件
     * @param offset   偏移量
     * @param pageSize 页大小
     * @param lastKey  上一页最后一条记录的id
     * @return EmailMessage列表
     */
    @SelectProvider(type = EmailMessageMapperProvider.class, method = "getEmailMessages")
    List<EmailMessage> getEmailMessages(String query, long offset, int pageSize, String lastKey);

    /**
     * 获取某个时间段的所有邮箱使用计数
     *
     * @param start 开始的时间
     * @param end   结束的时间
     * @return 返回邮箱使用计数列表
     */
    @SelectProvider(type = EmailMessageMapperProvider.class, method = "getEmailUsedCounter")
    List<EmailUsed> getEmailUsedCounter(OffsetDateTime start, OffsetDateTime end);
}

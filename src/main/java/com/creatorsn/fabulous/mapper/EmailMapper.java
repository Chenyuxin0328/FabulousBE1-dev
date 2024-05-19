package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 邮箱DAO层
 */
@Mapper
public interface EmailMapper {

    static final String table = "QEmail";

    class EmailMapperProvider {

        public String add(Email email) {
            MSSQL sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("username", "#{username}");
            sql.VALUES("password", "#{password}");
            if (StringUtils.hasText(email.getSmtphost()))
                sql.VALUES("smtphost", "#{smtphost}");
            if (StringUtils.hasText(email.getImaphost()))
                sql.VALUES("imaphost", "#{imaphost}");
            if (email.getSmtpport() != null)
                sql.VALUES("smtpport", "#{smtpport}");
            if (email.getImapport() != null)
                sql.VALUES("imapport", "#{imapport}");
            sql.VALUES("smtpssl", "#{smtpssl}");
            sql.VALUES("imapssl", "#{imapssl}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            if (email.getModifier() != null) {
                sql.VALUES("modifier", "#{modifier}");
            }
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

        public String exists(String username) {
            MSSQL sql = new MSSQL();
            sql.SELECT("count(*)");
            sql.FROM(table);
            sql.WHERE("username=#{username}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String update(Email email) {
            MSSQL sql = new MSSQL();
            sql.UPDATE(table);
            if (StringUtils.hasText(email.getUsername()))
                sql.SET("username=#{username}");
            if (StringUtils.hasText(email.getPassword()))
                sql.SET("password=#{password}");
            if (StringUtils.hasText(email.getSmtphost()))
                sql.SET("smtphost=#{smtphost}");
            if (StringUtils.hasText(email.getImaphost()))
                sql.SET("imaphost=#{imaphost}");
            if (email.getSmtpport() != null)
                sql.SET("smtpport=#{smtpport}");
            if (email.getImapport() != null)
                sql.SET("imapport=#{imapport}");
            if (email.isSmtpssl() != null)
                sql.SET("smtpssl=#{smtpssl}");
            if (email.isImapssl() != null)
                sql.SET("imapssl=#{imapssl}");
            if (email.getModifier() != null)
                sql.SET("modifier=#{modifier}");
            sql.SET("updateDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getEmail(String id) {
            MSSQL sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("*");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getEmails(String query, long offset, int pageSize, String lastKey) {
            offset = Math.max(offset, 0);
            pageSize = Math.min(pageSize, 10000);
            MSSQL sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("id", "username", "smtphost", "imaphost", "smtpport", "imapport", "smtpssl", "imapssl", "createDate", "updateDate", "modifier");
            if (StringUtils.hasText(query)) {
                sql.WHERE("username like '%'+#{query}+'%'");
            }
            if (StringUtils.hasText(lastKey)) {
                sql.WHERE("id > #{lastKey}");
            }
            sql.WHERE("deleteDate is null");
            sql.ORDER_BY("id");
            sql.OFFSET(offset);
            sql.FETCH_FIRST_ROWS_ONLY(pageSize);
            return sql.toString();
        }
    }

    /**
     * 添加系统邮箱账户
     *
     * @param email 系统的邮箱
     * @return 返回被添加的一个系统邮箱
     */
    @SelectProvider(type = EmailMapperProvider.class, method = "add")
    Email add(Email email);

    /**
     * 删除系统邮箱账户
     *
     * @param id 系统邮箱账户的id
     * @return 返回是否删除成功
     */
    @UpdateProvider(type = EmailMapperProvider.class, method = "delete")
    boolean delete(String id);

    /**
     * 判断某个账户是否已经存在
     *
     * @param username 邮箱的用户名
     * @return 如果存在返回true, 否则返回false
     */
    @SelectProvider(type = EmailMapperProvider.class, method = "exists")
    boolean exists(String username);

    /**
     * 更新系统邮箱账户
     *
     * @param email 要修改的邮箱信息
     * @return 如果修改成功返回true, 否则返回false
     */
    @UpdateProvider(type = EmailMapperProvider.class, method = "update")
    boolean update(Email email);

    /**
     * 获取系统邮箱账户
     *
     * @param id 系统邮箱账户的id
     * @return 返回系统邮箱账户
     */
    @SelectProvider(type = EmailMapperProvider.class, method = "getEmail")
    Email getEmail(String id);

    /**
     * 获取系统邮箱的账户
     *
     * @param query    查询的用户名
     * @param offset   偏移量
     * @param lastKey  上一页的最后一个id
     * @param pageSize 每页的页数
     * @return 返回系统邮箱的账户
     */
    @SelectProvider(type = EmailMapperProvider.class, method = "getEmails")
    List<Email> getEmails(String query, long offset, int pageSize, String lastKey);


}

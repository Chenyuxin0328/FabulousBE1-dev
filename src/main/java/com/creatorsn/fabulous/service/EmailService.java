package com.creatorsn.fabulous.service;

import com.creatorsn.fabulous.entity.Email;
import com.creatorsn.fabulous.entity.EmailTemplate;
import com.creatorsn.fabulous.entity.EmailUsedCounter;
import com.creatorsn.fabulous.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * 邮箱提供类
 */
@Service
public interface EmailService {

    /**
     * 添加系统邮箱
     *
     * @param email 系统邮箱
     * @return 返回添加的邮箱
     */
    Email addEmail(Email email);

    /**
     * 添加系统邮件模版
     *
     * @param template 邮件模版
     * @return 返回添加的邮件模版
     */
    EmailTemplate addTemplate(EmailTemplate template) throws UserException;

    /**
     * 删除系统邮箱
     *
     * @param id 系统邮箱的Id
     * @return 如果删除成功则返回true，否则返回false
     */
    boolean deleteEmailById(String id);

    /**
     * 更新系统邮箱的信息
     *
     * @param id    系统邮箱的Id
     * @param email 邮箱的属性
     * @return 如果更新成功则返回true，否则返回false
     */
    boolean updateEmailById(String id, Email email);

    /**
     * 更新系统邮件模版的信息
     *
     * @param template 邮件模版的属性
     * @return 返回修改后的邮件模版
     */
    EmailTemplate updateTemplate(EmailTemplate template) throws UserException;

    /**
     * 获取邮件模版的列表
     *
     * @param before   获取在此时间之前的邮箱
     * @param offset   偏移量
     * @param pageSize 每页的大小
     * @return 返回获取的邮箱模版列表
     */
    List<EmailTemplate> getTemplates(OffsetDateTime before, long offset, int pageSize);

    /**
     * 根据查询获取邮箱的列表
     *
     * @param query    要查询的主题
     * @param offset   偏移量
     * @param pageSize 每页的大小
     * @param lastKey  最后的主题
     * @return 返回查询的邮箱列表
     */
    List<Email> getEmails(String query, long offset, int pageSize, String lastKey);

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id      邮箱的Id
     * @param to      接收方
     * @param subject 主题
     * @param content 内容
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, String to, String subject, String content);

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id           邮箱的Id
     * @param to           接收方
     * @param templateName 模版的名称
     * @param variables    模版的变量
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, String to, String templateName, HashMap<String, String> variables) throws UserException;

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id           邮箱的Id
     * @param to           接收方
     * @param templateName 模版的名称
     * @param variables    模版的变量
     * @param files        附件
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, String to, String templateName, HashMap<String, String> variables, List<MultipartFile> files) throws UserException;

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id      邮箱的Id
     * @param to      接收方
     * @param subject 主题
     * @param content 内容
     * @param files   附件
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, String to, String subject, String content, List<MultipartFile> files);

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id      邮箱的Id
     * @param to      接收方
     * @param subject 主题
     * @param content 内容
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, List<String> to, String subject, String content);

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id           邮箱的Id
     * @param to           接收方
     * @param templateName 模版的名称
     * @param variables    模版的变量
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, List<String> to, String templateName, HashMap<String, String> variables) throws UserException;

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id      邮箱的Id
     * @param to      接收方
     * @param subject 主题
     * @param content 内容
     * @param files   附件
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, List<String> to, String subject, String content, List<MultipartFile> files);

    /**
     * 使用指定的邮箱发送邮件
     *
     * @param id           邮箱的Id
     * @param to           接收方
     * @param templateName 模版的名称
     * @param variables    模版的变量
     * @param files        附件
     * @return 如果发送成功返回true，否则返回false
     */
    boolean sendEmail(String id, List<String> to, String templateName, HashMap<String, String> variables, List<MultipartFile> files) throws UserException;

    /**
     * 获取指定时间的邮箱使用计数
     *
     * @param from 开始的时间
     * @param to   结束的时间
     * @return 返回该段时间所有邮箱的使用情况
     */
    EmailUsedCounter getEmailUsedCounter(OffsetDateTime from, OffsetDateTime to);
}

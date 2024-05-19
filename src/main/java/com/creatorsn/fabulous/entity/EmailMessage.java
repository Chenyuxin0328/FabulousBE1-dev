package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * Email的讯息
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailMessage {

    /**
     * 讯息的Id
     */
    @JsonProperty
    private String id;
    /**
     * 使用的发件邮箱
     */
    @JsonProperty
    private String emailId;
    /**
     * 主题
     */
    @JsonProperty
    private String subject;
    /**
     * 接收方
     */
    @JsonProperty
    private String to;
    /**
     * 发送的内容
     */
    @JsonProperty
    private String content;
    /**
     * 附件的文件名，“、”分割
     */
    @JsonProperty
    private String attachments;
    /**
     * 发送的状态
     */
    @JsonProperty
    private EmailStatus status;
    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;
    /**
     * 如果发送失败，记录的错误信息
     */
    @JsonProperty
    private String err;
    /**
     * 更新的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    public String getId() {
        return id;
    }

    public EmailMessage setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmailId() {
        return emailId;
    }

    public EmailMessage setEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EmailMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getTo() {
        return to;
    }

    public EmailMessage setTo(String to) {
        this.to = to;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EmailMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAttachments() {
        return attachments;
    }

    public EmailMessage setAttachments(String attachments) {
        this.attachments = attachments;
        return this;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public EmailMessage setStatus(EmailStatus status) {
        this.status = status;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public EmailMessage setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getErr() {
        return err;
    }

    public EmailMessage setErr(String err) {
        this.err = err;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public EmailMessage setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public enum EmailStatus {
        SENT,
        PENDING,
        FAILED,
    }
}

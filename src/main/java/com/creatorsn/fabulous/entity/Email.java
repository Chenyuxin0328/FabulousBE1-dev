package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 网站的邮箱类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email {

    /**
     * 邮箱的唯一标识
     */
    @JsonProperty
    private String id;
    /**
     * 用户名，通常是邮箱的地址
     */
    @JsonProperty
    private String username;
    /**
     * 邮箱的密码
     */
    @JsonProperty
    private String password;
    /**
     * 邮箱的smtp地址，例如smtp.qq.com
     */
    @JsonProperty
    private String smtphost;
    /**
     * SMTP端口，例如默认的465
     */
    @JsonProperty
    private Integer smtpport;
    /**
     * imap地址，例如imap.qq.com
     */
    @JsonProperty
    private String imaphost;
    /**
     * imap端口，例如默认的993
     */
    @JsonProperty
    private Integer imapport;
    /**
     * 是否启用smtp的ssl加密
     */
    @JsonProperty
    private Boolean smtpssl;

    /**
     * 是否启用imap的ssl加密
     */
    @JsonProperty
    private Boolean imapssl;
    /**
     * 修改的用户id
     */
    @JsonProperty
    private String modifier;
    /**
     * 更新的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;
    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    public String getId() {
        return id;
    }

    public Email setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Email setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Email setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSmtphost() {
        return smtphost;
    }

    public Email setSmtphost(String smtphost) {
        this.smtphost = smtphost;
        return this;
    }

    public Integer getSmtpport() {
        return smtpport;
    }

    public Email setSmtpport(int smtpport) {
        this.smtpport = smtpport;
        return this;
    }

    public Email setSmtpport(Integer smtpport) {
        this.smtpport = smtpport;
        return this;
    }

    public String getImaphost() {
        return imaphost;
    }

    public Email setImaphost(String imaphost) {
        this.imaphost = imaphost;
        return this;
    }

    public Integer getImapport() {
        return imapport;
    }

    public Email setImapport(int imapport) {
        this.imapport = imapport;
        return this;
    }

    public Email setImapport(Integer imapport) {
        this.imapport = imapport;
        return this;
    }

    public Boolean isSmtpssl() {
        return smtpssl;
    }

    public Email setSmtpssl(boolean smtpssl) {
        this.smtpssl = smtpssl;
        return this;
    }

    public String getModifier() {
        return modifier;
    }

    public Email setModifier(String modifier) {
        this.modifier = modifier;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public Email setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public Email setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public Boolean isImapssl() {
        return imapssl;
    }

    public Email setImapssl(boolean imapssl) {
        this.imapssl = imapssl;
        return this;
    }
}

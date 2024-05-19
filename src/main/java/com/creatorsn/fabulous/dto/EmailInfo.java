package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailInfo {

    /**
     * 邮箱的唯一标识
     */
    @JsonProperty("id")
    private String id;
    /**
     * 用户名，通常是邮箱的地址
     */
    @JsonProperty("username")
    private String username;
    /**
     * 邮箱的smtp地址，例如smtp.qq.com
     */
    @JsonProperty("smtphost")
    private String smtphost;
    /**
     * SMTP端口，例如默认的465
     */
    @JsonProperty("smtpport")
    private Integer smtpport;
    /**
     * imap地址，例如imap.qq.com
     */
    @JsonProperty("imaphost")
    private String imaphost;
    /**
     * imap端口，例如默认的993
     */
    @JsonProperty("imapport")
    private Integer imapport;
    /**
     * 是否启用smtp的ssl加密
     */
    @JsonProperty("smtpssl")
    private Boolean smtpssl;

    /**
     * 是否启用imap的ssl加密
     */
    @JsonProperty("imapssl")
    private Boolean imapssl;
    /**
     * 修改的用户id
     */
    @JsonProperty("modifier")
    private String modifier;
    /**
     * 更新的时间
     */
    @JsonProperty("updateDate")
    private OffsetDateTime updateDate;
    /**
     * 创建的时间
     */
    @JsonProperty("createDate")
    private OffsetDateTime createDate;

    public String getId() {
        return id;
    }

    public EmailInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public EmailInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSmtphost() {
        return smtphost;
    }

    public EmailInfo setSmtphost(String smtphost) {
        this.smtphost = smtphost;
        return this;
    }

    public Integer getSmtpport() {
        return smtpport;
    }

    public EmailInfo setSmtpport(Integer smtpport) {
        this.smtpport = smtpport;
        return this;
    }

    public String getImaphost() {
        return imaphost;
    }

    public EmailInfo setImaphost(String imaphost) {
        this.imaphost = imaphost;
        return this;
    }

    public Integer getImapport() {
        return imapport;
    }

    public EmailInfo setImapport(Integer imapport) {
        this.imapport = imapport;
        return this;
    }

    public Boolean getSmtpssl() {
        return smtpssl;
    }

    public EmailInfo setSmtpssl(Boolean smtpssl) {
        this.smtpssl = smtpssl;
        return this;
    }

    public Boolean getImapssl() {
        return imapssl;
    }

    public EmailInfo setImapssl(Boolean imapssl) {
        this.imapssl = imapssl;
        return this;
    }

    public String getModifier() {
        return modifier;
    }

    public EmailInfo setModifier(String modifier) {
        this.modifier = modifier;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public EmailInfo setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public EmailInfo setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }
}

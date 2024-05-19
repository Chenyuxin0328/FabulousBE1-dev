package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 邮件模版
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailTemplate {

    /**
     * 邮件的唯一标识
     */
    @JsonProperty
    private String id;

    /**
     * 创建人
     */
    @JsonProperty
    private String creator;

    /**
     * 邮件的标题
     */
    @JsonProperty
    private String subject;

    /**
     * 邮件的姓名
     */
    @JsonProperty
    private String name;

    /**
     * 邮件的内容
     */
    @JsonProperty
    private String content;

    /**
     * 创建时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    /**
     * 更新时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    /**
     * 模版变量的json字符串（默认值）
     */
    private String variables;

    public String getId() {
        return id;
    }

    public EmailTemplate setId(String id) {
        this.id = id;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public EmailTemplate setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EmailTemplate setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getName() {
        return name;
    }

    public EmailTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EmailTemplate setContent(String content) {
        this.content = content;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public EmailTemplate setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public EmailTemplate setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getVariables() {
        return variables;
    }

    public EmailTemplate setVariables(String variables) {
        this.variables = variables;
        return this;
    }
}

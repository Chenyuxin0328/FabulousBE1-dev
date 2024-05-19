package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * @author minskiter
 * @date 31/8/2023 14:22
 * @description 模版内容
 */
public class DataTemplateContent {

    /**
     * 版本的Id
     */
    @JsonProperty
    private String versionId;

    /**
     * 内容
     */
    @JsonProperty
    private String content;

    /**
     * 作者
     */
    @JsonProperty
    private String author;

    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    /**
     * 模版的Id
     */
    @JsonProperty
    private String templateId;

    public String getVersionId() {
        return versionId;
    }

    public DataTemplateContent setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataTemplateContent setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public DataTemplateContent setAuthor(String author) {
        this.author = author;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataTemplateContent setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getTemplateId() {
        return templateId;
    }

    public DataTemplateContent setTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }
}

package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * @author minskiter
 * @date 30/8/2023 14:36
 * @description 数据笔记的内容
 */
public class DataPageContent {

    /**
     * 版本的Id
     */
    @JsonProperty
    private String versionId;
    /**
     * 数据笔记的Id
     */
    @JsonProperty
    private String pageId;

    /**
     * 数据笔记的内容
     */
    @JsonProperty
    private String content;

    /**
     * 拥有者
     */
    @JsonProperty
    private String author;

    @JsonProperty
    private String email;

    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    public String getVersionId() {
        return versionId;
    }

    public DataPageContent setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public String getPageId() {
        return pageId;
    }

    public DataPageContent setPageId(String pageId) {
        this.pageId = pageId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataPageContent setContent(String content) {
        this.content = content;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataPageContent setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public DataPageContent setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

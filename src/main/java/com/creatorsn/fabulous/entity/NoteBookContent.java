package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 笔记本的内容
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteBookContent {

    /**
     * 版本号
     */
    @JsonProperty
    private String versionId;

    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    /**
     * 作者
     */
    @JsonProperty
    private String author;

    /**
     * 用户的邮件
     */
    @JsonProperty
    private String email;

    /**
     * 内容
     */
    @JsonProperty
    private String content;

    /**
     * 笔记本的Id
     */
    @JsonProperty
    private String notebookId;

    public String getVersionId() {
        return versionId;
    }

    public NoteBookContent setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public NoteBookContent setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public NoteBookContent setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NoteBookContent setContent(String content) {
        this.content = content;
        return this;
    }

    public String getNotebookId() {
        return notebookId;
    }

    public NoteBookContent setNotebookId(String notebookId) {
        this.notebookId = notebookId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public NoteBookContent setEmail(String email) {
        this.email = email;
        return this;
    }
}

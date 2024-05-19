package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

/**
 * @author minskiter
 * @date 22/8/2023 09:46
 * @description
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotebookDTO {

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 标题
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 头图
     */
    private String banner;

    /**
     * 创建的时间
     */
    private OffsetDateTime createDate;

    /**
     * 更新的时间
     */
    private OffsetDateTime updateDate;

    /**
     * 父节点
     */
    private String parent;

    /**
     * 内容的版本号
     */
    private String versionId;

    /**
     * 内容
     */
    private String content;

    /**
     * 笔记本创建者
     */
    private String owner;

    /**
     * 数据源的Id
     */
    private String sourceId;

    public String getId() {
        return id;
    }

    public NotebookDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NotebookDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NotebookDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getBanner() {
        return banner;
    }

    public NotebookDTO setBanner(String banner) {
        this.banner = banner;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public NotebookDTO setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public NotebookDTO setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public NotebookDTO setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public String getVersionId() {
        return versionId;
    }

    public NotebookDTO setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NotebookDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public NotebookDTO setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public NotebookDTO setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }
}

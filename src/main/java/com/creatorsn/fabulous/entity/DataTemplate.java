package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * @author minskiter
 * @date 31/8/2023 11:37
 * @description 数据模版
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataTemplate {

    /**
     * 数据模版的Id
     */
    @JsonProperty
    private String id;

    /**
     * 模版名称
     */
    @JsonProperty
    private String name;

    /**
     * 模版创建者
     */
    @JsonProperty
    private String owner;

    /**
     * 模版的图标
     */
    @JsonProperty
    private String emoji;

    /**
     * 模版的父节点
     */
    @JsonProperty
    private String parent;

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

    /**
     * 模版的内容
     */
    @JsonProperty
    private String content;

    /**
     * 模版的版本号
     */
    @JsonProperty
    private String versionId;

    public String getId() {
        return id;
    }

    public DataTemplate setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public DataTemplate setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataTemplate setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public DataTemplate setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public DataTemplate setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataTemplate setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
}

package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 笔记
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPage {

    /**
     * 笔记的唯一标识
     */
    @JsonProperty
    private String id;

    /**
     * 笔记的名称
     */
    @JsonProperty
    private String name;

    /**
     * 笔记的表情
     */
    @JsonProperty
    private String emoji;

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
     * 内容版本
     */
    @JsonProperty
    private String versionId;

    /**
     * 内容
     */
    @JsonProperty
    private String content;

    /**
     * 拥有者
     */
    @JsonProperty
    private String owner;

    /**
     * 父节点
     */
    @JsonProperty
    private String parent;

    public String getId() {
        return id;
    }

    public DataPage setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataPage setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataPage setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public DataPage setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataPage setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public DataPage setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public String getVersionId() {
        return versionId;
    }

    public DataPage setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataPage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public DataPage setOwner(String owner) {
        this.owner = owner;
        return this;
    }
}

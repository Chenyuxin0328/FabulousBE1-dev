package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 数据分区（Page集合）
 */
public class DataPartition {

    /**
     * 组的唯一标识
     */
    @JsonProperty
    private String id;

    /**
     * 组的名称
     */
    @JsonProperty
    private String name;

    /**
     * 表情符号
     */
    @JsonProperty
    private String emoji;

    /**
     * 数据源
     */
    @JsonProperty
    private String sourceId;

    /**
     * 拥有者
     */
    @JsonProperty
    private String owner;

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
     * 父节点
     */
    private String parent;

    public String getId() {
        return id;
    }

    public DataPartition setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataPartition setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataPartition setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public DataPartition setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public DataPartition setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataPartition setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public DataPartition setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public DataPartition setOwner(String owner) {
        this.owner = owner;
        return this;
    }
}

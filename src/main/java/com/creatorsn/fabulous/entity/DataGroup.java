package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 数据分组，类似于文件夹
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataGroup implements Cloneable {

    /**
     * 文件夹的Id
     */
    @JsonProperty
    private String id;

    /**
     * 文件夹的名称
     */
    @JsonProperty
    private String name;

    /**
     * 文件夹的图标
     */
    @JsonProperty
    private String emoji;

    /**
     * 文件夹的创建者
     */
    @JsonProperty
    private String owner;

    /**
     * 父亲节点
     */
    @JsonProperty
    private String parent;

    /**
     * 数据源的Id
     */
    @JsonProperty
    private String sourceId;

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

    public DataGroup setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataGroup setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataGroup setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public DataGroup setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public DataGroup setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataGroup setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public DataGroup setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public DataGroup setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    @Override
    public DataGroup clone() {
        try {
            DataGroup clone = (DataGroup) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

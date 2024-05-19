package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 笔记本
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notebook implements Cloneable {

    /**
     * 唯一标识
     */
    @JsonProperty
    private String id;

    /**
     * 标题
     */
    @JsonProperty
    private String name;

    /**
     * 描述
     */
    @JsonProperty
    private String description;

    /**
     * 头图
     */
    @JsonProperty
    private String banner;

    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    /**
     * 更新的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    /**
     * 父节点
     */
    @JsonProperty
    private String parent;

    /**
     * 笔记本的内容
     */
    @JsonProperty
    private NoteBookContent content;

    /**
     * 笔记本创建者
     */
    @JsonProperty
    private String owner;

    /**
     * 数据源的Id
     */
    @JsonProperty
    private String sourceId;

    public String getId() {
        return id;
    }

    public Notebook setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Notebook setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Notebook setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getBanner() {
        return banner;
    }

    public Notebook setBanner(String banner) {
        this.banner = banner;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public Notebook setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public Notebook setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public Notebook setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public NoteBookContent getContent() {
        return content;
    }

    public Notebook setContent(NoteBookContent content) {
        this.content = content;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public Notebook setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Notebook setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    @Override
    public Notebook clone() {
        try {
            Notebook clone = (Notebook) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

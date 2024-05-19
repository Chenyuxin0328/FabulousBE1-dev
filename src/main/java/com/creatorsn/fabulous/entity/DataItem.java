package com.creatorsn.fabulous.entity;

import com.creatorsn.fabulous.dto.ItemLabel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * PDF数据项
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataItem {

    /**
     * 数据的标识
     */
    @JsonProperty
    private String id;

    /**
     * 数据的名称
     */
    @JsonProperty
    private String name;

    /**
     * 表情符号
     */
    @JsonProperty
    private String emoji;

    /**
     * 元信息
     */
    @JsonProperty
    private String metadataId;

    /**
     * 元信息
     */
    @JsonProperty
    private ItemMetadata metadata;

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
     * 标签
     */
    @JsonProperty
    private List<ItemLabel> labels;


    /**
     * 拥有者
     */
    @JsonProperty
    private String owner;

    /**
     * 数据源的Id
     */
    @JsonProperty
    private String sourceId;

    /**
     * PDF的唯一标识
     */
    @JsonProperty
    private String pdf;

    /**
     * 笔记的信息
     */
    @JsonProperty
    private List<DataPage> pages;

    public String getId() {
        return id;
    }

    public DataItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataItem setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public DataItem setMetadataId(String metadataId) {
        this.metadataId = metadataId;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataItem setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public DataItem setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public DataItem setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public DataItem setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getPdf() {
        return pdf;
    }

    public DataItem setPdf(String pdf) {
        this.pdf = pdf;
        return this;
    }

    public ItemMetadata getMetadata() {
        return metadata;
    }

    public DataItem setMetadata(ItemMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public List<ItemLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ItemLabel> labels) {
        this.labels = labels;
    }

    public List<DataPage> getPages() {
        return pages;
    }

    public void setPages(List<DataPage> pages) {
        this.pages = pages;
    }
}

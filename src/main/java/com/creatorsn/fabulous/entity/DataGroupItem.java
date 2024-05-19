/**
 * @author minskiter@qq.com
 * @date 2023/08/09 18:56
 * @description 定义数据分组子项
 */

package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 数据分组子项（合并项)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataGroupItem {

    /**
     * 唯一标识
     */
    @JsonProperty("id")
    private String id;
    /**
     * 名称
     */
    @JsonProperty
    private String name;
    /**
     * 标题（笔记本独有）
     */
    @JsonProperty
    private String title;
    /**
     * 描述
     */
    @JsonProperty
    private String description;
    /**
     * 图标
     */
    @JsonProperty
    private String emoji;
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
     * 头图
     */
    @JsonProperty
    private String banner;
    /**
     * 类型
     */
    @JsonProperty
    private DataGroupItemType type;

    public String getId() {
        return id;
    }

    public DataGroupItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataGroupItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DataGroupItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DataGroupItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataGroupItem setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public DataGroupItem setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public DataGroupItem setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataGroupItem setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public DataGroupItem setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getBanner() {
        return banner;
    }

    public DataGroupItem setBanner(String banner) {
        this.banner = banner;
        return this;
    }

    public DataGroupItemType getType() {
        return type;
    }

    public DataGroupItem setType(DataGroupItemType type) {
        this.type = type;
        return this;
    }

    /**
     * 子项类型
     */
    public enum DataGroupItemType {
        notebook,
        group
    }
}

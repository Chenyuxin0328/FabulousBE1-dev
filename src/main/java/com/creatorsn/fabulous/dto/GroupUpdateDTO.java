package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

/**
 * 分组更新数据传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupUpdateDTO {

    /**
     * 分组的Id
     */
    @Pattern(regexp = RegexPattern.GUID, message = "分组的Id格式错误")
    @JsonProperty
    private String id;

    /**
     * 分组的名称
     */
    @JsonProperty
    private String name;

    /**
     * 分组的图标
     */
    @JsonProperty
    private String emoji;

    /**
     * 分组的父节点
     */
    @Pattern(regexp = RegexPattern.GUID, message = "分组的父节点格式错误")
    @JsonProperty("parent")
    private String parent;

    public String getName() {
        return name;
    }

    public GroupUpdateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public GroupUpdateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public GroupUpdateDTO setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public String getId() {
        return id;
    }

    public GroupUpdateDTO setId(String id) {
        this.id = id;
        return this;
    }
}

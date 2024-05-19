package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

/**
 * @author minskiter
 * @date 30/8/2023 10:40
 * @description 数据项笔记更新传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPageUpdateDTO {

    /**
     * 笔记的Id
     */
    @JsonProperty
    private String id;

    /**
     * 数据项笔记的名称
     */
    @JsonProperty
    private String name;

    /**
     * 数据项笔记的图标
     */
    @JsonProperty
    private String emoji;

    /**
     * 数据项笔记的父节点
     */
    @JsonProperty
    @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误")
    private String parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * @author minskiter
 * @date 30/8/2023 10:35
 * @description 数据项笔记创建传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPageCreateDTO {

    /**
     * 笔记的名称
     */
    @JsonProperty
    @NotNull(message = "笔记的名称不能为空")
    private String name;

    /**
     * 笔记的表情
     */
    @JsonProperty
    private String emoji;

    /**
     * 文本内容
     */
    @JsonProperty
    private String content;

    public String getName() {
        return name;
    }

    public DataPageCreateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataPageCreateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataPageCreateDTO setContent(String content) {
        this.content = content;
        return this;
    }
}

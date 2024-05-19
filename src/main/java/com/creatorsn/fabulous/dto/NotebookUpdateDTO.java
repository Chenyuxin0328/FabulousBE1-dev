package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author minskiter
 * @date 7/9/2023 20:06
 * @description 笔记本更新传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotebookUpdateDTO {

    @JsonProperty
    private String name;

    @JsonProperty
    private String emoji;

    public String getName() {
        return name;
    }

    public NotebookUpdateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public NotebookUpdateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }
}

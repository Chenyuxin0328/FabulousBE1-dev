package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * @author minskiter
 * @date 31/8/2023 15:13
 * @description 创建模版的传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataTemplateCreateDTO {

    /**
     * 模版名称
     */
    @JsonProperty
    @NotNull
    private String name;

    /**
     * 模版的图标
     */
    @JsonProperty
    private String emoji;

    public String getName() {
        return name;
    }

    public DataTemplateCreateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataTemplateCreateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }
}

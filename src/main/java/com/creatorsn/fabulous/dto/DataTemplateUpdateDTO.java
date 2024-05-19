package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @author minskiter
 * @date 31/8/2023 15:16
 * @description
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataTemplateUpdateDTO {

    @JsonProperty
    @Pattern(regexp = RegexPattern.GUID, message = "模版的Id格式错误")
    @NotNull
    private String id;

    /**
     * 模版的名称
     */
    @JsonProperty
    private String name;

    /**
     * 模版的图标
     */
    @JsonProperty
    private String emoji;

    public String getName() {
        return name;
    }

    public DataTemplateUpdateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataTemplateUpdateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

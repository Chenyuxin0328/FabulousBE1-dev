package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

/**
 * 分组创建数据传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupCreateDTO {

    /**
     * 分组名称
     */
    @JsonProperty("name")
    @NotEmpty(message = "分组的名称不能为空")
    private String name;

    /**
     * 分组的图标
     */
    @JsonProperty("emoji")
    @Schema(description = "图标", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String emoji;

    public String getName() {
        return name;
    }

    public GroupCreateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public GroupCreateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }
}

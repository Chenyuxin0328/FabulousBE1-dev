package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 角色请求类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleRequest {

    /**
     * 角色的名称
     */
    @JsonProperty("name")
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 角色的描述
     */
    @Schema(description = "描述",requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("description")
    private String description;

    public String getName() {
        return name;
    }

    public RoleRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoleRequest setDescription(String description) {
        this.description = description;
        return this;
    }
}

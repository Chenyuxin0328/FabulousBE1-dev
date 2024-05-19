package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 添加网站配置数据传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddWebsiteConfigDTO {

    /**
     * 网站配置名称
     */
    @JsonProperty("name")
    @Schema(description = "网站配置名称")
    @NotNull(message = "网站配置名称不能为空")
    private String name;

    /**
     * 网站配置值
     */
    @JsonProperty("value")
    @Schema(description = "网站配置值")
    @NotNull(message = "网站配置值不能为空")
    private String value;

    /**
     * 网站配置权限
     */
    @JsonProperty("permission")
    @Schema(description = "网站配置可视权限")
    @NotNull(message = "网站配置可视权限不能为空")
    @Pattern(regexp = "^ALL|ADMIN|USER$", message = "网站配置可视权限只能为ALL|ADMIN|USER")
    private String permission;

    public String getName() {
        return name;
    }

    public AddWebsiteConfigDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public AddWebsiteConfigDTO setValue(String value) {
        this.value = value;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    public AddWebsiteConfigDTO setPermission(String permission) {
        this.permission = permission;
        return this;
    }
}

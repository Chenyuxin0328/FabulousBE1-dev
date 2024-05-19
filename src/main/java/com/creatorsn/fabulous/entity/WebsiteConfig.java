package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 网站配置
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteConfig {

    /**
     * 网站配置id
     */
    @JsonProperty
    private String id;
    /**
     * 网站配置名称
     */
    @JsonProperty
    private String name;
    /**
     * 网站配置值
     */
    @JsonProperty
    private String value;
    /**
     * 网站配置创建者
     */
    @JsonProperty
    private String createdBy;
    /**
     * 网站配置权限
     */
    @JsonProperty
    private WebsiteConfigPermission permission;
    /**
     * 网站配置创建时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;
    /**
     * 网站配置更新时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    public String getId() {
        return id;
    }

    public WebsiteConfig setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WebsiteConfig setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public WebsiteConfig setValue(String value) {
        this.value = value;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public WebsiteConfig setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public WebsiteConfigPermission getPermission() {
        return permission;
    }

    public WebsiteConfig setPermission(WebsiteConfigPermission permission) {
        this.permission = permission;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public WebsiteConfig setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public WebsiteConfig setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    /**
     * 网站配置权限
     */
    public enum WebsiteConfigPermission {
        ALL,
        ADMIN,
        USER
    }
}

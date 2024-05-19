package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 用户角色
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRole {

    /**
     * 角色的唯一标识
     */
    @JsonProperty
    private String id;

    /**
     * 角色的名称
     */
    @JsonProperty
    private String name;

    /**
     * 角色的描述
     */
    @JsonProperty
    private String description;

    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    /**
     * 更新的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    public String getId() {
        return id;
    }

    public UserRole setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserRole setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UserRole setDescription(String description) {
        this.description = description;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public UserRole setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public UserRole setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }
}

package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * Wiki的数据存储结构
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataStruct {

    /**
     * 数据存储的Id
     */
    @JsonProperty
    private String id;

    /**
     * 数据存储的名称
     */
    @JsonProperty
    private String name;

    /**
     * 同sourceId
     */
    @JsonProperty
    private String path;

    /**
     * 数据存储创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    /**
     * 数据存储更新的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    /**
     * 数据所属的用户
     */
    @JsonProperty
    private String userId;

    public String getId() {
        return id;
    }

    public DataStruct setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataStruct setName(String name) {
        this.name = name;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public DataStruct setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public DataStruct setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public DataStruct setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPath() {
        return path;
    }

    public DataStruct setPath(String path) {
        this.path = path;
        return this;
    }
}

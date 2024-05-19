package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Email 的发送量计数
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailUsed {

    /**
     * 邮箱的Id
     */
    @JsonProperty
    private String id;

    /**
     * 邮箱的发送量
     */
    @JsonProperty
    private long count;

    public String getId() {
        return id;
    }

    public EmailUsed setId(String id) {
        this.id = id;
        return this;
    }

    public long getCount() {
        return count;
    }

    public EmailUsed setCount(long count) {
        this.count = count;
        return this;
    }
}

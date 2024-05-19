package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 用户登陆访问的令牌
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionToken {
    /**
     * 访问令牌
     */
    @JsonProperty
    private String accessToken;
    /**
     * 令牌的方案
     */
    @JsonProperty
    private String schema;
    /**
     * 过期的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime expiredAt;
    /**
     * 授权的区域或者权限
     */
    @JsonProperty
    private List<String> scopes;

    public String getAccessToken() {
        return accessToken;
    }

    public SessionToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public SessionToken setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public OffsetDateTime getExpiredAt() {
        return expiredAt;
    }

    public SessionToken setExpiredAt(OffsetDateTime expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public SessionToken setScopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }
}

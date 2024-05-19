package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.entity.UserGenderEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户查询")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserQuery {

    @JsonProperty("query")
    private String query;

    @JsonProperty("gender")
    private UserGenderEnum gender = UserGenderEnum.SECRET;

    @JsonProperty("offset")
    private long offset = 0;

    @JsonProperty("pageSize")
    private int pageSize = 20;

    public String getQuery() {
        return query;
    }

    public UserQuery setQuery(String query) {
        this.query = query;
        return this;
    }

    public UserGenderEnum getGender() {
        return gender;
    }

    public UserQuery setGender(UserGenderEnum gender) {
        this.gender = gender;
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public UserQuery setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public UserQuery setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}

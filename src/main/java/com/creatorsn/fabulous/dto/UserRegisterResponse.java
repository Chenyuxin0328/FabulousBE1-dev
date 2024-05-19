package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegisterResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("nickname")
    private String nickName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    public String getIdcard() {
        return idcard;
    }

    public UserRegisterResponse setIdcard(String idcard) {
        this.idcard = idcard;
        return this;
    }

    @JsonProperty("idcard")
    private String idcard;

    @JsonProperty("createDate")
    private OffsetDateTime createDate;

    public String getId() {
        return id;
    }

    public UserRegisterResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserRegisterResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public UserRegisterResponse setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserRegisterResponse setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public UserRegisterResponse setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }
}

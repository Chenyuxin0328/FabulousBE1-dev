package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.entity.UserGenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "用户信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String nickName;

    @JsonProperty
    private UserGenderEnum gender;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime birth;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime lastLoginAt;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;

    @JsonProperty
    private String email;

    @JsonProperty
    private String phone;

    @JsonProperty
    private boolean emailVerified;

    @JsonProperty
    private boolean phoneVerified;

    @JsonProperty
    private String idcard;

    public String getIdcard() {
        return idcard;
    }

    public UserInfo setIdcard(String idcard) {
        this.idcard = idcard;
        return this;
    }

    public String getId() {
        return id;
    }

    public UserInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserInfo setName(String Name) {
        this.name = Name;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public UserInfo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public UserGenderEnum getGender() {
        return gender;
    }

    public UserInfo setGender(UserGenderEnum gender) {
        this.gender = gender;
        return this;
    }

    public OffsetDateTime getBirth() {
        return birth;
    }

    public UserInfo setBirth(OffsetDateTime birth) {
        this.birth = birth;
        return this;
    }

    public OffsetDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public UserInfo setLastLoginAt(OffsetDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public UserInfo setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserInfo setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public UserInfo setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public UserInfo setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
        return this;
    }
}

package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * 用户类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    /**
     * 用户的唯一Id
     */
    @JsonProperty
    private String id;
    /**
     * 用户名
     */
    @JsonProperty
    private String name;
    /**
     * 用户的昵称
     */
    @JsonProperty
    private String nickName;
    /**
     * 用户的密码
     */
    @JsonProperty
    private String password;
    /**
     * 用户创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;
    /**
     * 用户最后登陆的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime lastLoginAt;
    /**
     * 用户最后一次登陆的IP
     */
    @JsonProperty
    private String lastLoginIP;
    /**
     * 用户的性别
     */
    @JsonProperty
    private UserGenderEnum gender = UserGenderEnum.SECRET;
    /**
     * 用户的头像，BASE64编码
     */
    @JsonProperty
    private String avatar;
    /**
     * 用户的邮箱
     */
    @JsonProperty
    private String email;
    /**
     * 用户的手机号码
     */
    @JsonProperty
    private String phone;
    /**
     * 用户邮箱是否验证过
     */
    @JsonProperty
    private boolean emailVerified = false;
    /**
     * 用户手机号码是否验证过
     */
    @JsonProperty
    private boolean phoneVerified = false;
    /**
     * 用户出生年月日
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime birth;
    /**
     * 用户信息更新的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;
    /**
     * 身份证号码
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private String idcard;

    public String getIdcard() {
        return idcard;
    }

    public User setIdcard(String idcard) {
        this.idcard = idcard;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public OffsetDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(OffsetDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public UserGenderEnum getGender() {
        return gender;
    }

    public void setGender(UserGenderEnum gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public OffsetDateTime getBirth() {
        return birth;
    }

    public void setBirth(OffsetDateTime birth) {
        this.birth = birth;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
    }
}

package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.entity.UserGenderEnum;
import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

@Schema(description = "用户更新传输对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdate {

    @JsonProperty("name")
    @Schema(type="String", description = "用户的真实名字")
    @Size(min=2,max=20,message = "名字 ${validatedValue} 不能低于2或者超过20")
    private String name;

    @JsonProperty("nickname")
    @Schema(type="String", description = "用户的昵称")
    @Size(min=2,max=20,message = "昵称 ${validatedValue} 不能低于2或者超过20")
    private String nickName;

    @JsonProperty("birth")
    @Schema(description = "用户出生的时间")
    private OffsetDateTime birth;

    @JsonProperty("gender")
    @Schema(type="String",description = "用户的性别MALE/FEMALE")
    private UserGenderEnum gender = UserGenderEnum.SECRET;

    @JsonProperty("email")
    @Schema(type = "String",description = "用户的邮箱",defaultValue = "fabulous@creatorsn.com")
    @Pattern(regexp = RegexPattern.Email, message = "邮箱 ${validatedValue} 格式错误")
    private String email;

    @JsonProperty("phone")
    @Schema(type="String",description = "手机号码")
    @Pattern(regexp = RegexPattern.Phone, message = "手机 ${validatedValue} 格式错误")
    private String phone;

    public String getNickName() {
        return nickName;
    }

    public UserUpdate setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public OffsetDateTime getBirth() {
        return birth;
    }

    public UserUpdate setBirth(OffsetDateTime birth) {
        this.birth = birth;
        return this;
    }

    public UserGenderEnum getGender() {
        return gender;
    }

    public UserUpdate setGender(UserGenderEnum gender) {
        this.gender = gender;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserUpdate setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserUpdate setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserUpdate setName(String name) {
        this.name = name;
        return this;
    }
}

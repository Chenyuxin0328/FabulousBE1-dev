package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "用户注册类")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegister {

    @Schema(description = "姓名",defaultValue = "李世民")
    @Size(min=2,max=20,message = "真实姓名 ${validatedValue} 长度不能低于2、不能超过20")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "密码不能为空")
    @Schema(description = "密码",defaultValue = "fabulous")
    @Size(min=8,max=20,message = "密码 ${validatedValue} 长度不能低于8、不能超过20")
    @JsonProperty("password")
    private String password;

    @NotNull(message = "邮箱${validatedName}是必须的")
    @Pattern(regexp = RegexPattern.Email, message = "邮箱 ${validatedValue} 格式错误")
    @Schema(description = "邮箱",defaultValue = "fabulous@creatorsn.com")
    @JsonProperty("email")
    private String email;

    @Schema(description = "手机号码",defaultValue = "110")
    @Pattern(regexp = RegexPattern.Phone, message = "电话号码 ${validatedValue} 格式错误")
    @JsonProperty("phone")
    private String phone;

    @Size(min=1, max=12,message = "昵称长度不能超过12")
    @Schema(description = "昵称",defaultValue = "fabulous")
    @JsonProperty("nickname")
    private String nickName;

    public String getName() {
        return name;
    }

    public UserRegister setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegister setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegister setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserRegister setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public UserRegister setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}

package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "UserUpdatePassword", description = "用户修改密码")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdatePassword {


    @Schema(name = "password", description = "用户的密码")
    @Size(min=8,max=20,message = "密码 ${validatedValue} 长度不能低于8、不能超过20")
    @JsonProperty("password")
    private String password;

    @Schema(name = "code", description = "用户的验证码")
    @JsonProperty("code")
    @Pattern(regexp = RegexPattern.VerifiedCode, message = "验证码格式错误")
    private String code;

    public String getPassword() {
        return password;
    }

    public UserUpdatePassword setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UserUpdatePassword setCode(String code) {
        this.code = code;
        return this;
    }
}

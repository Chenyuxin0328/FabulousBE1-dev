package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "用户登陆传输对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogin {

    /**
     * 用户的Id
     */
    @Schema(type = "String", description = "用户的Id", defaultValue = "fabulous", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("id")
    private String id;

    @JsonProperty("password")
    @Size(min=8,max=20,message = "密码 ${validatedValue} 长度不能低于8、不能超过20")
    @Schema(type = "String", description = "用户的密码", defaultValue = "fabulous", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @JsonProperty("code")
    @Pattern(regexp = RegexPattern.VerifiedCode, message = "验证码格式不正确")
    @Schema(type = "String", description = "验证码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;

    public String getId() {
        return id;
    }

    public UserLogin setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UserLogin setCode(String code) {
        this.code = code;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserLogin setPassword(String password) {
        this.password = password;
        return this;
    }
}

package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

/**
 * 更新头像请求
 */
@Schema(description = "更新头像请求")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateAvatarRequest {

    @JsonProperty("avatar")
    @Schema(description = "头像", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = RegexPattern.BASE64Image, message = "头像格式必须是data:image/(jpg|jpeg|png|gif);base64,.*")
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public UpdateAvatarRequest setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
}

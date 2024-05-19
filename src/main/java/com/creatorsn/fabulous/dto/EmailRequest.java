package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailRequest {
    /**
     * 用户名，通常是邮箱的地址
     */
    @JsonProperty("username")
    @Schema(description = "用户名",requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码",requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("password")
    private String password;
    /**
     * 邮箱的smtp地址，例如smtp.qq.com
     */
    @Schema(description = "邮箱的smtp地址",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("smtphost")
    private String smtphost;
    /**
     * SMTP端口，例如默认的465
     */
    @Schema(description = "SMTP端口",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("smtpport")
    private Integer smtpport;
    /**
     * imap地址，例如imap.qq.com
     */
    @Schema(description = "imap地址",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("imaphost")
    private String imaphost;
    /**
     * imap端口，例如默认的993
     */
    @Schema(description = "imap端口",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("imapport")
    private Integer imapport;
    /**
     * 是否启用smtp的ssl加密
     */
    @Schema(description = "是否启用smtp的ssl加密",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("smtpssl")
    private Boolean smtpssl;

    /**
     * 是否启用imap的ssl加密
     */
    @Schema(description = "是否启用imap的ssl加密",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("imapssl")
    private Boolean imapssl;
}

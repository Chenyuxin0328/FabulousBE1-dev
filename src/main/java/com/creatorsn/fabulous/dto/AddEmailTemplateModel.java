package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;

/**
 * 添加邮件模版的模型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddEmailTemplateModel {

    @Schema(description = "模版名称")
    @JsonProperty("name")
    @NotNull(message = "模版名称不能为空")
    private String name;

    @Schema(description = "模版主题")
    @JsonProperty("subject")
    @NotNull(message = "模版主题不能为空")
    private String subject;

    @Schema(description = "模版内容")
    @JsonProperty("content")
    @NotNull(message = "模版内容不能为空")
    private String content;

    @Schema(description = "模版变量")
    @JsonProperty("variables")
    private HashMap<String, String> variables;

    public String getName() {
        return name;
    }

    public AddEmailTemplateModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public AddEmailTemplateModel setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public AddEmailTemplateModel setContent(String content) {
        this.content = content;
        return this;
    }

    public HashMap<String, String> getVariables() {
        return variables;
    }

    public AddEmailTemplateModel setVariables(HashMap<String, String> variables) {
        this.variables = variables;
        return this;
    }
}

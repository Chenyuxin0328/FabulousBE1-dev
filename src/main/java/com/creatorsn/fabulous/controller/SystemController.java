package com.creatorsn.fabulous.controller;


import com.creatorsn.fabulous.dto.*;
import com.creatorsn.fabulous.entity.Email;
import com.creatorsn.fabulous.entity.EmailTemplate;
import com.creatorsn.fabulous.entity.WebsiteConfig;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.service.EmailService;
import com.creatorsn.fabulous.service.WebsiteService;
import com.creatorsn.fabulous.util.RegexPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 系统配置接口
 */
@RestController
@Tag(name = "SystemController", description = "系统接口")
@SecurityRequirement(name = "Authorization")
@Validated
public class SystemController extends ControllerBase {

    final private EmailService emailService;

    final private WebsiteService websiteService;

    public SystemController(
            ModelMapper modelMapper,
            EmailService emailService,
            WebsiteService websiteService
    ) {
        super(modelMapper);
        this.emailService = emailService;
        this.websiteService = websiteService;
    }

    /**
     * 获取系统邮件
     *
     * @param query    查询关键字
     * @param offset   偏移
     * @param pageSize 页大小
     * @param lastKey  上一页最后一条记录的key
     * @return 返回系统邮件
     */
    @Operation(summary = "获取系统邮件")
    @GetMapping("/system/emails")
    @PreAuthorize("hasRole('admin')")
    public StdResult getSystemEmails(
            @Valid
            @RequestParam(required = false, value = "query")
            String query,
            @Valid
            @RequestParam(required = false, value = "offset", defaultValue = "0")
            @Min(value = 0, message = "偏移不能小于0")
            int offset,
            @Valid
            @RequestParam(required = false, value = "pageSize", defaultValue = "10")
            int pageSize,
            @Valid
            @RequestParam(required = false, value = "lastKey")
            String lastKey
    ) {
        var emails = this.emailService.getEmails(query, offset, pageSize, lastKey);
        var result = emails.stream().map(email -> this.modelMapper.map(email, EmailInfo.class)).collect(Collectors.toList());
        return ok(result);
    }

    /**
     * 添加系统邮件
     *
     * @param emailRequest 系统邮件请求
     * @return 返回添加的系统邮件
     */
    @Operation(summary = "添加系统邮件")
    @PostMapping("/system/emails")
    @PreAuthorize("hasRole('admin')")
    public StdResult addEmail(
            @RequestBody
            @Valid
            EmailRequest emailRequest
    ) {
        var userId = getUserId();
        var entity = this.modelMapper.map(emailRequest, Email.class);
        entity.setModifier(userId);
        var result = this.emailService.addEmail(entity);
        var dto = this.modelMapper.map(result, EmailInfo.class);
        return ok(dto);
    }

    /**
     * 删除系统邮件
     *
     * @param id 系统邮件id
     * @return 如果成功返回Ok
     */
    @Operation(summary = "删除系统邮件")
    @DeleteMapping("/system/emails/{id}")
    @PreAuthorize("hasRole('admin')")
    public StdResult delete(
            @PathVariable("id")
            @Pattern(regexp = RegexPattern.GUID, message = "id格式错误")
            @Valid
            String id
    ) {
        this.emailService.deleteEmailById(id);
        return ok();
    }

    /**
     * 获取系统所有的状态码
     *
     * @return 返回系统所有的状态码
     */
    @Operation(summary = "获取系统所有的状态码")
    @GetMapping("/system/statusCode")
    @PermitAll
    public StdResult getAllStatusCode(
    ) {
        HashMap<Integer, String> mapper = new HashMap<>();
        for (var code : StatusCode.values()) {
            mapper.put(code.getValue(), code.name());
        }
        return ok(mapper);
    }

    /**
     * 添加系统邮件模板
     *
     * @param addEmailTemplateModel 添加系统邮件模板请求
     * @return 返回添加的系统邮件模板
     * @throws UserException 用户异常
     */
    @Operation(summary = "添加系统邮件模板")
    @PostMapping("/system/emailTemplates")
    public StdResult addEmailTemplate(
            @RequestBody
            @Valid
            AddEmailTemplateModel addEmailTemplateModel
    ) throws UserException {
        var userId = getUserId();
        var template = modelMapper.map(addEmailTemplateModel, EmailTemplate.class);
        template.setCreator(userId);
        template = emailService.addTemplate(template);
        return ok(template);
    }

    /**
     * 获取系统邮件模板
     *
     * @param before   获取指定时间之前的模板
     * @param offset   偏移
     * @param pageSize 页大小
     * @return 返回系统邮件模板
     */
    @Operation(summary = "获取系统邮件模板")
    @GetMapping("/system/emailTemplates")
    public StdResult getEmailTemplate(
            @RequestParam(required = false, value = "before")
            @Valid
            OffsetDateTime before,
            @RequestParam(required = false, value = "offset", defaultValue = "0")
            @Min(value = 0, message = "偏移不能小于0")
            @Valid
            long offset,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10")
            @Min(value = 10, message = "pageSize不能小于10")
            @Max(value = 100, message = "pageSize不能大于100")
            @Valid
            int pageSize
    ) {
        var templates = emailService.getTemplates(before, offset, pageSize);
        return ok(templates);
    }

    /**
     * 更新系统邮件模板
     *
     * @param id                       系统邮件模板id
     * @param updateEmailTemplateModel 更新系统邮件模板请求
     * @return 返回更新后的系统邮件模板
     * @throws UserException 用户异常
     */
    @Operation(summary = "更新系统邮件模板")
    @PostMapping("/system/emailTemplates/{id}")
    public StdResult updateEmailTemplate(
            @PathVariable("id")
            @Pattern(regexp = RegexPattern.GUID, message = "id格式错误")
            @Valid
            String id,
            @RequestBody
            @Valid
            UpdateEmailTemplateModel updateEmailTemplateModel
    ) throws UserException {
        var template = modelMapper.map(updateEmailTemplateModel, EmailTemplate.class);
        template.setId(id);
        template = emailService.updateTemplate(template);
        return ok(template);
    }

    /**
     * 添加系统配置
     *
     * @param addWebsiteConfigDTO 系统配置请求
     * @return 返回系统配置
     */
    @Operation(summary = "添加系统配置")
    @PostMapping("/system/config")
    public StdResult setConfig(
            @RequestBody
            @Valid
            AddWebsiteConfigDTO addWebsiteConfigDTO
    ) {
        var userId = getUserId();
        var config = modelMapper.map(addWebsiteConfigDTO, WebsiteConfig.class);
        config.setCreatedBy(userId);
        config = websiteService.create(config);
        return ok(config);
    }

    /**
     * 获取公开的系统配置
     *
     * @param name 系统配置名称
     * @return 返回公开的系统配置
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取系统配置")
    @GetMapping("/system/config/public/{name}")
    @PermitAll
    public StdResult getPublicConfig(
            @PathVariable
            @NotNull
            @Valid
            String name
    ) throws UserException {
        var config = websiteService.get(name);
        if (config == null || config.getPermission() != WebsiteConfig.WebsiteConfigPermission.ALL) {
            return badRequest();
        }
        return ok(config);
    }

    /**
     * 获取登陆系统配置
     *
     * @param name 系统配置名称
     * @return 返回登陆系统配置
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取登陆系统配置")
    @GetMapping("/system/config/user/{name}")
    @PreAuthorize("isAuthenticated()")
    public StdResult getUserConfig(
            @PathVariable
            @NotNull
            @Valid
            String name
    ) throws UserException {
        var config = websiteService.get(name);
        if (config == null || config.getPermission() == WebsiteConfig.WebsiteConfigPermission.ADMIN) {
            return badRequest();
        }
        return ok(config);
    }

    /**
     * 获取管理员系统配置
     *
     * @param name 系统配置名称
     * @return 返回管理员系统配置
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取管理员系统配置")
    @GetMapping("/system/config/admin/{name}")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('admin')")
    public StdResult getAdminConfig(
            @PathVariable
            @NotNull
            @Valid
            String name
    ) throws UserException {
        var config = websiteService.get(name);
        if (config == null) {
            return badRequest();
        }
        return ok(config);
    }

}

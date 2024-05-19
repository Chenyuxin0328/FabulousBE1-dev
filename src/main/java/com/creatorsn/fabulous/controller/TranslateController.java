package com.creatorsn.fabulous.controller;


import com.creatorsn.fabulous.dto.StdResult;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.service.TranslateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "TranslateController", description = "翻译接口")
@SecurityRequirement(name = "Authorization")//"Authorization" 表示需要提供认证信息
@Validated//在 Spring 中，可以在控制器方法的参数上使用 Bean Validation API 的注解（例如 @NotNull, @Size 等），然后在方法上加上 @Validated 注解来启用参数验证。
public class TranslateController extends ControllerBase{
    @Autowired
    TranslateService translateService;

    public TranslateController(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @GetMapping("/translate/baidu")
    @Operation(summary = "百度翻译接口")
    @PermitAll
//    @PreAuthorize("isAuthenticated()")
    public StdResult baidutTranslateText(
            @RequestParam String text)
//            @Valid
//            @Pattern(regexp = RegexPattern.fromTo, message = "from 格式错误")
//            @RequestParam String from,
//            @Valid
//            @Pattern(regexp = RegexPattern.FromTo, message = "to 格式错误")
//            @RequestParam String to)
           throws UserException {
        String from="auto";
        String to="zh";
        String s = translateService.baiduTranslate(text, from, to);

        if (s!=null){return ok(s);
        }else {return badRequest();} }



    @GetMapping("/translate/youdao")
    @Operation(summary = "有道翻译接口")
    @PermitAll
//    @PreAuthorize("isAuthenticated()")
    public StdResult youdaoTranslateText(
            @Valid
            @RequestParam String text
//            @Valid
//            @Pattern(regexp = RegexPattern.fromTo, message = "from 格式错误")
//            @RequestParam String from,
//            @Valid
//            @Pattern(regexp = RegexPattern.FromTo, message = "to 格式错误")
//            @RequestParam String to
    ) throws UserException {
        String from="auto";
        String to="zh";
        String s = translateService.youdaoTranslate(text, from, to);
        if (s!=null){return ok(s);
        }else {return badRequest();}
    }}


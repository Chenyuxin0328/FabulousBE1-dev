package com.creatorsn.fabulous.controller;

import com.creatorsn.fabulous.dto.ConfigCreateOrUpdateDTO;
import com.creatorsn.fabulous.dto.DataSourceCreateDTO;
import com.creatorsn.fabulous.dto.DataSourceUpdateDTO;
import com.creatorsn.fabulous.dto.StdResult;
import com.creatorsn.fabulous.entity.DataStruct;
import com.creatorsn.fabulous.entity.WikiConfig;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.service.ConfigService;
import com.creatorsn.fabulous.service.NoteBookService;
import com.creatorsn.fabulous.util.RegexPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 配置文件接口
 */
@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "ConfigController", description = "配置接口")
@Validated
public class ConfigController extends ControllerBase {

    final private ConfigService configService;

    final private NoteBookService noteBookService;

    final private Logger logger = LoggerFactory.getLogger(ConfigController.class);

    public ConfigController(
            ModelMapper modelMapper,
            ConfigService configService,
            NoteBookService noteBookService
    ) {
        super(modelMapper);
        this.configService = configService;
        this.noteBookService = noteBookService;
    }

    /**
     * 创建或者更新用户配置文件
     *
     * @param configCreateOrUpdateDTO 用户配置
     * @return 返回创建或者更新后的配置文件
     * @throws UserException 用户异常
     */
    @Operation(summary = "创建或者更新用户配置文件")
    @PostMapping("/configs")
    @PreAuthorize("isAuthenticated()")
    public StdResult createOrUpdateConfig(
            @RequestBody
            @Valid
            ConfigCreateOrUpdateDTO configCreateOrUpdateDTO
    ) throws UserException {
        var userId = getUserId();
        var config = modelMapper.map(configCreateOrUpdateDTO, WikiConfig.class);
        config.setUserId(userId);
        config = configService.createOrUpdateConfig(config);
        if (config == null) {
            return badRequest();
        }
        return ok(config);
    }

    /**
     * 获取用户的配置文件
     *
     * @return 返回用户的配置文件
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取用户的配置文件")
    @GetMapping("/me/config")
    @PreAuthorize("isAuthenticated()")
    public StdResult getConfig() throws UserException {
        String userId = getUserId();
        var config = configService.getConfig(userId);
        if (config == null) {
            return badRequest();
        }
        for (var dataPath : config.getDataPath()) {
            dataPath.setPath(dataPath.getId());
        }
        return ok(config);
    }

    /**
     * 获取指定数据源的信息
     *
     * @param id 数据源的Id
     * @return 返回数据源的信息
     * @throws UserException 用户异常
     */
    @Operation(summary = "获取数据源的信息")
    @GetMapping("/config/sources/{id}")
    @PreAuthorize("isAuthenticated()")
    public StdResult getSourceInfo(
            @PathVariable
            @Valid
            @Pattern(regexp = RegexPattern.GUID, message = "id 的格式错误")
            String id
    ) throws UserException {
        var source = configService.getDataStruct(id);
        if (source == null) return badRequest();
        return ok(source);
    }

    /**
     * 创建数据源
     *
     * @param dataSourceCreateDTO 数据源
     * @return 返回创建后的数据源
     * @throws UserException 用户异常
     */
    @Operation(summary = "创建数据源")
    @PostMapping("/configs/sources")
    @PreAuthorize("isAuthenticated()")
    public StdResult createDataSource(
            @RequestBody
            @Valid
            DataSourceCreateDTO dataSourceCreateDTO
    ) throws UserException {
        var userId = getUserId();
        var source = modelMapper.map(dataSourceCreateDTO, DataStruct.class);
        source.setUserId(userId);
        source = configService.createDataStruct(source);
        if (source == null) return badRequest();
        return ok(source);
    }

    /**
     * 更新数据源信息
     *
     * @param id                  数据源的Id
     * @param dataSourceUpdateDTO 更新的传输对象
     * @return 返回更新后的对象
     * @throws UserException 用户异常
     */
    @Operation(summary = "更新数据源的信息")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/configs/sources/{id}")
    public StdResult updateDataSource(
            @PathVariable
            @Valid
            @Pattern(regexp = RegexPattern.GUID, message = "id 格式错误")
            String id,
            @RequestBody
            @Valid
            DataSourceUpdateDTO dataSourceUpdateDTO
    ) throws UserException {
        var userId = getUserId();
        var source = modelMapper.map(dataSourceUpdateDTO, DataStruct.class);
        source = configService.updateDataStruct(source.setUserId(userId).setId(id));
        if (source == null) return badRequest();
        return ok(source);
    }

    /**
     * 删除指定的数据源
     *
     * @param id 数据源的Id
     * @return 如果删除成功返回ok，否则bad request
     * @throws UserException 用户异常
     */
    @Operation(summary = "删除数据源")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/configs/sources/{id}")
    public StdResult removeDataSource(
            @PathVariable
            @Valid
            @Pattern(regexp = RegexPattern.GUID, message = "id 格式错误")
            String id
    ) throws UserException {
        var userId = getUserId();
        if (!configService.deleteDataStruct(id, userId)) return badRequest();
        return ok();
    }

    /**
     * 监听数据源的变化
     *
     * @param id 数据源的Id
     * @return 返回监视
     * @throws UserException 用户异常
     */
    @Operation(summary = "监听数据源的变化")
    @GetMapping("/configs/sources/{id}/chokidar")
    @PreAuthorize("isAuthenticated()")
    public SseEmitter watchDataSource(
            @PathVariable("id")
            @Valid
            @Pattern(regexp = RegexPattern.GUID, message = "id 格式错误")
            String id
    ) throws UserException {
        var userId = getUserId();
        var emitter = new SseEmitter(-1L);
        // 注册Emitter;
        noteBookService.subscribeDataSource(userId, id, emitter);
        return emitter;
    }
}

package com.creatorsn.fabulous.controller;

import com.creatorsn.fabulous.dto.*;
import com.creatorsn.fabulous.entity.SessionToken;
import com.creatorsn.fabulous.entity.User;
import com.creatorsn.fabulous.entity.UserGenderEnum;
import com.creatorsn.fabulous.entity.UserRole;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.mapper.UserMapper;
import com.creatorsn.fabulous.service.UserService;
import com.creatorsn.fabulous.util.RegexPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 */
@RestController
@Tag(name = "UserController", description = "用户的注册、登陆、信息获取、信息更新等接口")
@Validated
public class UserController extends ControllerBase {

    final UserService userService;

    public UserController(
            ModelMapper modelMapper,
            UserService userService
    ) {
        super(modelMapper);
        this.userService = userService;
    }

    /**
     * 用户登陆接口
     *
     * @param userLogin 用户登陆参数
     * @return 返回用户登陆凭据
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "用户登陆")
    @PostMapping("/users/session")
    @PermitAll
    public StdResult login(
            @RequestBody
            @Valid
            UserLogin userLogin
    ) throws UserException {
        SessionToken sessionToken = this.userService.login(userLogin.getId(), userLogin.getPassword(), userLogin.getCode());
        return ok(sessionToken);
    }

    /**
     * 向指定用户发送验证邮件
     *
     * @param id 指定用户的用户名、邮箱、手机号等等
     * @return 如果成功则返回Ok，否则返回BadRequest
     * @throws UserException 用户业务错误
     */
    @Operation(summary = "发送验证邮件")
    @PostMapping("/users/{id}/email/code")
    @PermitAll
    public StdResult sendVerifiedEmail(
            @Valid
            @PathVariable("id")
            String id
    ) throws UserException {
        if (this.userService.sendVerifiedCode(id)) {
            return ok();
        }
        return badRequest();
    }

    /**
     * 发送忘记密码的邮件
     *
     * @param id 用户的Id
     * @return 如果成功则返回Ok，否则返回BadRequest
     * @throws UserException 用户业务错误
     */
    @Operation(summary = "发送忘记密码邮件")
    @PostMapping("/users/{id}/forgot/code")
    @PermitAll
    public StdResult sendForgotVerifiedEmail(
            @Valid
            @PathVariable("id")
            String id
    ) throws UserException {
        if (this.userService.sendForgotVerifiedCode(id)) {
            return ok();
        }
        return badRequest();
    }


    /**
     * 用户修改密码
     *
     * @param id                 用户的Id
     * @param userUpdatePassword 用户修改密码的参数
     * @return 如果成功则返回Ok，否则返回BadRequest
     * @throws UserException 用户业务错误
     */
    @Operation(summary = "用户修改密码")
    @PostMapping("/users/{id}/password")
    @PermitAll
    public StdResult updatePassword(
            @Valid
            @PathVariable("id")
            String id,
            @Valid
            @RequestBody
            UserUpdatePassword userUpdatePassword
    ) throws UserException {
        if (this.userService.updatePassword(id, userUpdatePassword.getPassword(), userUpdatePassword.getCode())) {
            return ok();
        }
        return badRequest();
    }

    /**
     * 用户注册
     *
     * @param userRegister 用户注册类
     * @return 返回注册后的用户
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "用户注册")
    @PostMapping("/users")
    @PermitAll
    public StdResult register(
            @RequestBody
            @Valid
            UserRegister userRegister) throws UserException {
        User user = this.modelMapper.map(userRegister, User.class);
        user = this.userService.register(user);
        return ok(this.modelMapper.map(user, UserRegisterResponse.class));
    }

    /**
     * 获取我的注册信息
     *
     * @return 返回我的信息
     * @throws UserException 用户输入错误
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "我的信息")
    @GetMapping("/users/me/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult getMyUserInfo() throws UserException {
        String userId = getUserId();
        if (userId != null) {
            User user = this.userService.getUserById(userId);
            UserInfo userInfo = this.modelMapper.map(user, UserInfo.class);
            return ok(userInfo);
        }
        return badRequest();
    }

    /**
     * 更新我的信息
     *
     * @param userUpdate 更新我的信息的参数
     * @return 如果成功则返回Ok，否则返回BadRequest
     * @throws UserException 用户输入错误
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "更新我的信息")
    @PostMapping("/users/me/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateMyUserInfo(
            @RequestBody
            @Valid
            UserUpdate userUpdate
    ) throws UserException {
        var userId = getUserId();
        if (userId != null) {
            User user = this.modelMapper.map(userUpdate, User.class);
            user.setId(userId);
            if (this.userService.updateUserInfoById(user))
                return ok();
        }
        return badRequest();
    }

    /**
     * 获取我的BASE64头像
     *
     * @return 返回我的头像
     * @throws UserException 用户输入错误
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "获取我的头像")
    @GetMapping("/users/me/avatar")
    @PreAuthorize("isAuthenticated()")
    public StdResult getMyAvatar() throws UserException {
        var userId = getUserId();
        if (userId != null) {
            String avatar = this.userService.getUserAvatarById(userId);
            return ok(avatar);
        }
        return badRequest();
    }

    /**
     * 更新我的头像
     *
     * @param request 更新我的头像的参数
     * @return 如果成功则返回Ok，否则返回BadRequest
     * @throws UserException 用户输入错误
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "更新我的头像")
    @PostMapping("/users/me/avatar")
    @PreAuthorize("isAuthenticated()")
    public StdResult updateMyAvatar(
            @RequestBody
            @Valid
            UpdateAvatarRequest request
    ) throws UserException {
        var userId = getUserId();
        if (userId != null) {
            if (this.userService.updateUserAvatarById(userId, request.getAvatar())) {
                return ok();
            }
        }
        return badRequest();
    }

    /**
     * 获取指定用户的头像
     *
     * @param id 指定用户的Id
     * @return 返回指定用户的头像
     * @throws UserException 用户输入错误
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "获取指定用户的头像")
    @GetMapping("/users/{id}/avatar")
    @PreAuthorize("isAuthenticated()")
    public StdResult getAvatar(
            @PathVariable("id")
            @Valid
            @Pattern(regexp = RegexPattern.GUID, message = "用户的Id格式错误")
            String id
    ) throws UserException {
        if (id != null) {
            String avatar = this.userService.getUserAvatarById(id);
            return ok(avatar);
        }
        return badRequest();
    }

    /**
     * 获取用户信息
     *
     * @param query    查询关键字
     * @param gender   性别
     * @param offset   偏移量
     * @param pageSize 页大小
     * @param sortKey  排序字段
     * @return 返回用户信息
     * @throws UserException 用户输入错误
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "查询用户信息")
    @GetMapping("/users/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult getUsers(
            @RequestParam(required = false)
            String query,
            @RequestParam(required = false, defaultValue = "SECRET")
            UserGenderEnum gender,
            @RequestParam(required = false, defaultValue = "0")
            @Schema(defaultValue = "0")
            long offset,
            @Schema(defaultValue = "20")
            @RequestParam(required = false, defaultValue = "20")
            int pageSize,
            @Schema(defaultValue = "id")
            @RequestParam(required = false, defaultValue = "id")
            UserMapper.SortKey sortKey
    ) throws UserException {
        // TODO: 数据脱敏
        List<User> user = this.userService.getUsers(query, gender, offset, pageSize, sortKey);
        List<UserInfo> userInfos = user.stream().map(u -> this.modelMapper.map(u, UserInfo.class)).collect(Collectors.toList());
        return ok(userInfos);
    }

    /**
     * 根据用户的Id获取用户的信息
     *
     * @param userId 用户的Id
     * @return 返回指定用户的用户信息
     * @throws UserException 用户异常
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "获取用户的信息")
    @GetMapping("/users/{userId}/info")
    @PreAuthorize("isAuthenticated()")
    public StdResult getUserById(
            @PathVariable
            @Pattern(regexp = RegexPattern.GUID, message = "用户的Id格式错误")
            String userId
    ) throws UserException {
        var user = userService.getUserById(userId);
        if (user == null) return badRequest();
        var info = modelMapper.map(user, UserInfo.class);
        return ok(info);
    }

    /**
     * 根据用户的Id列表获取用户的信息
     *
     * @param userIds 用户的Id列表
     * @return 返回指定用户的用户信息
     * @throws UserException 用户异常
     */
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "获取用户的Id")
    @GetMapping("/users/batched/{userIds}/info")
    public StdResult getUserByIds(
            @PathVariable
            @Pattern(regexp = RegexPattern.GUIDList, message = "用户的Id列表格式错误，用,间隔")
            String userIds
    ) throws UserException {
        var userIdsList = userIds.split(",");
        var users = userService.getUsersByIds(Arrays.stream(userIdsList).toList());
        var userDTOs = users.stream().map(u -> modelMapper.map(u, UserInfo.class)).toList();
        return ok(userDTOs);
    }

    /**
     * 获取指定用户的角色
     *
     * @param id 指定用户的Id
     * @return 返回指定用户的角色
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "获取指定用户的角色")
    @GetMapping("/users/{id}/roles")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('admin')")
    public StdResult getUserRoles(
            @PathVariable("id")
            @Valid
            @Pattern(regexp = RegexPattern.GUID, message = "用户的Id格式错误")
            String id
    ) throws UserException {
        if (id != null) {
            List<UserRole> roles = this.userService.getUserRoles(id);
            return ok(roles);
        }
        return badRequest();
    }

    /**
     * 获取我的角色
     *
     * @return 返回我的角色
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "获取我的角色")
    @GetMapping("/users/me/roles")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("isAuthenticated()")
    public StdResult getMyRoles() throws UserException {
        var userId = getUserId();
        if (userId != null) {
            List<UserRole> roles = this.userService.getUserRoles(userId);
            return ok(roles);
        }
        return badRequest();
    }

    /**
     * 获取所有的角色
     *
     * @param query    查询关键字
     * @param offset   偏移量
     * @param pageSize 页大小
     * @return 返回所有的角色
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "获取所有的角色")
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/users/roles")
    @PreAuthorize("hasRole('admin')")
    public StdResult getAllRoles(
            @RequestParam(required = false)
            String query,
            @RequestParam(required = false, defaultValue = "0")
            long offset,
            @RequestParam(required = false, defaultValue = "20")
            int pageSize
    ) throws UserException {
        return ok(this.userService.getAllUserRoles(query, offset, pageSize));
    }

    /**
     * 创建角色
     *
     * @param roleRequest 创建角色的参数
     * @return 返回创建的角色
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "创建角色")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/users/roles")
    @PreAuthorize("hasRole('admin')")
    public StdResult createRole(
            @RequestBody
            @Valid
            RoleRequest roleRequest
    ) throws UserException {
        var entity = this.modelMapper.map(roleRequest, UserRole.class);
        return ok(this.userService.addRole(entity));
    }

    /**
     * 更新角色
     *
     * @param roleId 角色的Id
     * @return 返回更新后的角色
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "删除角色")
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/users/roles/{roleId}")
    @PreAuthorize("hasRole('admin')")
    public StdResult deleteRole(
            @PathVariable("roleId")
            @Pattern(regexp = RegexPattern.GUID, message = "角色的Id格式错误")
            String roleId
    ) throws UserException {
        return ok(this.userService.removeRole(roleId));
    }

    /**
     * 为用户添加角色
     *
     * @param userId 用户的Id
     * @param roleId 角色的Id
     * @return 如果成功则返回Ok，否则返回BadRequest
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "为用户添加角色")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('admin')")
    public StdResult addRoleToUser(
            @PathVariable("userId")
            @Pattern(regexp = RegexPattern.GUID, message = "用户的Id格式错误")
            String userId,
            @Pattern(regexp = RegexPattern.GUID, message = "角色的Id格式错误")
            @PathVariable("roleId")
            String roleId
    ) throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creator = authentication.getPrincipal().toString();
        if (this.userService.addUserRole(userId, roleId, creator))
            return ok();
        return badRequest();
    }

    /**
     * 为用户删除角色
     *
     * @param userId 用户的Id
     * @param roleId 角色的Id
     * @return 如果成功则返回Ok，否则返回BadRequest
     * @throws UserException 用户输入错误
     */
    @Operation(summary = "为用户删除角色")
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('admin')")
    public StdResult removeRoleFromUser(
            @PathVariable("userId")
            @Pattern(regexp = RegexPattern.GUID, message = "用户的Id格式错误")
            String userId,
            @Pattern(regexp = RegexPattern.GUID, message = "角色的Id格式错误")
            @PathVariable("roleId")
            String roleId
    ) throws UserException {
        if (this.userService.removeUserRole(userId, roleId, null))
            return ok();
        return badRequest();
    }

}

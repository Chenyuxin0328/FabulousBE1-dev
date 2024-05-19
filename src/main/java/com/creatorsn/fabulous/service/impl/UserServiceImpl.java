package com.creatorsn.fabulous.service.impl;

import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.entity.SessionToken;
import com.creatorsn.fabulous.entity.User;
import com.creatorsn.fabulous.entity.UserGenderEnum;
import com.creatorsn.fabulous.entity.UserRole;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.mapper.RoleMapper;
import com.creatorsn.fabulous.mapper.UserMapper;
import com.creatorsn.fabulous.mapper.UserRoleMapper;
import com.creatorsn.fabulous.service.EmailService;
import com.creatorsn.fabulous.service.UserService;
import com.creatorsn.fabulous.util.HashUtil;
import com.creatorsn.fabulous.util.JsonWebTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务类实现
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final HashUtil hashUtil;

    private final JsonWebTokenUtil jsonWebTokenUtil;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final EmailService emailService;

    private final RoleMapper roleMapper;

    private final UserRoleMapper userRoleMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
            UserMapper userMapper,
            HashUtil hashUtil,
            JsonWebTokenUtil jsonWebTokenUtil,
            RedisTemplate<Object, Object> redisTemplate,
            EmailService emailService,
            RoleMapper roleMapper,
            UserRoleMapper userRoleMapper
    ) {
        this.userMapper = userMapper;
        this.hashUtil = hashUtil;
        this.jsonWebTokenUtil = jsonWebTokenUtil;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * @param user 要注册的用户
     * @return 返回成功注册的用户，否则返回null
     */
    @Override
    public User register(User user) throws UserException {
        // 首先判断这个用户是否被注册过
        if (user.getName() != null && userMapper.exists(user.getName())) {
            throw new UserException(StatusCode.UserExists);
        }
        if (user.getEmail() != null && userMapper.exists(user.getEmail())) {
            throw new UserException(StatusCode.EmailExists);
        }
        if (user.getPhone() != null && userMapper.exists(user.getPhone())) {
            throw new UserException(StatusCode.PhoneExists);
        }
        user.setPassword(hashUtil.encode(user.getPassword()));
        user.setUpdateDate(OffsetDateTime.now());
        user.setCreateDate(OffsetDateTime.now());
        user.setGender(UserGenderEnum.SECRET);
        user.setBirth(OffsetDateTime.now());
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        // 生成随机的用户Id
        user.setId(UUID.randomUUID().toString().toUpperCase());
        user = userMapper.add(user);
        return user;
    }

    /**
     * @param id       用户的用户名或者邮箱
     * @param password 用户的未加密的秘密
     * @return 返回令牌
     * @throws UserException 用户异常
     */
    @Override
    public SessionToken login(String id, String password, String code) throws UserException {
        if (id != null && !userMapper.exists(id)) {
            throw new UserException(StatusCode.UserNotFound);
        }
        String databasePassword = userMapper.getPasswordHash(id);
        if (!hashUtil.verify(password, databasePassword)) {
            throw new UserException(StatusCode.PasswordError);
        }
        // 生成对应的JWT Token
        User user = userMapper.getUserById(id);
        // 判断用户邮箱是否已经验证，如果没有验证则需要验证
        if (!user.isEmailVerified() && !StringUtils.hasText(code)) {
            throw new UserException(StatusCode.EmailNotVerified);
        }
        if (StringUtils.hasText(code)) {
            // 检测验证码是否正确
            var verifiedCode = redisTemplate.opsForValue().get("email_verified_code:" + user.getEmail());
            if (verifiedCode == null || !verifiedCode.equals(code)) {
                throw new UserException(StatusCode.CodeError);
            }

            // 更新用户的邮箱验证状态
            var updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setEmailVerified(true);
            if (!userMapper.updateUserInfoById(updateUser)) {
                throw new UserException(StatusCode.UpdateEmailVerifiedError);
            } else {
                // 验证码正确，删除验证码
                redisTemplate.delete("email_verified_code:" + user.getEmail());
            }
        }
        // TODO: 限制验证次数，以及密码登陆次数
        var roles = userMapper.getUserRoles(user.getId());
        var roleNames = roles.stream().map(UserRole::getName).toList();
        var claims = new HashMap<String, Object>();
        claims.put("roles", roleNames);
        String accessToken = this.jsonWebTokenUtil.createToken(user.getId(), claims, 365 * 24 * 60);
        OffsetDateTime datetime = OffsetDateTime.now().plusDays(365);
        // 更新登陆的时间
        {
            var updatedUser = new User();
            updatedUser.setId(user.getId());
            updatedUser.setLastLoginAt(OffsetDateTime.now());
            userMapper.updateUserInfoById(updatedUser);
        }
        // TODO: 更新用户登陆的IP
        return new SessionToken().setAccessToken(accessToken)
                .setSchema("Bearer").setExpiredAt(datetime);
    }

    @Override
    public boolean updatePassword(String id, String password, String code) throws UserException {
        if (code == null) throw new UserException(StatusCode.CodeEmpty);
        else {
            var user = userMapper.getUserById(id);
            if (user == null) {
                throw new UserException(StatusCode.UserNotFound);
            }
            // TODO: 限制验证次数防止爆破
            var verifiedCode = redisTemplate.opsForValue().get("email_forgot_verified_code:" + user.getEmail());
            if (verifiedCode == null || !verifiedCode.equals(code)) {
                throw new UserException(StatusCode.CodeError);
            }
            // 验证码正确，删除验证码
            redisTemplate.delete("email_forgot_verified_code:" + user.getEmail());
            // 更新用户的密码
            var updatedUser = new User();
            updatedUser.setId(user.getId());
            updatedUser.setPassword(hashUtil.encode(password));
            return userMapper.updateUserInfoById(updatedUser);
        }
    }

    /**
     * @param id 用户的Id
     * @return 返回查找的用户
     * @throws UserException 用户异常
     */
    @Override
    public User getUserById(String id) throws UserException {
        return this.userMapper.getUserById(id);
    }

    /**
     * @param ids 用户的Id列表
     * @return 返回符合条件的用户
     * @throws UserException 用户异常
     */
    @Override
    public List<User> getUsersByIds(List<String> ids) throws UserException {
        if (ids.isEmpty()) return new ArrayList<>();
        return userMapper.getUsersByIds(ids);
    }

    /**
     * @param id 用户的Id
     * @return 返回用户的头像
     * @throws UserException 用户输入异常
     */
    @Override
    public String getUserAvatarById(String id) throws UserException {
        return this.userMapper.getUserAvatarById(id);
    }

    /**
     * @param id     用户的Id
     * @param avatar 用户的BASE64头像
     * @return 如果修改成功则返回true，否则返回false
     * @throws UserException 用户输入异常
     */
    @Override
    public boolean updateUserAvatarById(String id, String avatar) throws UserException {
        return this.userMapper.updateUserAvatarById(id, avatar);
    }

    /**
     * 更新用户的信息
     *
     * @param user 用户类
     * @return 如果更新成功返回true，否则返回false
     * @throws UserException 用户输入异常
     */
    @Override
    public boolean updateUserInfoById(User user) throws UserException {
        if (user.getId() == null || !this.userMapper.exists(user.getId())) {
            throw new UserException(StatusCode.UserNotFound);
        }
        return this.userMapper.updateUserInfoById(user);
    }

    /**
     * 发送用户邮箱验证码
     *
     * @param id 用户的Id
     * @return 如果成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    @Override
    public boolean sendVerifiedCode(String id) throws UserException {
        if (id == null || !this.userMapper.exists(id)) {
            throw new UserException(StatusCode.UserNotFound);
        }
        var user = this.userMapper.getUserById(id);
        if (user.isEmailVerified()) {
            throw new UserException(StatusCode.EmailAlreadyVerified);
        }
        // 检测是否验证码仍然有效
        var code = redisTemplate.opsForValue().get("email_verified_code:" + user.getEmail());
        if (code == null) {
            code = String.valueOf(new Random().nextInt(899999) + 100000);
            var counter = emailService.getEmailUsedCounter(OffsetDateTime.now().plusDays(-30), OffsetDateTime.now().plusDays(1));
            if (counter.getEmailUseds().isEmpty()) {
                logger.error("当前未注册系统邮箱");
                return false;
            }
            // 获取使用最少次数的邮箱，比较暴力的方法
            var emailUsed = counter.getEmailUseds().get(0);
            var variables = new HashMap<String, String>();
            variables.put("User", user.getName());
            variables.put("code", code.toString());
            variables.put("email", user.getEmail());
            if (emailService.sendEmail(
                    emailUsed.getId(),
                    user.getEmail(),
                    "registerCode",
                    variables
            )) {
                redisTemplate.opsForValue().set("email_verified_code:" + user.getEmail(), code, 5, TimeUnit.MINUTES);
                return true;
            }
        } else {
            throw new UserException(StatusCode.CodeExists);
        }
        return false;
    }

    @Override
    public boolean sendForgotVerifiedCode(String id) throws UserException {
        if (id == null || !this.userMapper.exists(id)) {
            throw new UserException(StatusCode.UserNotFound);
        }
        var user = this.userMapper.getUserById(id);
        // 检测是否验证码仍然有效
        var code = redisTemplate.opsForValue().get("email_forgot_verified_code:" + user.getEmail());
        if (code == null) {
            code = String.valueOf(new Random().nextInt(899999) + 100000);
            var counter = emailService.getEmailUsedCounter(OffsetDateTime.now().plusDays(-30), OffsetDateTime.now().plusDays(1));
            if (counter.getEmailUseds().isEmpty()) {
                logger.error("当前未注册系统邮箱");
                return false;
            }
            // 获取使用最少次数的邮箱，比较暴力的方法
            var emailUsed = counter.getEmailUseds().get(0);
            var variables = new HashMap<String, String>();
            variables.put("User", user.getName());
            variables.put("code", code.toString());
            variables.put("email", user.getEmail());
            if (emailService.sendEmail(
                    emailUsed.getId(),
                    user.getEmail(),
                    "forgotCode",
                    variables
            )) {
                redisTemplate.opsForValue().set("email_forgot_verified_code:" + user.getEmail(), code, 5, TimeUnit.MINUTES);
                return true;
            }
        } else {
            throw new UserException(StatusCode.CodeExists);
        }
        return false;
    }

    @Override
    public List<User> getUsers(String query, UserGenderEnum gender, long offset, int pageSize, UserMapper.SortKey sortKey) throws UserException {
        offset = Math.max(0, offset);
//        限制10000条防止被爆破
        pageSize = Math.max(1, Math.min(10000, pageSize));
        return this.userMapper.getUsersOrderByKey(query, gender, offset, pageSize, sortKey);
    }

    @Override
    public UserRole addRole(UserRole role) throws UserException {
        if (this.roleMapper.exists(role.getName())) {
            throw new UserException(StatusCode.RoleExists);
        }
        role.setId(UUID.randomUUID().toString().toUpperCase());
        return this.roleMapper.add(role);
    }

    @Override
    public boolean removeRole(String roleId) throws UserException {
        if (!this.roleMapper.exists(roleId)) {
            throw new UserException(StatusCode.RoleNotFound);
        }
        return this.roleMapper.delete(roleId);
    }

    @Override
    public List<UserRole> getAllUserRoles(String query, long offset, int pageSize) {
        return this.roleMapper.list(query, offset, pageSize);
    }

    @Override
    public List<UserRole> getUserRoles(String userId) throws UserException {
        if (!userMapper.exists(userId)) {
            throw new UserException(StatusCode.UserNotFound);
        }
        return this.userMapper.getUserRoles(userId);
    }

    @Override
    public boolean addUserRole(String userId, String roleId, String creator) throws UserException {
        if (!userMapper.exists(userId)) {
            throw new UserException(StatusCode.UserNotFound);
        }
        if (!roleMapper.exists(roleId)) {
            throw new UserException(StatusCode.RoleNotFound);
        }
        return this.userRoleMapper.addUserRole(userId, roleId, creator);
    }

    @Override
    public boolean removeUserRole(String userId, String roleId, String creator) throws UserException {
        if (!userMapper.exists(userId)) {
            throw new UserException(StatusCode.UserNotFound);
        }
        if (!roleMapper.exists(roleId)) {
            throw new UserException(StatusCode.RoleNotFound);
        }
        return this.userRoleMapper.removeUserRole(userId, roleId, creator);
    }

}

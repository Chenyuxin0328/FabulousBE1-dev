package com.creatorsn.fabulous.service;

import com.creatorsn.fabulous.entity.SessionToken;
import com.creatorsn.fabulous.entity.User;
import com.creatorsn.fabulous.entity.UserGenderEnum;
import com.creatorsn.fabulous.entity.UserRole;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务类的接口
 */
@Service
public interface UserService {

    /**
     * 注册用户
     *
     * @param user 要注册的用户
     * @return 返回注册的用户
     * @throws UserException 用户异常
     */
    User register(User user) throws UserException;

    /**
     * 用户登陆
     *
     * @param id       用户的用户名或者邮箱
     * @param password 用户的未加密的秘密
     * @param code     用户邮箱验证码
     * @return 登陆的令牌
     * @throws UserException 用户异常
     */
    SessionToken login(String id, String password, String code) throws UserException;

    /**
     * 根据验证码修改密码
     *
     * @param id       用户的Id
     * @param password 用户的密码
     * @param code     验证码
     * @return 如果修改成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean updatePassword(String id, String password, String code) throws UserException;

    /**
     * 根据用户的Id获取用户的资料
     *
     * @param id 用户的Id
     * @return 返回查找的用户，如果没有查找到则返回null
     * @throws UserException 用户异常
     */
    User getUserById(String id) throws UserException;

    /**
     * 根据用户的Ids获取指定用户的资料
     *
     * @param ids 用户的Id列表
     * @return 返回符合条件的用户列表
     * @throws UserException 用户异常
     */
    List<User> getUsersByIds(List<String> ids) throws UserException;

    /**
     * 获取用户的头像
     *
     * @param id 用户的Id
     * @return 返回用户的头像BASE64
     * @throws UserException 用户异常
     */
    String getUserAvatarById(String id) throws UserException;

    /**
     * 更新用户的头像
     *
     * @param id     用户的Id
     * @param avatar 用户的BASE64头像
     * @return 如果更新成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean updateUserAvatarById(String id, String avatar) throws UserException;

    /**
     * 更新用户的信息
     *
     * @param user 用户类
     * @return 如果更新成功返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean updateUserInfoById(User user) throws UserException;

    /**
     * 向指定的用户发送邮箱验证码
     *
     * @param id 用于的Id
     * @return 如果成功返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean sendVerifiedCode(String id) throws UserException;

    /**
     * 向指定的用户发送忘记密码的邮箱验证码
     *
     * @param id 用户的Id
     * @return 如果成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean sendForgotVerifiedCode(String id) throws UserException;


    /**
     * 获取用户的信息
     *
     * @param query    查询关键字（可查询ID、用户名、昵称、邮箱、手机号码）
     * @param gender   性别
     * @param offset   偏移量
     * @param pageSize 页的大小
     * @param sortKey  排序关键字
     * @return 获取到的用户
     * @throws UserException 用户异常
     */
    List<User> getUsers(String query, UserGenderEnum gender, long offset, int pageSize, UserMapper.SortKey sortKey) throws UserException;

    /**
     * 添加用户角色
     *
     * @param role 用户角色
     * @return 添加的用户角色
     */
    UserRole addRole(UserRole role) throws UserException;

    /**
     * 删除用户角色
     *
     * @param roleId 用户角色的Id
     * @return 如果删除成功则返回true，否则返回false
     */
    boolean removeRole(String roleId) throws UserException;

    /**
     * 获取系统所有的用户角色
     *
     * @param query    查询关键字
     * @param offset   偏移量
     * @param pageSize 页的大小
     * @return 获取到的用户角色
     */
    List<UserRole> getAllUserRoles(String query, long offset, int pageSize) throws UserException;

    /**
     * 获取用户角色
     *
     * @param userId 用户的Id
     * @return 获取到的用户角色
     */
    List<UserRole> getUserRoles(String userId) throws UserException;

    /**
     * 给对应的用户添加角色
     *
     * @param userId  用户的Id
     * @param roleId  角色的Id
     * @param creator 创建者的Id
     * @return 如果添加成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean addUserRole(String userId, String roleId, String creator) throws UserException;

    /**
     * 删除用户的角色
     *
     * @param userId  用户的Id
     * @param roleId  角色的Id
     * @param creator 创建者的Id
     * @return 如果删除成功则返回true，否则返回false
     * @throws UserException 用户异常
     */
    boolean removeUserRole(String userId, String roleId, String creator) throws UserException;


}

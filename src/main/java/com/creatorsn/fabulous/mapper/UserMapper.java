package com.creatorsn.fabulous.mapper;


import com.creatorsn.fabulous.entity.User;
import com.creatorsn.fabulous.entity.UserGenderEnum;
import com.creatorsn.fabulous.entity.UserRole;
import com.creatorsn.fabulous.util.RegexPattern;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户仓库接口
 */
@Mapper
public interface UserMapper {

    String table = "QUser";

    /**
     * 插入新的用户
     *
     * @param user 创建的用户
     * @return 返回真或者假
     */
    @SelectProvider(type = UserMapperProvider.class, method = "add")
    User add(User user);

    /**
     * 判断该用户名或者邮箱或者手机号是否存在
     *
     * @param id 对应的Id、用户名、邮箱、手机号
     * @return 如果存在返回真、否则返回假
     */
    @SelectProvider(type = UserMapperProvider.class, method = "exists")
    boolean exists(String id);

    /**
     * 获取对应用户的密码
     *
     * @param id 用户名或者邮箱、手机号
     * @return 对应用户密码的HASH值
     */
    @SelectProvider(type = UserMapperProvider.class, method = "getPasswordHash")
    String getPasswordHash(String id);

    /**
     * 根据用户的Id获取指定的用户
     *
     * @param id 用户的Id
     * @return 返回查询的用户
     */
    @SelectProvider(type = UserMapperProvider.class, method = "getUserById")
    User getUserById(String id);

    /**
     * 根据用户的Id列表获取用户列表
     *
     * @param ids 用户的Ids
     * @return 返回指定的用户
     */
    @SelectProvider(type = UserMapperProvider.class, method = "getUsersByIds")
    List<User> getUsersByIds(List<String> ids);

    /**
     * 根据用户的Id获取用户的BASE64头像
     *
     * @param id 用户的Id
     * @return 返回用户的头像
     */
    @SelectProvider(type = UserMapperProvider.class, method = "getUserAvatar")
    String getUserAvatarById(String id);

    /**
     * 设置用户的头像
     *
     * @param id     用户的Id
     * @param avatar 用户头像BASE64
     * @return 如果成功返回true，否则返回false
     */
    @Update("update " + table + " set avatar=#{avatar} where id=#{id} and deleteDate is null")
    boolean updateUserAvatarById(String id, String avatar);

    /**
     * 更新用户的信息
     *
     * @param user 用户类
     * @return 如果成功返回true，否则返回false
     */
    @UpdateProvider(type = UserMapperProvider.class, method = "updateUserInfoByProviderSQL")
    boolean updateUserInfoById(User user);

    /**
     * 获取用户的信息
     *
     * @param query    用户名、Id、昵称、邮箱、手机号查找关键字
     * @param gender   用户的性别
     * @param offset   偏移量
     * @param pageSize 页数大小
     * @param key      排序的关键字
     * @return 返回获取的用户列表
     */
    @SelectProvider(type = UserMapperProvider.class, method = "queryUsersOrderByKey")
    List<User> getUsersOrderByKey(String query, UserGenderEnum gender, long offset, int pageSize, SortKey key);

    /**
     * 获取用户的信息
     *
     * @param query    用户名、Id、昵称、邮箱、手机号查找关键字
     * @param gender   用户的性别
     * @param offset   偏移量
     * @param pageSize 页数大小
     * @return 返回获取的用户列表
     */
    @SelectProvider(type = UserMapperProvider.class, method = "queryUsers")
    List<User> getUsers(String query, UserGenderEnum gender, long offset, int pageSize);

    /**
     * 获取用户所有的角色
     *
     * @param userId 用户的唯一标识
     * @return 返回用户的角色列表
     */
    @SelectProvider(type = UserMapperProvider.class, method = "getUserRoles")
    List<UserRole> getUserRoles(String userId);

    /**
     * 列出指定Id的用户
     *
     * @param userIds 用户的Id
     * @return 返回指定的用户信息
     */
    @SelectProvider(type = UserMapperProvider.class, method = "list")
    List<User> list(List<String> userIds);

    enum SortKey {
        id,
        email,
        phone,
        username,
        createDate,
        updateDate,
        nickname
    }

    class UserMapperProvider {

        public String add(User user) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            if (StringUtils.hasText(user.getName()))
                sql.VALUES("name", "#{name}");
            sql.VALUES("password", "#{password}");
            sql.VALUES("nickname", "#{nickName}");
            sql.VALUES("email", "#{email}");
            sql.VALUES("emailVerified", "0");
            if (StringUtils.hasText(user.getPhone())) {
                sql.VALUES("phone", "#{phone}");
                sql.VALUES("phoneVerified", "0");
            }
            sql.VALUES("gender", "#{gender}");
            if (StringUtils.hasText(user.getIdcard())) {
                sql.VALUES("idcard", "#{idcard}");
            }
            if (user.getBirth() != null)
                sql.VALUES("birth", "#{birth}");
            else
                sql.VALUES("birth", "getdate()");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            return sql.toString();
        }

        public String updateUserInfoByProviderSQL(User user) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            if (StringUtils.hasText(user.getName())) {
                sql.SET("name=#{name}");
            }
            if (StringUtils.hasText(user.getNickName())) {
                sql.SET("nickname=#{nickName}");
            }
            if (StringUtils.hasText(user.getEmail())) {
                sql.SET("email=#{email},emailVerified=0");
            } else if (user.isEmailVerified()) {
                sql.SET("emailVerified=1");
            }
            if (StringUtils.hasText(user.getPhone())) {
                sql.SET("phone=#{phone},phoneVerified=0");
            } else if (user.isPhoneVerified()) {
                sql.SET("phoneVerified=1");
            }
            if (user.getBirth() != null) {
                sql.SET("birth=#{birth}");
            }
            if (user.getGender() != UserGenderEnum.SECRET) {
                sql.SET("gender=#{gender}");
            }
            if (StringUtils.hasText(user.getIdcard())) {
                sql.SET("idcard=#{idcard}");
            }
            if (user.getLastLoginAt() != null) {
                sql.SET("lastLoginAt=#{lastLoginAt}");
            }
            if (user.getPassword() != null) {
                sql.SET("password=#{password}");
            }
            sql.SET("updateDate=getdate()");
            return sql.toString();
        }

        public String exists(String id) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("top 1 1").EXISTS();
            whereId(id, sql);
            sql.WHERE(" deleteDate is null ");
            return sql.toString();
        }

        public String getPasswordHash(String id) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("top 1 password");
            whereId(id, sql);
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getUserById(String id) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("top 1 *");
            whereId(id, sql);
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getUsersByIds(List<String> ids) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("*");
            sql.WHERE("id in ('" + String.join(",", ids) + "')");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getUserAvatar(String id) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("top 1 avatar");
            whereId(id, sql);
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        private void whereId(String id, MSSQL sql) {
            if (Pattern.matches(RegexPattern.GUID, id)) {
                sql.WHERE("id=#{id}");
            } else if (Pattern.matches(RegexPattern.Email, id)) {
                sql.WHERE("email=#{id} ");
            } else {
                sql.WHERE("phone=#{id} ");
            }
        }

        public String queryUsers(String query, UserGenderEnum gender, long offset, int pageSize) {
            return queryUsersOrderByKey(query, gender, offset, pageSize, SortKey.id);
        }

        public String queryUsersOrderByKey(String query, UserGenderEnum gender, long offset, int pageSize, SortKey key) {
            var sql = new SQL();
            sql.SELECT("id", "name", "nickname", "email", "phone", "gender",
                    "createDate", "lastLoginAt", "birth", "idcard");
            sql.FROM(table);
            if (StringUtils.hasText(query)) {
                if (Pattern.matches(RegexPattern.GUID, query)) {
                    sql.WHERE("id=#{query}");
                } else {
                    sql.WHERE("(name like '%'+#{query}+'%' or nickname like '%'+#{query}+'%' " +
                            "or email like '%'+#{query}+'%' or phone like '%'+#{query}+'%') ");
                }
            }
            if (gender != UserGenderEnum.SECRET)
                sql.WHERE("gender=#{gender}");
            sql.WHERE("deleteDate is null");
            sql.OFFSET(offset);
            sql.FETCH_FIRST_ROWS_ONLY(pageSize);
            sql.ORDER_BY(key.name());
            return sql.toString();
        }

        public String getUserRoles(String id) {
            var sql = new SQL();
            sql.SELECT("r.*");
            sql.FROM(UserRoleMapper.table + " ur");
            sql.LEFT_OUTER_JOIN(RoleMapper.table + " r on ur.roleId=r.id");
            sql.WHERE("ur.userId=#{id}");
            return sql.toString();
        }

        public String list(List<String> userIds) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("deleteDate is null");
            sql.WHERE("id in ('" + String.join("','", userIds) + "')");
            return sql.toString();
        }
    }

}

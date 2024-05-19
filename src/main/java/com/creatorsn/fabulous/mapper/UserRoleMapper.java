package com.creatorsn.fabulous.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * 用户角色接口
 */
@Mapper
public interface UserRoleMapper {

    final String table = "QUserRole";

    public class UserRoleMapperProvider {

        public String addUserRole(String userId, String roleId, String creator) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.VALUES("userId", "#{userId}");
            sql.VALUES("roleId", "#{roleId}");
            if (creator != null) {
                sql.VALUES("creator", "#{creator}");
            }
            sql.VALUES("createDate", "getdate()");
            return sql.toString();
        }

        public String removeUserRole(String userId, String roleId, String creator) {
            var sql = new MSSQL();
            sql.DELETE_FROM(table);
            sql.WHERE("userId=#{userId}");
            sql.WHERE("roleId=#{roleId}");
            if (creator != null) {
                sql.WHERE("creator=#{creator}");
            }
            return sql.toString();
        }

        public String exists(String userId, String roleId) {
            var sql = new MSSQL();
            sql.SELECT("count(*)");
            sql.FROM(table);
            sql.WHERE("userId=#{userId}");
            sql.WHERE("roleId=#{roleId}");
            return sql.toString();
        }

    }

    /**
     * 为用户添加对应的角色
     *
     * @param userId  用户的唯一标识
     * @param roleId  角色的唯一标识
     * @param creator 创建者
     * @return 如果添加成功则返回true，否则返回false
     */
    @UpdateProvider(type = UserRoleMapperProvider.class, method = "addUserRole")
    public boolean addUserRole(String userId, String roleId, String creator);

    /**
     * 为用户移除对应的角色
     *
     * @param userId  用户的唯一标识
     * @param roleId  角色的唯一标识
     * @param creator 创建者
     * @return 如果移除成功则返回true，否则返回false
     */
    @UpdateProvider(type = UserRoleMapperProvider.class, method = "removeUserRole")
    public boolean removeUserRole(String userId, String roleId, String creator);

    /**
     * 判断用户是否拥有某个角色
     *
     * @param userId 用户的唯一标识
     * @param roleId 角色的唯一标识
     * @return 如果用户拥有该角色则返回true，否则返回false
     */
    @UpdateProvider(type = UserRoleMapperProvider.class, method = "exists")
    public boolean exists(String userId, String roleId);


}

package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.UserRole;
import com.creatorsn.fabulous.util.RegexPattern;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户角色接口
 */
@Mapper
public interface RoleMapper {

    final String table = "QRole";

    public class RoleMapperProvider {

        public String add(UserRole userRole) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            sql.VALUES("description", "#{description}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            return sql.toString();
        }

        public String update(UserRole userRole) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            if (userRole.getName() != null) {
                sql.SET("name=#{name}");
            }
            if (userRole.getDescription() != null)
                sql.SET("description=#{description}");
            sql.SET("updateDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String delete(String id) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.SET("deleteDate=getdate()");
            sql.WHERE("id=#{id}");
            return sql.toString();
        }

        public String exists(String id) {
            var sql = new MSSQL();
            sql.SELECT("count(*)");
            sql.FROM(table);
            if (Pattern.matches(RegexPattern.GUID, id)) {
                sql.WHERE("id=#{id}");
            } else {
                sql.WHERE("name=#{id}");
            }
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String get(String id) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            if (Pattern.matches(RegexPattern.GUID, id)) {
                sql.WHERE("id=#{id}");
            } else {
                sql.WHERE("name=#{id}");
            }
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String list(String query, long offset, int pageSize) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("deleteDate is null");
            if (StringUtils.hasText(query)) {
                sql.WHERE("name like '%'+#{query}+'%'");
            }
            sql.ORDER_BY("createDate desc");
            sql.OFFSET(offset);
            sql.FETCH_FIRST_ROWS_ONLY(pageSize);
            return sql.toString();
        }


    }

    /**
     * 添加用户角色
     *
     * @param userRole 用户角色
     * @return 返回创建的用户角色
     */
    @SelectProvider(type = RoleMapperProvider.class, method = "add")
    UserRole add(UserRole userRole);

    /**
     * 更新用户角色的部分属性
     *
     * @param userRole 要更新的用户角色
     * @return 返回是否更新成功
     */
    @UpdateProvider(type = RoleMapperProvider.class, method = "update")
    boolean update(UserRole userRole);

    /**
     * 删除某个用户角色
     *
     * @param id 用户角色的id
     * @return 返回是否删除成功
     */
    @UpdateProvider(type = RoleMapperProvider.class, method = "delete")
    boolean delete(String id);

    /**
     * 判断某个用户角色是否存在
     *
     * @param id 用户角色的id
     * @return 返回是否存在
     */
    @SelectProvider(type = RoleMapperProvider.class, method = "exists")
    boolean exists(String id);

    /**
     * 获取用户角色
     *
     * @param id 用户角色的Id
     * @return 返回用户角色
     */
    @SelectProvider(type = RoleMapperProvider.class, method = "get")
    UserRole get(String id);

    /**
     * 列出所有的用户角色
     *
     * @param query    查询的关键字
     * @param offset   偏移量
     * @param pageSize 页数的大小
     * @return 返回查询后的用户角色列表
     */
    @SelectProvider(type = RoleMapperProvider.class, method = "list")
    List<UserRole> list(String query, long offset, int pageSize);
}

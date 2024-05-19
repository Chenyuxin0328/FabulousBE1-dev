package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.WebsiteConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * 网站配置数据库映射
 */
@Mapper
public interface WebsiteConfigMapper {

    /**
     * 网站配置表名
     */
    static public String tableName = "w_config";

    /**
     * 网站配置数据库映射提供者
     */
    public class WebsiteConfigMapperProvider {

        /**
         * 创建网站配置
         *
         * @param websiteConfig 网站配置
         * @return SQL语句
         */
        public String create(WebsiteConfig websiteConfig) {
            var sql = new MSSQL();
            sql.INSERT_INTO(tableName);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            sql.VALUES("value", "#{value}");
            sql.VALUES("createdBy", "#{createdBy}");
            sql.VALUES("permission", "#{permission}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            return sql.toString();
        }

        /**
         * 获取网站配置
         *
         * @param name 网站配置名称
         * @return SQL语句
         */
        public String get(String name) {
            var sql = new MSSQL();
            sql.SELECT("top 1 *");
            sql.FROM(tableName);
            sql.WHERE("name = #{name}");
            sql.ORDER_BY("updateDate desc");
            return sql.toString();
        }

    }

    /**
     * 创建网站配置
     *
     * @param websiteConfig 网站配置
     * @return 创建后的网站配置
     */
    @SelectProvider(type = WebsiteConfigMapperProvider.class, method = "create")
    WebsiteConfig create(WebsiteConfig websiteConfig);

    /**
     * 获取网站配置
     *
     * @param id 网站配置id
     * @return 网站配置
     */
    @SelectProvider(type = WebsiteConfigMapperProvider.class, method = "get")
    WebsiteConfig get(String id);
}

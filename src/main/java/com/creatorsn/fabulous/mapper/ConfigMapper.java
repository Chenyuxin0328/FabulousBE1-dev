package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.WikiConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

@Mapper
public interface ConfigMapper {

    final String table = "QWikiConfig";


    public class UserWikiConfigMapperProvider {

        public String createConfig(WikiConfig config) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.VALUES("configId", "#{configId}");
            sql.VALUES("userId", "#{userId}");
            sql.VALUES("autoSave", "#{autoSave}");
            sql.VALUES("initStatus", "#{initStatus}");
            sql.VALUES("name", "#{name}");
            sql.VALUES("activeSystemMode", "#{activeSystemMode}");
            sql.VALUES("editorExpandContent", "#{editorExpandContent}");
            sql.VALUES("dynamicEffect", "#{dynamicEffect}");
            sql.VALUES("watchAllExtensions", "#{watchAllExtensions}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            if (StringUtils.hasText(config.getDataIndex()))
                sql.VALUES("dataIndex", "#{dataIndex}");
            sql.VALUES("theme", "#{theme}");
            if (StringUtils.hasText(config.getLanguage())) {
                sql.VALUES("language", "#{language}");
            }
            if (config.isAutoSave() == null) config.setAutoSave(false);
            if (config.isInitStatus() == null) config.setInitStatus(false);
            if (StringUtils.hasText(config.getLastLocalPath()))
                sql.VALUES("lastLocalPath", "#{lastLocalPath}");
            if (config.getActiveSystemMode() == null) config.setActiveSystemMode(WikiConfig.ConfigSystemMode.both);
            if (config.isEditorExpandContent() == null) config.setEditorExpandContent(false);
            if (config.isDynamicEffect() == null) config.setDynamicEffect(false);
            if (config.isWatchAllExtensions() == null) config.setWatchAllExtensions(false);
            if (config.getTheme() == null) config.setTheme(WikiConfig.ConfigTheme.light);
            sql.OUTPUT_INSERT("*");
            return sql.toString();
        }

        public String updateConfig(WikiConfig config) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (StringUtils.hasText(config.getName())) {
                sql.SET("name=#{name}");
            }
            if (config.getTheme() != null) {
                sql.SET("theme=#{theme}");
            }
            if (StringUtils.hasText(config.getLanguage())) {
                sql.SET("language=#{language}");
            }
            if (config.getDataPath() != null) {
                sql.SET("dataPath=#{dataPath}");
            }
            if (config.getDataIndex() != null) {
                sql.SET("dataIndex=#{dataIndex}");
            }
            if (config.isAutoSave() != null) {
                sql.SET("autoSave=#{autoSave}");
            }
            if (config.isDynamicEffect() != null) {
                sql.SET("dynamicEffect=#{dynamicEffect}");
            }
            if (config.isEditorExpandContent() != null) {
                sql.SET("editorExpandContent=#{editorExpandContent}");
            }
            if (config.isInitStatus() != null) {
                sql.SET("initStatus=#{initStatus}");
            }
            if (StringUtils.hasText(config.getLastLocalPath())) {
                sql.SET("lastLocalPath=#{lastLocalPath}");
            }
            if (config.isWatchAllExtensions() != null) {
                sql.SET("watchAllExtensions=#{watchAllExtensions}");
            }
            if (config.getActiveSystemMode() != null) {
                sql.SET("activeSystemMode=#{activeSystemMode}");
            }
            sql.SET("updateDate=getdate()");
            sql.WHERE("configId=#{configId}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getConfigByUserId(String userId) {
            var sql = new MSSQL();
            sql.SELECT("top 1 *");
            sql.FROM(table);
            sql.WHERE("userId=#{userId}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }
    }

    /**
     * 创建用户的Wiki配置
     *
     * @param config 创建配置文件
     * @return 返回用户创建的配置
     */
    @SelectProvider(type = UserWikiConfigMapperProvider.class, method = "createConfig")
    WikiConfig create(WikiConfig config);

    /**
     * 更新用户的Wiki配置
     *
     * @param config 更新配置文件
     * @return 返回用户更新的配置
     */
    @SelectProvider(type = UserWikiConfigMapperProvider.class, method = "updateConfig")
    WikiConfig update(WikiConfig config);

    /**
     * 获取用户的Wiki配置
     *
     * @param userId 用户ID
     * @return 返回用户的配置列表
     */
    @SelectProvider(type = UserWikiConfigMapperProvider.class, method = "getConfigByUserId")
    WikiConfig getConfigByUserId(String userId);

    /**
     * 判断用户的Wiki配置是否存在
     *
     * @param userId 用户的Id
     * @return 如果存在则返回true，否则返回false
     */
    @Select("select count(*) as result from " + table + " where userId=#{userId}")
    boolean existsByUserId(String userId);
}

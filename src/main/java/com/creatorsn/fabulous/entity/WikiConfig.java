package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 用户笔记配置类
 */
public class WikiConfig {

    /**
     * 配置文件的Id
     */
    @JsonProperty
    private String configId;
    /**
     * 所属的用户的Id
     */
    @JsonProperty
    private String userId;
    /**
     * 数据的路径,JSON的数组格式
     */
    @JsonProperty("data_path")
    private List<DataStruct> dataPath;
    /**
     * 数据的索引
     */
    @JsonProperty("data_index")
    private String dataIndex;
    /**
     * 支持的语言
     */
    @JsonProperty
    private String language;
    /**
     * 是否自动保存
     */
    @JsonProperty
    private Boolean autoSave;
    /**
     * 是否已初始化
     */
    @JsonProperty
    private Boolean initStatus;
    /**
     * 配置的名字
     */
    @JsonProperty
    private String name;
    /**
     * 最后的页面位置
     */
    @JsonProperty
    private String lastLocalPath;
    /**
     * editor是否开展内容
     */
    @JsonProperty
    private Boolean editorExpandContent;
    /**
     * 系统的激活模式
     */
    @JsonProperty
    private ConfigSystemMode activeSystemMode;
    /**
     * 动态效果是否开启
     */
    @JsonProperty
    private Boolean dynamicEffect;
    /**
     * 是否监听所有扩展
     */
    @JsonProperty
    private Boolean watchAllExtensions;
    /**
     * 主题颜色
     */
    @JsonProperty
    private ConfigTheme theme;
    /**
     * 创建的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime createDate;
    /**
     * 更新的时间
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    public String getConfigId() {
        return configId;
    }

    public WikiConfig setConfigId(String configId) {
        this.configId = configId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public WikiConfig setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public List<DataStruct> getDataPath() {
        return dataPath;
    }

    public WikiConfig setDataPath(List<DataStruct> dataPath) {
        this.dataPath = dataPath;
        return this;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public WikiConfig setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public WikiConfig setLanguage(String language) {
        this.language = language;
        return this;
    }

    public Boolean isAutoSave() {
        return autoSave;
    }

    public WikiConfig setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
        return this;
    }

    public Boolean isInitStatus() {
        return initStatus;
    }

    public WikiConfig setInitStatus(Boolean initStatus) {
        this.initStatus = initStatus;
        return this;
    }

    public String getName() {
        return name;
    }

    public WikiConfig setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastLocalPath() {
        return lastLocalPath;
    }

    public WikiConfig setLastLocalPath(String lastLocalPath) {
        this.lastLocalPath = lastLocalPath;
        return this;
    }

    public Boolean isEditorExpandContent() {
        return editorExpandContent;
    }

    public WikiConfig setEditorExpandContent(Boolean editorExpandContent) {
        this.editorExpandContent = editorExpandContent;
        return this;
    }

    public ConfigSystemMode getActiveSystemMode() {
        return activeSystemMode;
    }

    public WikiConfig setActiveSystemMode(ConfigSystemMode activeSystemMode) {
        this.activeSystemMode = activeSystemMode;
        return this;
    }

    public Boolean isDynamicEffect() {
        return dynamicEffect;
    }

    public WikiConfig setDynamicEffect(Boolean dynamicEffect) {
        this.dynamicEffect = dynamicEffect;
        return this;
    }

    public Boolean isWatchAllExtensions() {
        return watchAllExtensions;
    }

    public WikiConfig setWatchAllExtensions(Boolean watchAllExtensions) {
        this.watchAllExtensions = watchAllExtensions;
        return this;
    }

    public ConfigTheme getTheme() {
        return theme;
    }

    public WikiConfig setTheme(ConfigTheme theme) {
        this.theme = theme;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public WikiConfig setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public WikiConfig setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public enum ConfigSystemMode {
        both,
        ds,
        notebook
    }

    public enum ConfigTheme {
        light,
        dark,
        system
    }

    public enum WikiDataType {
        notebook,
        pdf
    }

    /**
     * 数据类型
     */
    public static class WikiDataPath {
        /**
         * 数据源的id或者本地路径
         */
        private String path;

        /**
         * 是否是共享的
         */
        private Boolean shared;

        /**
         * 是否是本地的
         */
        private Boolean local;

        /**
         * 类型：笔记本或者文献文档
         */
        private WikiDataType type;

        public String getPath() {
            return path;
        }

        public WikiDataPath setPath(String path) {
            this.path = path;
            return this;
        }

        public Boolean getShared() {
            return shared;
        }

        public WikiDataPath setShared(Boolean shared) {
            this.shared = shared;
            return this;
        }

        public WikiDataType getType() {
            return type;
        }

        public WikiDataPath setType(WikiDataType type) {
            this.type = type;
            return this;
        }

        public Boolean getLocal() {
            return local;
        }

        public WikiDataPath setLocal(Boolean local) {
            this.local = local;
            return this;
        }
    }
}

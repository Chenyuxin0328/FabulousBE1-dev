package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.entity.WikiConfig;
import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * 用户配置文件创建或者更新传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigCreateOrUpdateDTO {
    /**
     * 配置文件的Id
     */
    @JsonProperty("configId")
    @Pattern(regexp = RegexPattern.GUID, message = "配置Id格式错误")
    private String configId;

    /**
     * 数据的索引
     */
    @JsonProperty("data_index")
    private String dataIndex;

    /**
     * 语言
     */
    @JsonProperty("language")
    @NotNull
    private String language;

    /**
     * 是否自动保存
     */
    @JsonProperty("autoSave")
    private Boolean autoSave;

    /**
     * 是否已初始化
     */
    @JsonProperty("initStatus")
    private Boolean initStatus;

    /**
     * 配置的名字
     */
    @JsonProperty("name")
    private String name;

    /**
     * 最后的页面位置
     */
    @JsonProperty("lastLocalPath")
    private String lastLocalPath;

    /**
     * editor是否开展内容
     */
    @JsonProperty("editorExpandContent")
    private Boolean editorExpandContent;

    /**
     * 系统的激活模式
     */
    @JsonProperty("activeSystemMode")
    private WikiConfig.ConfigSystemMode activeSystemMode;

    /**
     * 动态效果是否开启
     */
    @JsonProperty("dynamicEffect")
    private Boolean dynamicEffect;

    /**
     * 是否监听所有扩展
     */
    @JsonProperty("watchAllExtensions")
    private Boolean watchAllExtensions;

    /**
     * 主题颜色
     */
    @JsonProperty("theme")
    private WikiConfig.ConfigTheme theme;

    public String getConfigId() {
        return configId;
    }

    public ConfigCreateOrUpdateDTO setConfigId(String configId) {
        this.configId = configId;
        return this;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public ConfigCreateOrUpdateDTO setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public ConfigCreateOrUpdateDTO setLanguage(String language) {
        this.language = language;
        return this;
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public ConfigCreateOrUpdateDTO setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
        return this;
    }

    public Boolean getInitStatus() {
        return initStatus;
    }

    public ConfigCreateOrUpdateDTO setInitStatus(Boolean initStatus) {
        this.initStatus = initStatus;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConfigCreateOrUpdateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastLocalPath() {
        return lastLocalPath;
    }

    public ConfigCreateOrUpdateDTO setLastLocalPath(String lastLocalPath) {
        this.lastLocalPath = lastLocalPath;
        return this;
    }

    public Boolean getEditorExpandContent() {
        return editorExpandContent;
    }

    public ConfigCreateOrUpdateDTO setEditorExpandContent(Boolean editorExpandContent) {
        this.editorExpandContent = editorExpandContent;
        return this;
    }

    public WikiConfig.ConfigSystemMode getActiveSystemMode() {
        return activeSystemMode;
    }

    public ConfigCreateOrUpdateDTO setActiveSystemMode(WikiConfig.ConfigSystemMode activeSystemMode) {
        this.activeSystemMode = activeSystemMode;
        return this;
    }

    public Boolean getDynamicEffect() {
        return dynamicEffect;
    }

    public ConfigCreateOrUpdateDTO setDynamicEffect(Boolean dynamicEffect) {
        this.dynamicEffect = dynamicEffect;
        return this;
    }

    public Boolean getWatchAllExtensions() {
        return watchAllExtensions;
    }

    public ConfigCreateOrUpdateDTO setWatchAllExtensions(Boolean watchAllExtensions) {
        this.watchAllExtensions = watchAllExtensions;
        return this;
    }

    public WikiConfig.ConfigTheme getTheme() {
        return theme;
    }

    public ConfigCreateOrUpdateDTO setTheme(WikiConfig.ConfigTheme theme) {
        this.theme = theme;
        return this;
    }
}

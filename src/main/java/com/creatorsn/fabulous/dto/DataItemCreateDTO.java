package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author minskiter
 * @date 29/8/2023 12:12
 * @description 数据项创建传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataItemCreateDTO {

    /**
     * 数据项的名称
     */
    @NotNull(message = "数据项的名称不能为空")
    @JsonProperty
    private String name;

    /**
     * 数据项的图标
     */
    @JsonProperty
    private String emoji;

    /**
     * 数据项的标签
     */
    @JsonProperty
    private List<ItemLabel> labels;

    public String getName() {
        return name;
    }

    public DataItemCreateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataItemCreateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }


    public List<ItemLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ItemLabel> labels) {
        this.labels = labels;
    }
}

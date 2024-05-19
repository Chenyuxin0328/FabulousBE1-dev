package com.creatorsn.fabulous.dto;

import com.creatorsn.fabulous.util.RegexPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * @author minskiter
 * @date 29/8/2023 12:18
 * @description 数据项更新传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataItemUpdateDTO {

    /**
     * 数据项的名称
     */
    @JsonProperty
    private String name;

    /**
     * 数据项的图标
     */
    @JsonProperty
    private String emoji;

    /**
     * 数据项的Id
     */
    @JsonProperty
    @Pattern(regexp = RegexPattern.GUID, message = "数据项的Id格式错误")
    private String id;

    /**
     * 数据项的pdfId
     */
    @JsonProperty
    @Pattern(regexp = RegexPattern.GUID, message = "数据项的pdfId格式错误")
    private String pdfId;

    /**
     * 数据项的标签
     */
    @JsonProperty
    private List<ItemLabel> labels;

    public String getName() {
        return name;
    }

    public DataItemUpdateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmoji() {
        return emoji;
    }

    public DataItemUpdateDTO setEmoji(String emoji) {
        this.emoji = emoji;
        return this;
    }

    public String getId() {
        return id;
    }

    public DataItemUpdateDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getPdfId() {
        return pdfId;
    }

    public DataItemUpdateDTO setPdfId(String pdfId) {
        this.pdfId = pdfId;
        return this;
    }

    public List<ItemLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ItemLabel> labels) {
        this.labels = labels;
    }
}

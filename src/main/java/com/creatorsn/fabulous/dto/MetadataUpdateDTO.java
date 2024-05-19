package com.creatorsn.fabulous.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * @author minskiter
 * @date 30/8/2023 08:06
 * @description 元数据更新
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataUpdateDTO {

    @JsonProperty
    private String id;

    /**
     * 发布者
     */
    @JsonProperty
    private String publisher;

    /**
     * DOI
     */
    @JsonProperty
    private String DOI;

    /**
     * 年份
     */
    @JsonProperty
    private Integer year;

    /**
     * 创建的时间
     */
    @JsonProperty
    private OffsetDateTime createDate;

    /**
     * 源
     */
    @JsonProperty
    private String source;

    /**
     * 标题
     */
    @JsonProperty
    private String title;

    /**
     * 链接
     */
    @JsonProperty
    private String url;

    /**
     * 容器标题
     */
    @JsonProperty
    private String containerTitle;

    /**
     * 摘要
     */
    @JsonProperty("abstract")
    private String _abstract;

    /**
     * ISSN号码
     */
    @JsonProperty
    private String ISSN;

    /**
     * 语言
     */
    @JsonProperty
    private String language;

    /**
     * 章节
     */
    @JsonProperty
    private String chapter;

    /**
     * 页
     */
    @JsonProperty
    private String pages;

    /**
     * 学校
     */
    @JsonProperty
    private String school;

    /**
     * 备忘录
     */
    @JsonProperty
    private String note;

    public String getId() {
        return id;
    }

    public MetadataUpdateDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public MetadataUpdateDTO setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public String getDOI() {
        return DOI;
    }

    public MetadataUpdateDTO setDOI(String DOI) {
        this.DOI = DOI;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public MetadataUpdateDTO setYear(Integer year) {
        this.year = year;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public MetadataUpdateDTO setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getSource() {
        return source;
    }

    public MetadataUpdateDTO setSource(String source) {
        this.source = source;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MetadataUpdateDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MetadataUpdateDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getContainerTitle() {
        return containerTitle;
    }

    public MetadataUpdateDTO setContainerTitle(String containerTitle) {
        this.containerTitle = containerTitle;
        return this;
    }

    public String get_abstract() {
        return _abstract;
    }

    public MetadataUpdateDTO set_abstract(String _abstract) {
        this._abstract = _abstract;
        return this;
    }

    public String getISSN() {
        return ISSN;
    }

    public MetadataUpdateDTO setISSN(String ISSN) {
        this.ISSN = ISSN;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public MetadataUpdateDTO setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getChapter() {
        return chapter;
    }

    public MetadataUpdateDTO setChapter(String chapter) {
        this.chapter = chapter;
        return this;
    }

    public String getPages() {
        return pages;
    }

    public MetadataUpdateDTO setPages(String pages) {
        this.pages = pages;
        return this;
    }

    public String getSchool() {
        return school;
    }

    public MetadataUpdateDTO setSchool(String school) {
        this.school = school;
        return this;
    }

    public String getNote() {
        return note;
    }

    public MetadataUpdateDTO setNote(String note) {
        this.note = note;
        return this;
    }
}

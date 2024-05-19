package com.creatorsn.fabulous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * @author minskiter
 * @date 28/8/2023 11:00
 * @description 数据项元信息
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemMetadata {

    /**
     * 元信息的Id
     */
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
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

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime updateDate;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private OffsetDateTime deleteDate;

    public String getId() {
        return id;
    }

    public ItemMetadata setId(String id) {
        this.id = id;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public ItemMetadata setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public String getDOI() {
        return DOI;
    }

    public ItemMetadata setDOI(String DOI) {
        this.DOI = DOI;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public ItemMetadata setYear(Integer year) {
        this.year = year;
        return this;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public ItemMetadata setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getSource() {
        return source;
    }

    public ItemMetadata setSource(String source) {
        this.source = source;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ItemMetadata setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ItemMetadata setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getContainerTitle() {
        return containerTitle;
    }

    public ItemMetadata setContainerTitle(String containerTitle) {
        this.containerTitle = containerTitle;
        return this;
    }

    public String get_abstract() {
        return _abstract;
    }

    public ItemMetadata set_abstract(String _abstract) {
        this._abstract = _abstract;
        return this;
    }

    public String getISSN() {
        return ISSN;
    }

    public ItemMetadata setISSN(String ISSN) {
        this.ISSN = ISSN;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public ItemMetadata setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getChapter() {
        return chapter;
    }

    public ItemMetadata setChapter(String chapter) {
        this.chapter = chapter;
        return this;
    }

    public String getPages() {
        return pages;
    }

    public ItemMetadata setPages(String pages) {
        this.pages = pages;
        return this;
    }

    public String getSchool() {
        return school;
    }

    public ItemMetadata setSchool(String school) {
        this.school = school;
        return this;
    }

    public String getNote() {
        return note;
    }

    public ItemMetadata setNote(String note) {
        this.note = note;
        return this;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public ItemMetadata setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public OffsetDateTime getDeleteDate() {
        return deleteDate;
    }

    public ItemMetadata setDeleteDate(OffsetDateTime deleteDate) {
        this.deleteDate = deleteDate;
        return this;
    }
}

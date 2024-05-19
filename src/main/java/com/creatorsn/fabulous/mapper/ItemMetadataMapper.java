package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.ItemMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

/**
 * @author minskiter
 * @date 28/8/2023 11:38
 * @description 数据项源数据映射
 */
@Mapper
public interface ItemMetadataMapper {

    static String table = "QItemMetadata";

    public class ItemMetadataMapperProvider {

        public String create(ItemMetadata metadata) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            if (StringUtils.hasText(metadata.get_abstract())) {
                sql.VALUES("abstract", "#{_abstarct}");
            }
            if (StringUtils.hasText(metadata.getLanguage())) {
                sql.VALUES("language", "#{language}");
            }
            if (StringUtils.hasText(metadata.getPages())) {
                sql.VALUES("pages", "#{pages}");
            }
            if (StringUtils.hasText(metadata.getDOI())) {
                sql.VALUES("DOI", "#{DOI}");
            }
            if (StringUtils.hasText(metadata.getChapter())) {
                sql.VALUES("chapter", "#{chapter}");
            }
            if (StringUtils.hasText(metadata.getISSN())) {
                sql.VALUES("ISSN", "#{ISSN}");
            }
            if (StringUtils.hasText(metadata.getNote())) {
                sql.VALUES("note", "#{note}");
            }
            if (StringUtils.hasText(metadata.getContainerTitle())) {
                sql.VALUES("containerTitle", "#{containerTitle}");
            }
            if (StringUtils.hasText(metadata.getSource())) {
                sql.VALUES("source", "#{source}");
            }
            if (StringUtils.hasText(metadata.getUrl())) {
                sql.VALUES("url", "#{url}");
            }
            if (StringUtils.hasText(metadata.getSchool())) {
                sql.VALUES("school", "#{school}");
            }
            if (StringUtils.hasText(metadata.getPublisher())) {
                sql.VALUES("publisher", "#{publisher}");
            }
            if (StringUtils.hasText(metadata.getTitle())) {
                sql.VALUES("title", "#{title}");
            }
            if (metadata.getYear() != null) {
                sql.VALUES("year", "#{year}");
            }
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            return sql.toString();
        }

        public String update(ItemMetadata metadata) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (StringUtils.hasText(metadata.get_abstract())) {
                sql.SET("abstract=#{_abstract}");
            }
            if (StringUtils.hasText(metadata.getLanguage())) {
                sql.SET("language=#{language}");
            }
            if (StringUtils.hasText(metadata.getPages())) {
                sql.SET("pages=#{pages}");
            }
            if (StringUtils.hasText(metadata.getDOI())) {
                sql.SET("DOI=#{DOI}");
            }
            if (StringUtils.hasText(metadata.getChapter())) {
                sql.SET("chapter=#{chapter}");
            }
            if (StringUtils.hasText(metadata.getISSN())) {
                sql.SET("ISSN=#{ISSN}");
            }
            if (StringUtils.hasText(metadata.getNote())) {
                sql.SET("note=#{note}");
            }
            if (StringUtils.hasText(metadata.getContainerTitle())) {
                sql.SET("containerTitle=#{containerTitle}");
            }
            if (StringUtils.hasText(metadata.getSource())) {
                sql.SET("source=#{source}");
            }
            if (StringUtils.hasText(metadata.getUrl())) {
                sql.SET("url=#{url}");
            }
            if (StringUtils.hasText(metadata.getSchool())) {
                sql.SET("school=#{school}");
            }
            if (StringUtils.hasText(metadata.getPublisher())) {
                sql.SET("publisher=#{publisher}");
            }
            if (StringUtils.hasText(metadata.getTitle())) {
                sql.SET("title=#{title}");
            }
            if (metadata.getYear() != null) {
                sql.SET("year=#{year}");
            }
            sql.SET("updateDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String delete(String id) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.EXISTS();
            sql.SET("deleteDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getById(String id) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

    }

    @SelectProvider(type = ItemMetadataMapperProvider.class, method = "create")
    ItemMetadata create(ItemMetadata metadata);

    @SelectProvider(type = ItemMetadataMapperProvider.class, method = "update")
    ItemMetadata update(ItemMetadata metadata);

    @Results(id = "itemMetadata")
    @ResultType(ItemMetadata.class)
    @SelectProvider(type = ItemMetadataMapperProvider.class, method = "getById")
    ItemMetadata getById(String id);


    @SelectProvider(type = ItemMetadataMapperProvider.class, method = "delete")
    boolean delete(String id);
}

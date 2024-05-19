package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.DataItem;
import com.creatorsn.fabulous.entity.ItemMetadata;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author minskiter
 * @date 24/8/2023 21:49
 * @description 数据项映射器
 */
@Mapper
public interface DataItemMapper {

    String table = "QDataItem";

    String relTable = "QDataItemRel";

    /**
     * 创建一个数据项
     *
     * @param item 数据项
     * @return 返回创建的数据项
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "create")
    DataItem create(DataItem item);

    /**
     * 更新一个数据项
     *
     * @param item 数据项
     * @return 返回更新后的数据项
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "update")
    DataItem update(DataItem item);

    /**
     * 删除一个数据项
     *
     * @param id 数据项的Id
     * @return 如果删除成功返回true，否则返回false
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "delete")
    boolean delete(String id);

    /**
     * 批量删除指定的数据项
     *
     * @param sourceId 数据源的Id
     * @param ids      要删除的数据项列表
     * @return 返回删除的数据项列表
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "deleteList")
    List<String> deleteList(String sourceId, List<String> ids);

    /**
     * 获取对应的数据项
     *
     * @param ids 数据项的Id列表
     * @return 返回对应的数据项
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "list")
    List<DataItem> list(List<String> ids);

    /**
     * 获取父节点下的所有数据项
     *
     * @param parentId 父节点的Id
     * @return 返回获取的所有数据项
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "listByParent")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "emoji", column = "emoji"),
            @Result(property = "metadataId", column = "metadataId"),
            @Result(property = "labels", column = "labels"),
            @Result(property = "metadata", one = @One(resultMap = "itemMetadata", columnPrefix = "metadata_")),
            @Result(property = "createDate", column = "createDate"),
            @Result(property = "updateDate", column = "updateDate"),
            @Result(property = "owner", column = "owner"),
            @Result(property = "sourceId", column = "sourceId"),
            @Result(property = "pdf", column = "pdf")
    })
    List<DataItem> listByParent(String parentId, long offset, int length, String sort, boolean desc);

    /**
     * 获取父节点下的数据项数目
     *
     * @param parentId 父节点的Id
     * @return 返回数据项的数目
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "getCountByParent")
    long getCountByParent(String parentId);

    /**
     * 获取对应的数据项
     *
     * @param id 数据项的Id
     * @return 返回对应的数据项
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "getById")
    DataItem getById(String id);

    /**
     * 列出指定数据源下的所有数据项
     *
     * @param sourceId 数据源
     * @param offset   偏移量
     * @param length   长度
     * @param sort     排序的关键字
     * @param desc     是否递减
     * @return 返回指定数据源下的所有数据项
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "listBySourceId")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "emoji", column = "emoji"),
            @Result(property = "metadataId", column = "metadataId"),
            @Result(property = "labels", column = "labels"),
            @Result(property = "metadata", one = @One(resultMap = "itemMetadata", columnPrefix = "metadata_")),
            @Result(property = "createDate", column = "createDate"),
            @Result(property = "updateDate", column = "updateDate"),
            @Result(property = "owner", column = "owner"),
            @Result(property = "sourceId", column = "sourceId"),
            @Result(property = "pdf", column = "pdf")
    })
    List<DataItem> listBySourceId(String sourceId, long offset, int length, String sort, boolean desc);

    /**
     * 获取数据源下所有数据项的数量
     *
     * @param sourceId 数据源的Id
     * @return 返回数据源下所有的数据项的数量
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "getCountBySourceId")
    long getCountBySourceId(String sourceId);

    /**
     * 添加数据项到指定分区
     *
     * @param id          数据项的Id
     * @param partitionId 分区的Id
     * @param operator    操作者
     * @return 如果添加成功则返回true，否则返回false
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "addToPartition")
    boolean addToPartition(String id, String partitionId, String operator);

    /**
     * 批量添加数据项到指定的分区中
     *
     * @param ids         数据项的Id列表
     * @param partitionId 分区的列表
     * @param operator    操作者
     * @return 返回成功添加的数据项Id
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "addListToPartition")
    List<String> addListToPartition(List<String> ids, String partitionId, String operator);

    /**
     * 从分区中移除指定数据项
     *
     * @param id          数据项的Id
     * @param partitionId 分区的Id
     * @return 如果成功则返回true，否则返回false
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "removeFromPartition")
    boolean removeFromPartition(String id, String partitionId);

    /**
     * 批量从分区中移除指定的数据项
     *
     * @param ids         数据项的Id列表
     * @param partitionId 分区的Id
     * @return 返回被删除的数据项Id列表
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "removeListFromPartition")
    List<String> removeListFromPartition(List<String> ids, String partitionId);

    /**
     * 查询指定分区下的数据项
     *
     * @param parentId 分区的Id
     * @param sourceId 数据源的Id
     * @param keyword  关键字
     * @param offset   偏移量
     * @param length   长度
     * @param sort     排序的关键字
     * @param desc     是否递减
     * @return 返回符合条件的数据项
     */
    @SelectProvider(type = DataItemMapperProvider.class, method = "query")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "emoji", column = "emoji"),
            @Result(property = "metadataId", column = "metadataId"),
            @Result(property = "labels", column = "labels"),
            @Result(property = "metadata", one = @One(resultMap = "itemMetadata", columnPrefix = "metadata_")),
            @Result(property = "createDate", column = "createDate"),
            @Result(property = "updateDate", column = "updateDate"),
            @Result(property = "owner", column = "owner"),
            @Result(property = "sourceId", column = "sourceId"),
            @Result(property = "pdf", column = "pdf")
    })
    List<DataItem> query(String parentId, String sourceId, String keyword, long offset, int length, String sort, boolean desc);

    @Select("select * from " + ItemMetadataMapper.table + " where id=#{metadataId}")
    @Results(id = "itemMetadata", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "DOI", column = "DOI"),
            @Result(property = "year", column = "year"),
            @Result(property = "createDate", column = "createDate"),
            @Result(property = "source", column = "source"),
            @Result(property = "title", column = "title"),
            @Result(property = "url", column = "url"),
            @Result(property = "containerTitle", column = "containerTitle"),
            @Result(property = "_abstract", column = "abstract"),
            @Result(property = "ISSN", column = "ISSN"),
            @Result(property = "language", column = "language"),
            @Result(property = "chapter", column = "chapter"),
            @Result(property = "pages", column = "pages"),
            @Result(property = "school", column = "school"),
            @Result(property = "note", column = "note"),
            @Result(property = "updateDate", column = "updateDate")
    })
    List<ItemMetadata> getMetadataById(String metadataId);

    class DataItemMapperProvider {

        public String create(DataItem item) {
            var sql = new MSSQL()
                    .INSERT_INTO(table)
                    .OUTPUT_INSERT("*")
                    .VALUES("name", "#{name}");
            if (StringUtils.hasText(item.getEmoji()))
                sql.VALUES("emoji", "#{emoji}");
            if (StringUtils.hasText(item.getPdf())) {
                sql.VALUES("pdf", "#{pdf}");
            }
            sql.VALUES("id", "#{id}");
            if (StringUtils.hasText(item.getMetadataId()))
                sql.VALUES("metadataId", "#{metadataId}");
            sql.VALUES("updateDate", "getdate()");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("owner", "#{owner}");
            sql.VALUES("sourceId", "#{sourceId}");
            sql.VALUES("labels", "#{labels}");
            return sql.toString();
        }

        public String delete(String id) {
            var sql = new MSSQL();
            sql.EXISTS();
            sql.UPDATE(table);
            sql.SET("deleteDate=getdate()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String deleteList(String sourceId, List<String> ids) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("id");
            sql.SET("deleteDate=getdate()");
            sql.WHERE("id in ('" + String.join("','", ids) + "')");
            sql.WHERE("sourceId=#{sourceId}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String update(DataItem item) {
            var sql = new MSSQL()
                    .UPDATE(table)
                    .OUTPUT_INSERT("*")
                    .SET("updateDate=getdate()");
            if (StringUtils.hasText(item.getName())) {
                sql.SET("name=#{name}");
            }
            if (StringUtils.hasText(item.getEmoji()))
                sql.SET("emoji=#{emoji}");
            if (StringUtils.hasText(item.getPdf())) {
                sql.SET("pdf=#{pdf}");
            }
            if (StringUtils.hasText(item.getMetadataId())) {
                sql.SET("metadataId=#{metadataId}");
            }
            if (!item.getLabels().isEmpty()) {
                sql.SET("labels=#{labels}");
            }
            sql.WHERE("deleteDate is null");
            sql.WHERE("id=#{id}");
            return sql.toString();
        }

        public String list(List<String> ids) {
            var sql = new MSSQL()
                    .SELECT("*")
                    .FROM(table);
            if (!ids.isEmpty() && ids.size() > 1)
                sql.WHERE("id in ('" + String.join("','", ids) + "')");
            else if (ids.size() == 1) {
                sql.WHERE("id = " + ids.get(0));
            } else {
                sql.WHERE("1=0");
            }
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getById(String id) {
            var sql = new MSSQL().SELECT("top 1 *")
                    .FROM(table)
                    .WHERE("id=#{id}")
                    .WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listByParent(String parentId, long offset, int length, String sort, boolean desc) {
            var sql = new MSSQL().SELECT(table + ".*",
                            ItemMetadataMapper.table + ".id as metadata_id",
                            ItemMetadataMapper.table + ".publisher as metadata_publisher",
                            ItemMetadataMapper.table + ".DOI as metadata_DOI",
                            ItemMetadataMapper.table + ".year as metadata_year",
                            ItemMetadataMapper.table + ".createDate as metadata_createDate",
                            ItemMetadataMapper.table + ".source as metadata_source",
                            ItemMetadataMapper.table + ".title as metadata_title",
                            ItemMetadataMapper.table + ".url as metadata_url",
                            ItemMetadataMapper.table + ".containerTitle as metadata_containerTitle",
                            ItemMetadataMapper.table + ".abstract as metadata_abstract",
                            ItemMetadataMapper.table + ".ISSN as metadata_ISSN",
                            ItemMetadataMapper.table + ".language as metadata_language",
                            ItemMetadataMapper.table + ".chapter as metadata_chapter",
                            ItemMetadataMapper.table + ".pages as metadata_pages",
                            ItemMetadataMapper.table + ".school as metadata_school",
                            ItemMetadataMapper.table + ".note as metadata_note",
                            ItemMetadataMapper.table + ".updateDate as metadata_updateDate")
                    .FROM(table)
                    .LEFT_OUTER_JOIN(relTable + " on " + relTable + ".itemId=" + table + ".id")
                    .LEFT_OUTER_JOIN(ItemMetadataMapper.table + " on " + ItemMetadataMapper.table + ".id=" + table + ".metadataId")
                    .WHERE(relTable + ".partitionId=#{parentId}")
                    .WHERE(table + ".deleteDate is null")
                    .OFFSET(offset)
                    .FETCH_FIRST_ROWS_ONLY(length);
            if (sort.startsWith("metadata")) {
                var names = sort.split("\\.");
                sort = names[names.length - 1];
                sql.ORDER_BY(ItemMetadataMapper.table + "." + sort + (desc ? " desc" : " asc"));
            } else {
                sql.ORDER_BY(table + "." + sort + (desc ? " desc" : " asc"));
            }
            return sql.toString();
        }

        public String query(String parentId, String sourceId, String keyword, long offset, int length, String sort, boolean desc) {
            var sql = new MSSQL().SELECT(table + ".*",
                            ItemMetadataMapper.table + ".id as metadata_id",
                            ItemMetadataMapper.table + ".publisher as metadata_publisher",
                            ItemMetadataMapper.table + ".DOI as metadata_DOI",
                            ItemMetadataMapper.table + ".year as metadata_year",
                            ItemMetadataMapper.table + ".createDate as metadata_createDate",
                            ItemMetadataMapper.table + ".source as metadata_source",
                            ItemMetadataMapper.table + ".title as metadata_title",
                            ItemMetadataMapper.table + ".url as metadata_url",
                            ItemMetadataMapper.table + ".containerTitle as metadata_containerTitle",
                            ItemMetadataMapper.table + ".abstract as metadata_abstract",
                            ItemMetadataMapper.table + ".ISSN as metadata_ISSN",
                            ItemMetadataMapper.table + ".language as metadata_language",
                            ItemMetadataMapper.table + ".chapter as metadata_chapter",
                            ItemMetadataMapper.table + ".pages as metadata_pages",
                            ItemMetadataMapper.table + ".school as metadata_school",
                            ItemMetadataMapper.table + ".note as metadata_note",
                            ItemMetadataMapper.table + ".updateDate as metadata_updateDate")
                    .FROM(table)
                    .LEFT_OUTER_JOIN(relTable + " on " + relTable + ".itemId=" + table + ".id")
                    .LEFT_OUTER_JOIN(ItemMetadataMapper.table + " on " + ItemMetadataMapper.table + ".id=" + table + ".metadataId")
                    .WHERE(table + ".deleteDate is null")
                    .WHERE(ItemMetadataMapper.table + ".deleteDate is null")
                    .WHERE("sourceId = #{sourceId}");
            if (StringUtils.hasText(parentId)) {
                sql.WHERE(relTable + ".partitionId=#{parentId}");
            }
            if (StringUtils.hasText(keyword)) {
                sql.WHERE("name like '%'+#{keyword}+'%'");
            }
            sql.OFFSET(offset);
            sql.FETCH_FIRST_ROWS_ONLY(length);
            if (sort.startsWith("metadata")) {
                var names = sort.split("\\.");
                sort = names[names.length - 1];
                sql.ORDER_BY(ItemMetadataMapper.table + "." + sort + (desc ? " desc" : " asc"));
            } else {
                sql.ORDER_BY(table + "." + sort + (desc ? " desc" : " asc"));
            }
            return sql.toString();
        }

        public String getCountByParent(String parentId) {
            var sql = new MSSQL();
            sql.SELECT("count(*)");
            sql.FROM(table)
                    .JOIN(relTable + " on " + relTable + ".itemId=" + table + ".id")
                    .WHERE(relTable + ".partitionId=#{parentId}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listBySourceId(String sourceId, long offset, int length, String sort, boolean desc) {
            var sql = new MSSQL();
            sql.SELECT(table + ".*",
                    ItemMetadataMapper.table + ".id as metadata_id",
                    ItemMetadataMapper.table + ".publisher as metadata_publisher",
                    ItemMetadataMapper.table + ".DOI as metadata_DOI",
                    ItemMetadataMapper.table + ".year as metadata_year",
                    ItemMetadataMapper.table + ".createDate as metadata_createDate",
                    ItemMetadataMapper.table + ".source as metadata_source",
                    ItemMetadataMapper.table + ".title as metadata_title",
                    ItemMetadataMapper.table + ".url as metadata_url",
                    ItemMetadataMapper.table + ".containerTitle as metadata_containerTitle",
                    ItemMetadataMapper.table + ".abstract as metadata_abstract",
                    ItemMetadataMapper.table + ".ISSN as metadata_ISSN",
                    ItemMetadataMapper.table + ".language as metadata_language",
                    ItemMetadataMapper.table + ".chapter as metadata_chapter",
                    ItemMetadataMapper.table + ".pages as metadata_pages",
                    ItemMetadataMapper.table + ".school as metadata_school",
                    ItemMetadataMapper.table + ".note as metadata_note",
                    ItemMetadataMapper.table + ".updateDate as metadata_updateDate");
            sql.FROM(table);
            sql.LEFT_OUTER_JOIN(ItemMetadataMapper.table + " on " + ItemMetadataMapper.table + ".id=" + table + ".metadataId");
            sql.WHERE(table + ".sourceId=#{sourceId}");
            sql.WHERE(table + ".deleteDate is null");
            sql.OFFSET(offset);
            sql.FETCH_FIRST_ROWS_ONLY(length);
            if (sort.startsWith("metadata")) {
                var names = sort.split("\\.");
                sort = names[names.length - 1];
                sql.ORDER_BY(ItemMetadataMapper.table + "." + sort + (desc ? " desc" : " asc"));
            } else {
                sql.ORDER_BY(table + "." + sort + (desc ? " desc" : " asc"));
            }
            return sql.toString();
        }

        public String getCountBySourceId(String sourceId) {
            var sql = new MSSQL();
            sql.SELECT("count(*)");
            sql.FROM(table);
            sql.WHERE("sourceId=#{sourceId}");
            return sql.toString();
        }

        public String addToPartition(String id, String partitionId, String operator) {
            var sql = new MSSQL();
            sql.INSERT_INTO(relTable);
            sql.VALUES("itemId", "#{id}");
            sql.VALUES("partitionId", "#{partitionId}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("operator", "#{operator}");
            sql.EXISTS();
            return sql.toString();
        }

        public String addListToPartition(List<String> ids, String partitionId, String operator) {
            var sql = new StringBuilder();
            sql.append("MERGE INTO ").append(relTable).append(" As target\n");
            sql.append("USING (VALUES ");
            boolean first = true;
            for (var id : ids) {
                if (first) first = false;
                else sql.append(",");
                sql.append("('").append(id).append("',#{partitionId},#{operator})\n");
            }
            sql.append(") AS source (id,partitionId,operator)\n");
            sql.append("ON target.itemId=source.id\n");
            sql.append("WHEN NOT MATCHED THEN\n");
            sql.append("INSERT (itemId,partitionId,operator,createDate)\n");
            sql.append("VALUES (source.id,source.partitionId,source.operator,getdate());");
            return sql.toString();
        }

        public String removeFromPartition(String id, String partitionId) {
            var sql = new MSSQL();
            sql.DELETE_FROM(table);
            sql.WHERE("itemId=#{id}");
            sql.WHERE("partitionId=#{partitionId}");
            sql.EXISTS();
            return sql.toString();
        }

        public String removeListFromPartition(List<String> ids, String partitionId) {
            var sql = new MSSQL();
            sql.DELETE_FROM(relTable);
            sql.WHERE("itemId in ('" + String.join("','", ids) + "')");
            sql.WHERE("partitionId=#{partitionId}");
            sql.OUTPUT_DELETE("itemId");
            return sql.toString();
        }

    }
}

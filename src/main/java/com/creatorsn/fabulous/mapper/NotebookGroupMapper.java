package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.DataGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 笔记本分组
 */
@Mapper
public interface NotebookGroupMapper {

    String table = "QNotebookGroup";

    @SelectProvider(type = DataGroupMapperProvider.class, method = "create")
    DataGroup create(DataGroup group);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "delete")
    boolean delete(String id);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "update")
    DataGroup update(DataGroup group);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "getById")
    DataGroup getById(String id);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "list")
    List<DataGroup> list(List<String> ids);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "listByParent")
    List<DataGroup> listByParent(String parent);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "listByParentAndOwner")
    List<DataGroup> listByParentAndOwner(String parent, String owner);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "exists")
    boolean exists(String id);

    @SelectProvider(type = DataGroupMapperProvider.class, method = "existsByName")
    boolean existsByName(String name, String parentId);

    class DataGroupMapperProvider {

        public String create(DataGroup group) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            if (StringUtils.hasText(group.getParent()))
                sql.VALUES("parent", "#{parent}");
            sql.VALUES("updateDate", "getdate()");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("sourceId", "#{sourceId}");
            if (StringUtils.hasText(group.getOwner())) {
                sql.VALUES("owner", "#{owner}");
            }
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

        public String update(DataGroup group) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (StringUtils.hasText(group.getName())) {
                sql.SET("name=#{name}");
            }
            if (StringUtils.hasText(group.getEmoji())) {
                sql.SET("emoji=#{emoji}");
            }
            if (StringUtils.hasText(group.getParent())) {
                sql.SET("parent=#{parent}");
            }
            sql.SET("updateDate=getdate()");
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

        public String list(List<String> ids) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("id in ('" + String.join("','", ids) + "')");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listByParent(String parent) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            if (StringUtils.hasText(parent)) {
                sql.WHERE("parent=#{parent}");
            } else {
                sql.WHERE("parent is null");
            }
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String exists(String id) {
            var sql = new MSSQL();
            sql.EXISTS();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String existsByName(String parentId, String name) {
            var sql = new MSSQL();
            sql.EXISTS();
            sql.SELECT("id");
            sql.FROM(table);
            sql.WHERE("parent=#{parentId}");
            sql.WHERE("name=#{name}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listByParentAndOwner(String parent, String owner) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            if (StringUtils.hasText(parent))
                sql.WHERE("parent=#{parent}");
            else
                sql.WHERE("parent is null");
            sql.WHERE("owner=#{owner}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }
    }


}

package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.DataPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;

@Mapper
public interface PageMapper {

    static String table = "QDataPage";

    public class PageMapperProvider {

        public String create(DataPage page) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            if (StringUtils.hasText(page.getEmoji())) {
                sql.VALUES("emoji", "#{emoji}");
            }
            if (page.getParent() != null) {
                sql.VALUES("parent", "#{parent}");
            }
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            return sql.toString();
        }

        public String delete(DataPage page) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.SET("deleteDate=getdate()");
            sql.OUTPUT("count(inserted.*) as result");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String update(DataPage page) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (StringUtils.hasText(page.getName())) {
                sql.SET("name=#{name}");
            }
            if (StringUtils.hasText(page.getParent())) {
                sql.SET("parent=#{parent}");
            }
            sql.SET("updateDate=#{updated}");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String list(List<String> ids) {
            var idsString = String.join(",", ids);
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("*");
            sql.WHERE("ids in (" + idsString + ")");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String listByParent(String id) {
            var sql = new MSSQL();
            sql.FROM(table);
            sql.SELECT("*");
            sql.WHERE("parent=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

    }

    @SelectProvider(type = PageMapperProvider.class, method = "create")
    DataPage create(DataPage page);

    @SelectProvider(type = PageMapperProvider.class, method = "delete")
    boolean delete(String id);

    @SelectProvider(type = PageMapperProvider.class, method = "update")
    DataPage update(DataPage page);

    @SelectProvider(type = PageMapperProvider.class, method = "list")
    List<DataPage> list(List<String> ids);

    @SelectProvider(type = PageMapperProvider.class, method = "listByParent")
    List<DataPage> listByParent(String parent);

}

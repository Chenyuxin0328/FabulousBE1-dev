package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.DataStruct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 数据结构DAO
 */
@Mapper
public interface DataStructMapper {

    String table = "QDataStruct";

    class DataStructMapperProvider {

        public String create(DataStruct dataStruct) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("id", "#{id}");
            sql.VALUES("name", "#{name}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            sql.VALUES("userId", "#{userId}");
            return sql.toString();
        }

        public String update(DataStruct dataStruct) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (dataStruct.getName() != null) {
                sql.SET("name=#{name}");
            }
            sql.SET("updateDate=GETDATE()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String getByUserId(String userId) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("userId=#{userId}");
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

        public String delete(String id) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.EXISTS();
            sql.SET("deleteDate=GETDATE()");
            sql.WHERE("id=#{id}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

        public String existsByName(String name, String userId) {
            var sql = new MSSQL();
            sql.SELECT("count(name) as result");
            sql.FROM(table);
            sql.WHERE("name=#{name}");
            sql.WHERE("userId=#{userId}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }
    }


    @SelectProvider(type = DataStructMapperProvider.class, method = "create")
    DataStruct create(DataStruct dataStruct);


    @SelectProvider(type = DataStructMapperProvider.class, method = "update")
    DataStruct update(DataStruct dataStruct);


    @SelectProvider(type = DataStructMapperProvider.class, method = "getByUserId")
    List<DataStruct> getByUserId(String userId);

    @SelectProvider(type = DataStructMapperProvider.class, method = "getById")
    DataStruct getById(String id);

    @SelectProvider(type = DataStructMapperProvider.class, method = "delete")
    boolean delete(String id);

    @SelectProvider(type = DataStructMapperProvider.class, method = "existsByName")
    boolean existsByName(String name, String userId);

}

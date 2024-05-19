package com.creatorsn.fabulous.mapper;

import com.creatorsn.fabulous.entity.DataPartition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author minskiter
 * @date 24/8/2023 16:54
 * @description 数据分区映射器
 */
@Mapper
public interface DataPartitionMapper {

    static String table = "QDataPartition";

    public class DataPartitionMapperProvider {

        public String create(DataPartition dataPartition) {
            var sql = new MSSQL();
            sql.INSERT_INTO(table);
            sql.OUTPUT_INSERT("*");
            sql.VALUES("name", "#{name}");
            if (StringUtils.hasText(dataPartition.getEmoji()))
                sql.VALUES("emoji", "#{emoji}");
            sql.VALUES("id", "#{id}");
            sql.VALUES("parent", "#{parent}");
            sql.VALUES("createDate", "getdate()");
            sql.VALUES("updateDate", "getdate()");
            sql.VALUES("sourceId", "#{sourceId}");
            sql.VALUES("owner", "#{owner}");
            return sql.toString();
        }

        public String update(DataPartition dataPartition) {
            var sql = new MSSQL();
            sql.UPDATE(table);
            sql.OUTPUT_INSERT("*");
            if (StringUtils.hasText(dataPartition.getName()))
                sql.SET("name=#{name}");
            if (StringUtils.hasText(dataPartition.getParent()))
                sql.SET("parent=#{parent}");
            if (StringUtils.hasText(dataPartition.getEmoji())) {
                sql.SET("emoji=#{emoji}");
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
            sql.SELECT("top 1 *");
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

        public String listByParent(String parentId) {
            var sql = new MSSQL();
            sql.SELECT("*");
            sql.FROM(table);
            sql.WHERE("parent =#{parent}");
            sql.WHERE("deleteDate is null");
            return sql.toString();
        }

    }

    /**
     * 创建数据分区
     *
     * @param dataPartition 数据分区
     * @return 返回创建后的数据分区
     */
    @SelectProvider(type = DataPartitionMapperProvider.class, method = "create")
    DataPartition create(DataPartition dataPartition);

    /**
     * 更新数据分区
     *
     * @param dataPartition 数据分区
     * @return 返回更新后的数据
     */
    @SelectProvider(type = DataPartitionMapperProvider.class, method = "update")
    DataPartition update(DataPartition dataPartition);

    /**
     * 获取对应的数据分区
     *
     * @param id 数据分区的Id
     * @return 返回获取对应的数据分区
     */
    @SelectProvider(type = DataPartitionMapperProvider.class, method = "getById")
    DataPartition getById(String id);

    /**
     * 列出数据分区
     *
     * @param ids 要获取的数据分区的id列表
     * @return 返回指定的数据分区列表
     */
    @SelectProvider(type = DataPartitionMapperProvider.class, method = "list")
    List<DataPartition> list(List<String> ids);

    /**
     * 列出所有的子数据分区
     *
     * @param parent 数据分区的父节点Id
     * @return 返回子数据分区列表
     */
    @SelectProvider(type = DataPartitionMapperProvider.class, method = "listByParent")
    List<DataPartition> listByParent(String parent);

    /**
     * 删除指定的数据分区
     *
     * @param id 数据分区的Id
     * @return 如果删除成功则返回true，否则返回false
     */
    @SelectProvider(type = DataPartitionMapperProvider.class, method = "delete")
    boolean delete(String id);
}

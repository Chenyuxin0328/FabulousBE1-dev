package com.creatorsn.fabulous.mapper.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
@Component
public class ListTypeHandler implements TypeHandler<List<Object>> {

    private final Logger logger = LoggerFactory.getLogger(ListTypeHandler.class);

    /**
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, List<Object> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.isEmpty()) {
            ps.setString(i, "");
        } else {
            var object = new ObjectMapper();
            try {
                ps.setString(i, object.writeValueAsString(parameter));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public List<Object> getResult(ResultSet rs, String columnName) throws SQLException {
        var result = rs.getString(columnName);
        var object = new ObjectMapper();
        try {
            return !StringUtils.hasText(result) ? new ArrayList<>() : List.of(object.readValue(result, Object[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public List<Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        var result = rs.getString(columnIndex);
        var object = new ObjectMapper();
        try {
            return !StringUtils.hasText(result) ? new ArrayList<>() : List.of(object.readValue(result, Object[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param cs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public List<Object> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        var result = cs.getString(columnIndex);
        var object = new ObjectMapper();
        try {
            return !StringUtils.hasText(result) ? new ArrayList<>() : List.of(object.readValue(result, Object[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

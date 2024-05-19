package com.creatorsn.fabulous.mapper.handlers;

import com.creatorsn.fabulous.entity.UserGenderEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({UserGenderEnum.class})
@MappedJdbcTypes(JdbcType.INTEGER)
@Component
public class UserGenderEnumTypeHandler implements TypeHandler<UserGenderEnum> {

    /**
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, UserGenderEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    /**
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public UserGenderEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return UserGenderEnum.fromValue(rs.getInt(columnName));
    }

    /**
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public UserGenderEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return UserGenderEnum.fromValue(rs.getInt(columnIndex));
    }

    /**
     * @param cs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public UserGenderEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return UserGenderEnum.fromValue(cs.getInt(columnIndex));
    }
}

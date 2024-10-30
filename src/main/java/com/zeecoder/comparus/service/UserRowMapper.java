package com.zeecoder.comparus.service;

import com.zeecoder.comparus.domain.Users;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<Users> {
    @Override
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Users.builder()
                .id(rs.getString("id"))
                .username(rs.getString("username"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .build();
    }
}
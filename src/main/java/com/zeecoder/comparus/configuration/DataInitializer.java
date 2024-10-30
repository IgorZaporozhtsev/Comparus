package com.zeecoder.comparus.configuration;

import com.zeecoder.comparus.configuration.properties_set.DataSet;
import com.zeecoder.comparus.configuration.properties_set.DataSourceProperties;
import com.zeecoder.comparus.configuration.properties_set.TableSet;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final DataSourceProperties properties;

    private void createTableFromDataSource(DataSet dataSet) {
        try {
            DataSourceContextHolder.setCurrentDataSource(dataSet.name());

            jdbcTemplate.update("DROP TABLE IF EXISTS Users");

            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ")
                    .append(dataSet.table())
                    .append("(")
                    .append("id VARCHAR(255),")
                    .append("username VARCHAR(255),")
                    .append("name VARCHAR(255),")
                    .append("surname VARCHAR(255)")
                    .append(")");

            jdbcTemplate.update(builder.toString());
        } finally {
            DataSourceContextHolder.clear();
        }
    }

    private void insertTestData(DataSet dataSet, TableSet tableSet){
        DataSourceContextHolder.setCurrentDataSource(dataSet.name());
        for (int i = 1; i < 2; i++) {
            String sql = "INSERT INTO " + dataSet.table() + " (id, username, name, surname) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, tableSet.id(), tableSet.username(), tableSet.name(), tableSet.surname());
        }
    }

    @PostConstruct
    private void init(){
        for (DataSet dataSet : properties.dataSources()) {
            createTableFromDataSource(dataSet);
            insertTestData(dataSet, dataSet.mapping());
        }
    }
}

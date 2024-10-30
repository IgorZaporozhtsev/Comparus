package com.zeecoder.comparus.service;

import com.zeecoder.comparus.configuration.DataSourceContextHolder;
import com.zeecoder.comparus.configuration.properties_set.DataSet;
import com.zeecoder.comparus.configuration.properties_set.DataSourceProperties;
import com.zeecoder.comparus.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSourceProperties properties;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public List<Users> findAll(){
        List<Users> users = properties.dataSources().parallelStream()
                .map(dataSource -> CompletableFuture.supplyAsync(() -> executeQuery(dataSource), executor))
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();
        return users;
    }

    public List<Users> executeQuery(DataSet dataSet) {
        log.info("Execute query. Current datasource url: {} ", dataSet.url());
        DataSourceContextHolder.setCurrentDataSource(dataSet.name());
        String sql = "SELECT id, username, name, surname FROM " + dataSet.table();
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
}

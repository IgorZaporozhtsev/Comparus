package com.zeecoder.comparus.configuration;

import com.zeecoder.comparus.configuration.properties_set.DataSet;
import com.zeecoder.comparus.configuration.properties_set.DataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final DataSourceProperties properties;

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();

        for (DataSet dataSource : properties.dataSources()) {
            targetDataSources.put(dataSource.name(), initializeDataSourceBuilder(
                            dataSource.url(),
                            dataSource.user(),
                            dataSource.password(),
                            dataSource.strategy())
                            .build());
        }

        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return DataSourceContextHolder.getCurrentDataSource();
            }
        };
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(targetDataSources.values().iterator().next());
        return routingDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public DataSourceBuilder<?> initializeDataSourceBuilder(String url, String user, String password, String strategy) {
        return DataSourceBuilder.create()
                .url(url)
                .username(user)
                .password(password)
                .driverClassName(getDriverClassName(strategy));
    }

    private String getDriverClassName(String strategy) {
        return switch (strategy) {
            case "postgres" -> "org.postgresql.Driver";
            case "mysql" -> "com.mysql.cj.jdbc.Driver";
            default -> throw new IllegalArgumentException("Unsupported strategy: " + strategy);
        };
    }
}

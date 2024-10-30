package com.zeecoder.comparus.configuration.properties_set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;


@ConfigurationProperties(prefix = "")
public record DataSourceProperties(List<DataSet> dataSources){
}

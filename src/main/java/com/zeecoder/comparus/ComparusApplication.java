package com.zeecoder.comparus;

import com.zeecoder.comparus.configuration.properties_set.DataSourceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DataSourceProperties.class)
@ConfigurationPropertiesScan
public class ComparusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComparusApplication.class, args);
	}

}

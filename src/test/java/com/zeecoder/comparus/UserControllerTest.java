package com.zeecoder.comparus;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.zeecoder.comparus.configuration.properties_set.DataSet;
import com.zeecoder.comparus.configuration.properties_set.DataSourceProperties;
import com.zeecoder.comparus.configuration.properties_set.TableSet;
import com.zeecoder.comparus.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    DataSourceProperties properties;

    private static final int POSTGRES_PORT_1 = 5432;
    private static final int POSTGRES_PORT_2 = 5433;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresContainer1 = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test1")
            .withUsername("testuser")
            .withPassword("testpass")
            .withExposedPorts(POSTGRES_PORT_1) //5432
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
            new HostConfig().withPortBindings(
                    new PortBinding(Ports.Binding.bindPort(POSTGRES_PORT_1), //5432
                    new ExposedPort(POSTGRES_PORT_1)) //5432
            )));

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresContainer2 = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test2")
            .withUsername("testuser")
            .withPassword("testpass")
            .withExposedPorts(POSTGRES_PORT_1) //5432
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(POSTGRES_PORT_2), //5433
                            new ExposedPort(POSTGRES_PORT_1)) // 5432
            )));

    @Test
    public void connectionEstablished(){
        assertThat(postgresContainer1.isCreated()).isTrue();
        assertThat(postgresContainer2.isCreated()).isTrue();
    }

    @Test
    public void shouldReturnAllUsersFromAllDataSources() {
        List<Users> expectedUserFromPropertiesMapping = properties.dataSources().stream()
                .map(DataSet::mapping)
                .map(this::toUser)
                .toList();

        ResponseEntity<List<Users>> response = restTemplate.exchange("/api/v1/users", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0);
        List<Users> users = response.getBody();

        assertThat(users).allSatisfy(user -> {
            assertThat(user.getId()).isNotNull();
            assertThat(user.getName()).isNotEmpty();
        });

        Assertions.assertThat(response.getBody())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedUserFromPropertiesMapping);
    }

    private Users toUser(TableSet set){
        return Users.builder()
                .id(set.id())
                .username(set.username())
                .name(set.name())
                .surname(set.surname())
                .build();
    }
}


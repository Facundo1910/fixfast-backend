package com.fixfast.backend.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final Environment environment;

    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        String url = dataSourceProperties.getUrl();

        if (url == null || url.isBlank()) {
            url = environment.getProperty("SPRING_DATASOURCE_URL",
                    environment.getProperty("DATABASE_URL"));
        }

        if (url != null && url.startsWith("mysql://")) {
            url = convertToJdbcUrl(url);
        }

        if (url != null) {
            dataSourceProperties.setUrl(url);
        }

        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    private String convertToJdbcUrl(String url) {
        String jdbcUrl = url.replaceFirst("^mysql://", "jdbc:mysql://");

        StringBuilder builder = new StringBuilder(jdbcUrl);
        boolean hasQuery = jdbcUrl.contains("?");

        if (!jdbcUrl.contains("sslMode")) {
            builder.append(hasQuery ? "&" : "?").append("sslMode=REQUIRED");
            hasQuery = true;
        }

        if (!jdbcUrl.contains("allowPublicKeyRetrieval")) {
            builder.append(hasQuery ? "&" : "?").append("allowPublicKeyRetrieval=true");
            hasQuery = true;
        }

        if (!jdbcUrl.contains("serverTimezone")) {
            builder.append(hasQuery ? "&" : "?").append("serverTimezone=UTC");
        }

        return builder.toString();
    }
}


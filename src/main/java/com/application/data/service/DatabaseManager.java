package com.application.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class DatabaseManager {
    private static JdbcTemplate jdbcTemplate = new JdbcTemplate();


    @Autowired
    public DatabaseManager(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static DatabaseService getDatabaseService() throws DatabaseConnectionException {
        return DatabaseService.getInstance(jdbcTemplate);
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}

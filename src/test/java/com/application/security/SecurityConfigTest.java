package com.application.security;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecurityConfigTest {

    @Test
    public void testSecurityConfigConstructor() {
        // Create an instance using the default constructor and assert that the object is not null
        SecurityConfig securityConfig = new SecurityConfig(new JdbcTemplate());
        assertNotNull(securityConfig);
    }

}
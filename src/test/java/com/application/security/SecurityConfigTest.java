package com.application.security;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class SecurityConfigTest {

    @Test
    public void testSecurityConfigConstructor() {
        // Create a mock JdbcTemplate and PasswordEncoder
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        // Create an instance using the updated constructor and assert that the object is not null
        SecurityConfig securityConfig = new SecurityConfig(jdbcTemplate, passwordEncoder);
        assertNotNull(securityConfig);
    }

}

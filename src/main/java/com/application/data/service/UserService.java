package com.application.data.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First get user id using username
        String idQuery = "SELECT user_id FROM users WHERE username = ?";
        Integer userId = jdbcTemplate.queryForObject(idQuery, new Object[]{username}, Integer.class);

        if (userId == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Then use the user id to get the role and password hash
        String query = "SELECT logingetusernamepasswordhash(?), getcurrentuserrole(?)";
        Map<String, Object> rolePassMap = jdbcTemplate.queryForMap(query, username, userId);

        if (rolePassMap.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Get user details using user id
        query = "SELECT * FROM getcurrentuserinfo(?)";
        Map<String, Object> userMap = jdbcTemplate.queryForMap(query, userId);

        if (userMap.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(userMap.get("username").toString())
                .password("{bcrypt}" + rolePassMap.get("passwordhash").toString())
                .roles("ROLE_" + rolePassMap.get("role_name").toString().toUpperCase())
                .build();
    }

}

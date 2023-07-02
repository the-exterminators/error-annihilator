package com.application.security;

import com.application.data.service.UserService;
import com.application.security.PasswordEncoderConfig;
import com.application.views.LoginOverlayHeader;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private final JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    /**
     * Only an empty constructor needed since VaadinWebSecurity already requires a UserService dependency
     * there is no need to explicitly inject it again in the SecurityConfig constructor.
    */
    SecurityConfig(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure authorization rules for HTTP requests
        http.authorizeHttpRequests()
                // Since our logo is an svg file, allowing access for it here explicitly
                .requestMatchers("/images/*").permitAll();
        // Call the superclass's configure() method to apply any additional configuration
        super.configure(http);
        // Set the login view for the specified HTTP security configuration
        setLoginView(http, LoginOverlayHeader.class);
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(jdbcTemplate, passwordEncoder);
    }

}
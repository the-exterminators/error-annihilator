package com.application.security;

import com.application.views.LoginOverlayHeader;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Only an empty constructor needed since VaadinWebSecurity already requires a UserService dependency
     * there is no need to explicitly inject it again in the SecurityConfig constructor.
     */
    SecurityConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.jdbcAuthentication()
                .dataSource(jdbcTemplate.getDataSource())
                .usersByUsernameQuery("select username,passwordhash,is_active "
                        + "from users "
                        + "where username = ?")
                .authoritiesByUsernameQuery("select username,role_security "
                        + "from roles "
                        + "JOIN users USING (role_id) "
                        + "where username = ?")
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure authorization rules for HTTP requests
        http.authorizeHttpRequests()
                // Since our logo is an svg file, allowing access for it here explicitly
                .requestMatchers("/images/*").permitAll();
        // Call the superclass's configure() method to apply any additional configuration
        super.configure(http);
        // Set the login view for the specified HTTP security configuration
        /** HAS TO BE REINTEGRATED ONCE LOGINVIEW ADDED!!! */
        setLoginView(http, LoginOverlayHeader.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
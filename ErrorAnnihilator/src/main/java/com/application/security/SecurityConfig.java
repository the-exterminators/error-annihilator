package com.application.security;

import com.application.views.LoginOverlayHeader;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    /**
     * Only an empty constructor needed since VaadinWebSecurity already requires a UserDetailsService dependency
     * there is no need to explicitly inject it again in the SecurityConfig constructor.
    */
    SecurityConfig() {};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure authorization rules for HTTP requests
        http.authorizeHttpRequests()
                // Since our logo is an svg file, allowing access for it here explicitly
                .requestMatchers("/images/*.svg").permitAll();
        // Call the superclass's configure() method to apply any additional configuration
        super.configure(http);
        // Set the login view for the specified HTTP security configuration
        /** HAS TO BE REINTEGRATED ONCE LOGINVIEW ADDED!!! */
        setLoginView(http, LoginOverlayHeader.class);
    }


    /**
     * In-memory credentials to be changed later on
     *      username = admin
     *      password = password
     *      roles = USER + ADMIN
    */

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

}
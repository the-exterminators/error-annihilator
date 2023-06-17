package com.application.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private AuthenticationContext authenticationContext;

    public SecurityService() {};

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    //Retrieves the authenticated user details.
    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    //Logs out the currently authenticated user.
    public void logout() {
        authenticationContext.logout();
    }
}

package com.application.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecurityServiceTest {


    @Test
    public void testDefaultConstructor() {
        SecurityService securityService = new SecurityService();
        assertNotNull(securityService);
    }

    @Test
    public void testConstructorWithAuthenticationContext() {
        // Create a mock AuthenticationContext object
        AuthenticationContext authenticationContext = Mockito.mock(AuthenticationContext.class);

        // Create the SecurityService instance with the mock AuthenticationContext
        SecurityService securityService = new SecurityService(authenticationContext);

        // Assert that the securityService instance is not null
        assertNotNull(securityService);
    }


    private AuthenticationContext authenticationContext;
    private SecurityService securityService;

    @BeforeEach
    public void setup() {
        authenticationContext = Mockito.mock(AuthenticationContext.class);
        securityService = new SecurityService(authenticationContext);
    }

    @Test
    public void testLogout() {
        // Invoke the logout() method
        securityService.logout();

        // Verify that the logout() method of authenticationContext was called
        Mockito.verify(authenticationContext).logout();
    }
}

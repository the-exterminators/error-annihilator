package com.application.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testDefaultConstructor() {
        User user = new User();
        Assertions.assertEquals("", user.getFirstName());
        Assertions.assertEquals("", user.getLastName());
        Assertions.assertEquals("", user.getUserName());
        Assertions.assertEquals("", user.getEmail());
        Assertions.assertEquals("", user.getDummyPassword());
        Assertions.assertEquals("", user.getUserRole());
        Assertions.assertNull(user.getCreatedTickets());
        Assertions.assertNull(user.getTicket());
    }

    @Test
    public void testParameterizedConstructor() {
        String firstName = "John";
        String lastName = "Doe";
        String userName = "johndoe";
        String email = "john.doe@example.com";
        String dummyPassword = "password123";
        String userRole = "user";

        User user = new User(firstName, lastName, userName, email, dummyPassword, userRole);
        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
        Assertions.assertEquals(userName, user.getUserName());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(dummyPassword, user.getDummyPassword());
        Assertions.assertEquals(userRole, user.getUserRole());
        Assertions.assertNull(user.getCreatedTickets());
        Assertions.assertNull(user.getTicket());
    }
}

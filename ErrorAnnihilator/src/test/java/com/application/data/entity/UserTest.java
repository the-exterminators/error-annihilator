package com.application.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    @Test
    public void testGetterSetterMethods() {
        User user = new User();

        String firstName = "John";
        user.setFirstName(firstName);
        Assertions.assertEquals(firstName, user.getFirstName());

        String lastName = "Doe";
        user.setLastName(lastName);
        Assertions.assertEquals(lastName, user.getLastName());

        String userName = "johndoe";
        user.setUserName(userName);
        Assertions.assertEquals(userName, user.getUserName());

        String email = "john.doe@example.com";
        user.setEmail(email);
        Assertions.assertEquals(email, user.getEmail());

        String dummyPassword = "password123";
        user.setDummyPassword(dummyPassword);
        Assertions.assertEquals(dummyPassword, user.getDummyPassword());

        String userRole = "user";
        user.setUserRole(userRole);
        Assertions.assertEquals(userRole, user.getUserRole());

        List<Ticket> createdTickets = new ArrayList<>();
        user.setCreatedTickets(createdTickets);
        Assertions.assertEquals(createdTickets, user.getCreatedTickets());

        Ticket ticket = new Ticket();
        user.setTicket(ticket);
        Assertions.assertEquals(ticket, user.getTicket());
    }
}

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
}

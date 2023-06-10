package com.application.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketCommentTest {
    @Test
    public void testDefaultConstructor() {
        TicketComment comment = new TicketComment();
        Assertions.assertNull(comment.getCommentText());
        Assertions.assertNull(comment.getCommentAuthor());
        Assertions.assertNull(comment.getTicket());
    }
}

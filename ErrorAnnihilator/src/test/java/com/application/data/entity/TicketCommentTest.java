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

    @Test
    public void testParameterizedConstructor() {
        String commentText = "This is a comment.";
        User commentAuthor = new User();
        Ticket ticket = new Ticket();

        TicketComment comment = new TicketComment(commentText, commentAuthor, ticket);

        Assertions.assertEquals(commentText, comment.getCommentText());
        Assertions.assertEquals(commentAuthor, comment.getCommentAuthor());
        Assertions.assertEquals(ticket, comment.getTicket());
    }
}

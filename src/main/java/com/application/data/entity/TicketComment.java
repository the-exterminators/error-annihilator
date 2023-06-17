package com.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class TicketComment extends AbstractEntity {

    public TicketComment() {

    };

    public TicketComment(String commentText, User commentAuthor, Ticket ticket) {
        this.commentText = commentText;
        this.commentAuthor = commentAuthor;
        this.ticket = ticket;
    }

    private String commentText;

    @ManyToOne
    private User commentAuthor;

    @ManyToOne
    private Ticket ticket;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(User commentAuthor) {
        this.commentAuthor = commentAuthor;
    }
}
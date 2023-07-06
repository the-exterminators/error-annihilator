package com.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;

@Entity
public class TicketComment extends AbstractEntity {

    public TicketComment() {

    };

    public TicketComment(String commentText, User commentAuthor, Ticket ticket) {
        this.commentText = commentText;
        this.commentAuthor = commentAuthor;
        this.ticket = ticket;
    }

    public TicketComment(Integer comment_id, String commentText, User commentAuthor, Ticket ticket) {
        this.comment_id = comment_id;
        this.commentText = commentText;
        this.commentAuthor = commentAuthor;
        this.ticket = ticket;
    }

    public TicketComment(String commentText, User commentAuthor, Ticket ticket, Timestamp created) {
        this.commentText = commentText;
        this.commentAuthor = commentAuthor;
        this.ticket = ticket;
        this.created = created;
    }

    public TicketComment(Integer comment_id, String commentText, User commentAuthor, Ticket ticket, Timestamp created) {
        this.comment_id = comment_id;
        this.commentText = commentText;
        this.commentAuthor = commentAuthor;
        this.ticket = ticket;
        this.created = created;
    }

    private Integer comment_id;

    private String commentText;

    private Timestamp created;

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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long id) {
        super.setId(id);
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
package com.project.GaymMagaz.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int ID;

    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "authorID")
    private Users author;

    @Column(name = "content", length = 300)
    private String content;

    @Column(name = "commentDate")
    private Date commentDate;

    public Comment() {
    }

    public Comment(Game game, Users author, String content, Date commentDate) {
        this.game = game;
        this.author = author;
        this.content = content;
        this.commentDate = commentDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}

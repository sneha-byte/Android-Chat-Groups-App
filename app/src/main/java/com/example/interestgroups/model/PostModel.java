package com.example.interestgroups.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class PostModel {
    private String user;
    private int likes;
    private String content;

    @ServerTimestamp
    private Date time;

    public PostModel() {
        //  empty constructor
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}

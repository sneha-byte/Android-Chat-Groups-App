package com.example.interestgroups.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class PostModel implements Serializable {
    private String id;          // Firestore document ID
    private String groupId;     // The group this post belongs to
    private String userId;      // User ID of creator
    private String userEmail;   // Email of creator
    private String content;     // Post content
    private int likes;          // Like count (optional)
    private Timestamp timestamp;// Time of post

    // Empty constructor required for Firestore
    public PostModel() {}

    public PostModel(String id, String groupId, String userId, String userEmail, String content, int likes, Timestamp timestamp) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.content = content;
        this.likes = likes;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}

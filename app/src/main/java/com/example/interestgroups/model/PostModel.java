package com.example.interestgroups.model;

import com.google.firebase.Timestamp;
import java.io.Serializable;

/**
 * PostModel represents a single post in a group.
 * Implements Serializable so it can be passed in Intents.
 */
public class PostModel implements Serializable {

    private String id;          // Firestore document ID
    private String userId;      // ID of the user who created the post
    private String userEmail;   // Email of the user
    private String content;     // Content of the post
    private int likes;          // Number of likes (default: 0)
    private Timestamp timestamp; // When the post was created
    private String groupId;     // ID of the group this post belongs to

    // Empty constructor for Firestore
    public PostModel() {}

    // Constructor for easy post creation
    public PostModel(String userId, String userEmail, String content, int likes, Timestamp timestamp, String groupId) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.content = content;
        this.likes = likes;
        this.timestamp = timestamp;
        this.groupId = groupId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
}

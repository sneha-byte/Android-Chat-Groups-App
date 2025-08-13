package com.example.interestgroups.model;

import com.google.firebase.Timestamp;

public class MessageModel {
    public String senderId;
    public String text;
    public Timestamp timestamp;

    // firestore deserialization
    public MessageModel() {}

    public MessageModel(String senderId, String text, Timestamp timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    // getters and setters
    public String getSenderId() {
        return senderId;
    }
    public String getText() {
        return text;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

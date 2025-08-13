package com.example.interestgroups.model;

public class User {
    private String uid;
    private String email;
    private String displayName;
    private String profilePicUrl;

    public User() {} // Firestore requires empty constructor

    public User(String uid, String email, String displayName, String profilePicUrl) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.profilePicUrl = profilePicUrl;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }
}

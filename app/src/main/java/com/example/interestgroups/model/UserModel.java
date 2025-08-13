package com.example.interestgroups.model;

public class UserModel {
    private String uid;
    private String displayName;
    private String email;
    private String profilePicUrl;

    // Empty constructor needed for Firestore
    public UserModel() {}

    public UserModel(String uid, String displayName, String email, String profilePicUrl) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }
}

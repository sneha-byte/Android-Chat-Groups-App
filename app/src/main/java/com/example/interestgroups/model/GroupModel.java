package com.example.interestgroups.model;

import java.io.Serializable;
import java.util.List;

public class GroupModel implements Serializable {

    private String name; // lowercase
    private String id;
    private List<String> members;

    public GroupModel() {
        // Required for Firestore deserialization
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}

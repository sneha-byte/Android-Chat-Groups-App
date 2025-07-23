package com.example.interestgroups.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Adapter to show a list of groups in a RecyclerView.
 * Each item shows the group name and a Join/Leave button.
 * Handles user clicks on both the entire item and the Join/Leave button.
 */
public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    // List of groups to display
    private final List<GroupModel> groupList;

    // Interface to notify the Activity of user actions (clicks)
    private final OnGroupActionListener listener;

    /**
     * Listener interface for group item click and join/leave button click
     */
    public interface OnGroupActionListener {
        // Called when user taps the group item (to open group detail)
        void onGroupClick(GroupModel group);

        // Called when user taps the Join or Leave button
        // isJoining = true if user wants to join, false if leave
        void onJoinLeaveClick(GroupModel group, boolean isJoining);
    }

    // Constructor receives the groups list and the listener for actions
    public GroupsAdapter(List<GroupModel> groupList, OnGroupActionListener listener) {
        this.groupList = groupList;
        this.listener = listener;
    }

    // Creates the view for each list item by inflating the XML layout
    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_group.xml layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    // Binds data to each item view at the given position
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel group = groupList.get(position);

        // Set the group name text
        holder.groupName.setText(group.getName());

        // Get current logged-in user email to check membership
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser != null ? currentUser.getEmail() : "";

        // Check if current user is a member of this group
        boolean isMember = group.getMembers() != null && group.getMembers().contains(email);

        // Set button text accordingly
        holder.btnJoinLeave.setText(isMember ? "Leave" : "Join");

        // When user taps the whole group item, notify listener (to open detail)
        holder.itemView.setOnClickListener(v -> listener.onGroupClick(group));

        // When user taps Join/Leave button, notify listener with intent (join or leave)
        holder.btnJoinLeave.setOnClickListener(v -> listener.onJoinLeaveClick(group, !isMember));
    }

    // Returns the total number of groups in the list
    @Override
    public int getItemCount() {
        return groupList.size();
    }

    /**
     * ViewHolder class caches the views for a group item for smooth scrolling.
     */
    static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;     // TextView for showing group name
        Button btnJoinLeave;    // Button for Join/Leave action

        GroupViewHolder(View itemView) {
            super(itemView);

            // Find views by ID inside item layout
            groupName = itemView.findViewById(R.id.textGroupName);
            btnJoinLeave = itemView.findViewById(R.id.btnJoinLeaveGroup);
        }
    }
}

package com.example.interestgroups.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.GroupsAdapter;
import com.example.interestgroups.model.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import android.widget.EditText;

import java.util.List;

public class GroupsActivity extends AppCompatActivity implements GroupsAdapter.OnGroupActionListener {

    private RecyclerView recyclerGroups;
    private Button btnCreateGroup;
    private GroupsAdapter groupsAdapter;
    private List<GroupModel> groupList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        recyclerGroups = findViewById(R.id.recyclerGroups);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        groupsAdapter = new GroupsAdapter(groupList, this);
        recyclerGroups.setLayoutManager(new LinearLayoutManager(this));
        recyclerGroups.setAdapter(groupsAdapter);

        btnCreateGroup.setOnClickListener(v -> showCreateGroupDialog());

        loadGroups();
    }

    /**
     * Show dialog to create a new group
     */
    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Group");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_create_group, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Create", (dialog, which) -> {
            EditText input = customLayout.findViewById(R.id.editGroupName);
            String groupName = input.getText().toString().trim();

            if (TextUtils.isEmpty(groupName)) {
                Toast.makeText(this, "Group name cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                createGroup(groupName);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    /**
     * Create a group in Firestore
     */
    private void createGroup(String groupName) {
        GroupModel group = new GroupModel();
        group.setName(groupName);
        group.setMembers(new ArrayList<>());
        group.getMembers().add(user.getEmail());

        db.collection("groups")
                .add(group)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Group created!", Toast.LENGTH_SHORT).show();
                    openGroupDetails(docRef.getId(), groupName);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to create group", Toast.LENGTH_SHORT).show()
                );
    }

    /**
     * Load all groups from Firestore
     */
    private void loadGroups() {
        db.collection("groups")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(GroupsActivity.this, "Error loading groups", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    groupList.clear();
                    for (var doc : value.getDocuments()) {
                        GroupModel group = doc.toObject(GroupModel.class);
                        group.setId(doc.getId());
                        groupList.add(group);
                    }
                    groupsAdapter.notifyDataSetChanged();
                });
    }

    /**
     * Open Group Detail
     */
    private void openGroupDetails(String groupId, String groupName) {
        Intent intent = new Intent(this, GroupDetailActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        startActivity(intent);
    }

    @Override
    public void onGroupClick(GroupModel group) {
        openGroupDetails(group.getId(), group.getName());
    }

    @Override
    public void onJoinLeaveClick(GroupModel group, boolean isJoining) {
        // Automatically go to group details after clicking Join
        if (isJoining) {
            openGroupDetails(group.getId(), group.getName());
        }
    }
}

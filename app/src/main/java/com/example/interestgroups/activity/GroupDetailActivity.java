package com.example.interestgroups.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.PostAdapter;
import com.example.interestgroups.model.GroupModel;
import com.example.interestgroups.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    private TextView textGroupName;
    private Button btnJoinLeave;
    private RecyclerView recyclerPosts;

    private String groupId;
    private GroupModel group;
    private FirebaseFirestore db;
    private FirebaseUser user;

    private PostAdapter postAdapter;
    private List<PostModel> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        textGroupName = findViewById(R.id.textGroupNameDetail);
        btnJoinLeave = findViewById(R.id.btnJoinLeave);
        recyclerPosts = findViewById(R.id.recyclerPosts);

        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {
            Toast.makeText(this, "Group ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        setupRecycler();
        fetchGroupData();
    }

    private void setupRecycler() {
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);
        recyclerPosts.setAdapter(postAdapter);
    }

    private void fetchGroupData() {
        db.collection("groups").document(groupId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        group = document.toObject(GroupModel.class);
                        if (group != null) {
                            group.setId(document.getId());
                            textGroupName.setText(group.getName());
                            updateJoinLeaveButton();
                            fetchChats();
                        } else {
                            Toast.makeText(this, "Error loading group data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Group not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load group", Toast.LENGTH_SHORT).show();
                    Log.e("GroupDetailActivity", "Fetch failed", e);
                });
    }

    private void updateJoinLeaveButton() {
        if (user == null) return;

        String email = user.getEmail();
        List<String> members = group.getMembers();
        if (members == null) members = new ArrayList<>();

        if (members.contains(email)) {
            btnJoinLeave.setText("Leave Group");
            btnJoinLeave.setOnClickListener(v -> leaveGroup());
        } else {
            btnJoinLeave.setText("Join Group");
            btnJoinLeave.setOnClickListener(v -> joinGroup());
        }
    }

    private void joinGroup() {
        List<String> members = new ArrayList<>(group.getMembers() != null ? group.getMembers() : new ArrayList<>());
        members.add(user.getEmail());
        group.setMembers(members);

        db.collection("groups").document(groupId)
                .set(group)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Joined group", Toast.LENGTH_SHORT).show();
                    updateJoinLeaveButton();
                });
    }

    private void leaveGroup() {
        List<String> members = new ArrayList<>(group.getMembers() != null ? group.getMembers() : new ArrayList<>());
        members.remove(user.getEmail());
        group.setMembers(members);

        db.collection("groups").document(groupId)
                .set(group)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Left group", Toast.LENGTH_SHORT).show();
                    updateJoinLeaveButton();
                });
    }

    private void fetchChats() {
        db.collection("groups")
                .document(groupId)
                .collection("chats")
                .orderBy("timestamp")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.w("GroupDetailActivity", "Listen failed.", e);
                        return;
                    }

                    postList.clear();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            PostModel post = doc.toObject(PostModel.class);
                            if (post != null) {
                                postList.add(post);
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                    }
                });
    }
}

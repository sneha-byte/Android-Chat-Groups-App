package com.example.interestgroups.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.GroupsAdapter;
import com.example.interestgroups.model.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    private RecyclerView groupsRecycler;
    private GroupsAdapter adapter;
    private ArrayList<GroupModel> groupList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupsRecycler = findViewById(R.id.recyclerGroups);
        groupsRecycler.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        adapter = new GroupsAdapter(groupList, new GroupsAdapter.OnGroupActionListener() {
            @Override
            public void onGroupClick(GroupModel group) {
                Intent intent = new Intent(GroupsActivity.this, GroupDetailActivity.class);
                intent.putExtra("groupId", group.getId());
                intent.putExtra("groupName", group.getName());
                startActivity(intent);
            }

            @Override
            public void onJoinLeaveClick(GroupModel group, boolean isJoining) {
                if (isJoining) {
                    joinGroup(group);
                } else {
                    leaveGroup(group);
                }
            }
        });
        groupsRecycler.setAdapter(adapter);

        fetchGroups();
    }

    private void fetchGroups() {
        db.collection("groups")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        groupList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            GroupModel group = doc.toObject(GroupModel.class);
                            groupList.add(group);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error fetching groups", Toast.LENGTH_SHORT).show();
                        Log.e("GroupsActivity", "Fetch failed", task.getException());
                    }
                });
    }

    private void joinGroup(GroupModel group) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        List<String> members = group.getMembers() != null ? new ArrayList<>(group.getMembers()) : new ArrayList<>();
        if (!members.contains(currentUser.getEmail())) {
            members.add(currentUser.getEmail());
        }
        group.setMembers(members);

        db.collection("groups").document(group.getId())
                .set(group)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Joined " + group.getName(), Toast.LENGTH_SHORT).show();
                    fetchGroups();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to join group", Toast.LENGTH_SHORT).show());
    }

    private void leaveGroup(GroupModel group) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        List<String> members = group.getMembers() != null ? new ArrayList<>(group.getMembers()) : new ArrayList<>();
        members.remove(currentUser.getEmail());
        group.setMembers(members);

        db.collection("groups").document(group.getId())
                .set(group)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Left " + group.getName(), Toast.LENGTH_SHORT).show();
                    fetchGroups();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to leave group", Toast.LENGTH_SHORT).show());
    }
}

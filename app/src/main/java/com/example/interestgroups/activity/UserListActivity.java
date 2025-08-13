package com.example.interestgroups.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list); // UI file for this screen

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Adapter setup
        userAdapter = new UserAdapter(userList, new UserAdapter.OnChatClickListener() {
            @Override
            public void onChatClick(User user) {
                // Start chat activity with selected user
                Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
                intent.putExtra("otherUserId", user.getUid()); // Pass UID
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(userAdapter);

        loadUsers();
    }

    private void loadUsers() {
        String currentUserId = auth.getCurrentUser().getUid();

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    User user = doc.toObject(User.class);

                    // Skip the current logged-in user
                    if (!user.getUid().equals(currentUserId)) {
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

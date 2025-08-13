package com.example.interestgroups.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.UserAdapter;
import com.example.interestgroups.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        loadUsers();
    }

    private void loadUsers() {
        String currentUserId = auth.getCurrentUser().getUid();
        CollectionReference usersRef = db.collection("users");

        usersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            userList.clear();
            for (var doc : queryDocumentSnapshots) {
                User user = doc.toObject(User.class);
                // Exclude current logged-in user from the list
                if (!user.getUid().equals(currentUserId)) {
                    userList.add(user);
                }
            }
            userAdapter.notifyDataSetChanged();
        });
    }
}

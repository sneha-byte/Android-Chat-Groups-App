package com.example.interestgroups.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interestgroups.R;
import com.example.interestgroups.model.PostModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostActivity extends AppCompatActivity {

    private EditText postContentInput;
    private Button submitBtn;

    private FirebaseUser currentUser;
    private FirebaseFirestore store;

    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Get current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        store = FirebaseFirestore.getInstance();

        // Get group ID from intent
        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {
            Toast.makeText(this, "Group ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Link UI
        postContentInput = findViewById(R.id.editTextPost);
        submitBtn = findViewById(R.id.submitPost);

        // Handle post submission
        submitBtn.setOnClickListener(view -> {
            String content = postContentInput.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                createPost(content);
            }
        });
    }

    private void redirectToLogin() {
        FirebaseAuth.getInstance().signOut();
        Intent loginIntent = new Intent(PostActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void createPost(String content) {
        PostModel postModel = new PostModel();
        postModel.setUserEmail(currentUser.getEmail());
        postModel.setUserId(currentUser.getUid());
        postModel.setContent(content);
        postModel.setGroupId(groupId);
        postModel.setLikes(0);
        postModel.setTimestamp(Timestamp.now());

        // Save to Firestore
        store.collection("groups")
                .document(groupId)
                .collection("chats")
                .add(postModel)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(error -> {
                    Log.e("PostActivity", "Error creating post", error);
                    Toast.makeText(this, "Error creating post", Toast.LENGTH_SHORT).show();
                });
    }
}

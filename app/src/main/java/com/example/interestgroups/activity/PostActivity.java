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
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostActivity extends AppCompatActivity {
    private EditText postBtn;
    private Button submitBtn;

    private FirebaseUser currentUser;

    private FirebaseFirestore Store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(PostActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        Store = FirebaseFirestore.getInstance();

        //Link UI
        postBtn = findViewById(R.id.editTextPost);
        submitBtn = findViewById(R.id.submitPost);

        submitBtn.setOnClickListener(view -> {
            String content = postBtn.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                createPost(content);
            }
        });

    }

    private void createPost(String content) {
        PostModel postModel = new PostModel();
        postModel.setUser(currentUser.getEmail());
        postModel.setContent(content);

        Store.collection("Chats").add(postModel)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Successfully Created Post", Toast.LENGTH_SHORT).show();
                    Intent postIntent = new Intent(PostActivity.this, HomeActivity.class);
                    startActivity(postIntent);
                    finish();
                })
                .addOnFailureListener(error -> {
                    Log.e("post error", error.toString());
                    Toast.makeText(this, "Error creating post", Toast.LENGTH_SHORT).show();
                });
    }
}

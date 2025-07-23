package com.example.interestgroups.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interestgroups.R;
import com.example.interestgroups.model.PostModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostDetailActivity extends AppCompatActivity {

    private EditText contentInput;
    private Button updateBtn, deleteBtn;
    private FirebaseFirestore store;

    private PostModel post;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        contentInput = findViewById(R.id.editTextPostContent);
        updateBtn = findViewById(R.id.btnUpdatePost);
        deleteBtn = findViewById(R.id.btnDeletePost);
        store = FirebaseFirestore.getInstance();

        // Get post and groupId from intent
        post = (PostModel) getIntent().getSerializableExtra("post");
        groupId = getIntent().getStringExtra("groupId");

        if (post != null && groupId != null) {
            contentInput.setText(post.getContent());
        } else {
            Toast.makeText(this, "Post data missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateBtn.setOnClickListener(v -> updatePost());
        deleteBtn.setOnClickListener(v -> deletePost());
    }

    private void updatePost() {
        String updatedContent = contentInput.getText().toString().trim();
        if (TextUtils.isEmpty(updatedContent)) {
            Toast.makeText(this, "Content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        store.collection("groups")
                .document(groupId)
                .collection("chats")
                .document(post.getId())
                .update("content", updatedContent)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void deletePost() {
        store.collection("groups")
                .document(groupId)
                .collection("chats")
                .document(post.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                });
    }
}

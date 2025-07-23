package com.example.interestgroups.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.PostAdapter;
import com.example.interestgroups.model.PostModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    private TextView textGroupName;
    private RecyclerView recyclerPosts;
    private EditText editNewPost;
    private Button btnSendPost;

    private FirebaseFirestore db;
    private FirebaseUser user;

    private String groupId;
    private String groupName;

    private List<PostModel> postList = new ArrayList<>();
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        // Initialize views
        textGroupName = findViewById(R.id.textGroupNameDetail);
        recyclerPosts = findViewById(R.id.recyclerPosts);
        editNewPost = findViewById(R.id.editNewPost);
        btnSendPost = findViewById(R.id.btnSendPost);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Get group info from Intent extras
        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");

        // Show group name
        textGroupName.setText(groupName);

        // Setup RecyclerView with layout manager and adapter
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);  // Only passing list if your PostAdapter constructor accepts just the list
        recyclerPosts.setAdapter(postAdapter);

        // Set click listener to send new post
        btnSendPost.setOnClickListener(v -> {
            String content = editNewPost.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            addNewPost(content);
        });

        // Start listening for posts updates
        listenForPosts();
    }

    private void listenForPosts() {
        if (groupId == null) {
            Toast.makeText(this, "Invalid group ID", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("groups")
                .document(groupId)
                .collection("chats")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(GroupDetailActivity.this, "Error loading posts", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value != null) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    PostModel post = dc.getDocument().toObject(PostModel.class);
                                    post.setId(dc.getDocument().getId());  // Store Firestore doc ID
                                    postList.add(post);

                                    postAdapter.notifyItemInserted(postList.size() - 1);
                                    recyclerPosts.scrollToPosition(postList.size() - 1);
                                }
                                // You could add support for MODIFIED and REMOVED here
                            }
                        }
                    }
                });
    }

    private void addNewPost(String content) {
        if (user == null) {
            Toast.makeText(this, "You must be logged in to post", Toast.LENGTH_SHORT).show();
            return;
        }
        if (groupId == null) {
            Toast.makeText(this, "Invalid group ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new post model object with current user info and content
        PostModel newPost = new PostModel(
                user.getUid(),           // user id
                user.getEmail(),         // user email
                content,                 // post content
                0,                       // initial likes count
                Timestamp.now()          // current time
        );

        // Add new post to Firestore under this group's chats subcollection
        db.collection("groups")
                .document(groupId)
                .collection("chats")
                .add(newPost)
                .addOnSuccessListener(docRef -> {
                    editNewPost.setText("");  // Clear input
                    Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show());
    }
}

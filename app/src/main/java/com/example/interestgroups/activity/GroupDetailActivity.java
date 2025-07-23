package com.example.interestgroups.activity;

import android.content.Intent;
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

        // UI References
        textGroupName = findViewById(R.id.textGroupNameDetail);
        recyclerPosts = findViewById(R.id.recyclerPosts);
        editNewPost = findViewById(R.id.editNewPost);
        btnSendPost = findViewById(R.id.btnSendPost);

        // Firebase
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Get Group ID and Name
        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");

        if (groupId == null) {
            Toast.makeText(this, "Group ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textGroupName.setText(groupName != null ? groupName : "Group");

        // RecyclerView Setup
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList, post -> openPostDetail(post));
        recyclerPosts.setAdapter(postAdapter);

        // Add post
        btnSendPost.setOnClickListener(v -> {
            String content = editNewPost.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            addNewPost(content);
        });
        // Start listening for posts
        listenForPosts();
    }

    /**
     * Opens PostDetailActivity for editing/deleting the clicked post.
     */
    private void openPostDetail(PostModel post) {
        Intent intent = new Intent(GroupDetailActivity.this, PostDetailActivity.class);
        intent.putExtra("post", post);  // post is Serializable
        startActivity(intent);
    }



    /**
     * Real-time listener for posts in the selected group.
     */
    private void listenForPosts() {
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
                        if (value == null) return;

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            PostModel post = dc.getDocument().toObject(PostModel.class);
                            post.setId(dc.getDocument().getId());
                            post.setGroupId(groupId);

                            switch (dc.getType()) {
                                case ADDED:
                                    postList.add(post);
                                    postAdapter.notifyItemInserted(postList.size() - 1);
                                    recyclerPosts.scrollToPosition(postList.size() - 1);
                                    break;
                                case MODIFIED:
                                    updatePostInList(post);
                                    break;
                                case REMOVED:
                                    removePostFromList(post.getId());
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * Add a new post to the current group's chats collection.
     */
    private void addNewPost(String content) {
        if (user == null) {
            Toast.makeText(this, "You must be logged in to post", Toast.LENGTH_SHORT).show();
            return;
        }

        PostModel newPost = new PostModel();
        newPost.setUserId(user.getUid());
        newPost.setUserEmail(user.getEmail());
        newPost.setContent(content);
        newPost.setLikes(0);
        newPost.setTimestamp(Timestamp.now());
        newPost.setGroupId(groupId);

        db.collection("groups")
                .document(groupId)
                .collection("chats")
                .add(newPost)
                .addOnSuccessListener(docRef -> {
                    editNewPost.setText("");
                    Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show());
    }

    /**
     * Update post in the local list when Firestore sends a MODIFIED event.
     */
    private void updatePostInList(PostModel updatedPost) {
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getId().equals(updatedPost.getId())) {
                postList.set(i, updatedPost);
                postAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * Remove post from the local list when Firestore sends a REMOVED event.
     */
    private void removePostFromList(String postId) {
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getId().equals(postId)) {
                postList.remove(i);
                postAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }
}

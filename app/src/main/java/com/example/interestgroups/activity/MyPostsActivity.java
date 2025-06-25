package com.example.interestgroups.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.PostAdapter;
import com.example.interestgroups.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseFirestore Store;
    private RecyclerView rV;
    private PostAdapter adapter;
    private List<Post> postList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Store = FirebaseFirestore.getInstance();

        postList = new ArrayList<>();
        adapter = new PostAdapter(postList);

        setUpRecyclerView();
        fetchPosts();
    }

    private void setUpRecyclerView() {
        rV = findViewById(R.id.myPostsRecyclerView);
        LinearLayoutManager lLM = new LinearLayoutManager(this);
        lLM.setOrientation(LinearLayoutManager.VERTICAL);
        rV.setLayoutManager(lLM);
        rV.setAdapter(adapter);
    }

    private void fetchPosts() {
        Store.collection("Chats")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    queryDocumentSnapshots.forEach(doc -> {
                        Post post = doc.toObject(Post.class);
                        postList.add(post);
                    });
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle errors, e.g., show a Toast
                });
    }
}

package com.example.interestgroups.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.adapter.PostAdapter;
import com.example.interestgroups.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyPostsActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseFirestore Store;
    private RecyclerView rV;
    private PostAdapter adapter;
    private ArrayList<PostModel> myPosts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Store = FirebaseFirestore.getInstance();

        rV = findViewById(R.id.myPostsRecyclerView);
        rV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(myPosts);
        rV.setAdapter(adapter);

        fetchPosts();
    }

    private void fetchPosts() {
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Store.collection("Chats")
                .whereEqualTo("user", currentUser.getEmail())
                .get()
                .addOnSuccessListener(posts -> {
                    myPosts.clear();
                    for (DocumentSnapshot dS : posts.getDocuments()) {
                        PostModel pM = dS.toObject(PostModel.class);
                        myPosts.add(pM);
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Data fetched successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to get posts", Toast.LENGTH_SHORT).show();
                });
    }
}

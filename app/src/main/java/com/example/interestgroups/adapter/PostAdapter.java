package com.example.interestgroups.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.PostModel;

import java.util.List;

/**
 * Adapter class that connects a list of PostModel items to the RecyclerView in MyPostsActivity.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostModel> posts;

    // Constructor that accepts the list of posts
    public PostAdapter(List<PostModel> posts) {
        this.posts = posts;
    }

    // Called when a new ViewHolder needs to be created (i.e., when scrolling)
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each individual item (from item_post.xml)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(v);
    }

    // Called to bind data to an existing ViewHolder (for a given position)
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = posts.get(position);

        // Display the email (user who posted)
        holder.user.setText(post.getUser());

        // Display the post content
        holder.content.setText(post.getContent());

        // Optionally display likes if available
        holder.likes.setText("Likes: " + post.getLikes());

        // You can format time if needed (if time is added to the model)
    }

    // Total number of items
    @Override
    public int getItemCount() {
        return posts.size();
    }

    /**
     * ViewHolder class that holds references to the views for each post item.
     */
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView user, content, likes;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link UI elements from item_post.xml
            user = itemView.findViewById(R.id.postUser);
            content = itemView.findViewById(R.id.postContent);
            likes = itemView.findViewById(R.id.postLikes);
        }
    }
}

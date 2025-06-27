package com.example.interestgroups.adapter;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.PostModel;

import java.util.Date;
import java.util.List;
import java.util.Locale;

// Adapter class that connects a list of PostModel items to the RecyclerView in MyPostsActivity
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostModel> posts;

    // Constructor that accepts the list of posts
    public PostAdapter(List<PostModel> posts) {
        this.posts = posts;
    }

    // Called when a new ViewHolder needs to be created
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each individual item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(v);
    }

    // Called to bind data to an existing ViewHolder
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = posts.get(position);

        // Display email
        holder.user.setText(post.getUser());

        // Display content
        holder.content.setText(post.getContent());

        //likes
        holder.likes.setText("Likes: " + post.getLikes());

        // Dates
        Date timestamp = post.getTime();
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = sdf.format(timestamp);
            holder.date.setText(formattedDate);
        } else {
            holder.date.setText("");
        }

        holder.itemView.setOnClickListener(v -> listener.onPostClick(post));
    }

    public interface OnPostClickListener {
        void onPostClick(PostModel post);
    }

    // Total number of items
    @Override
    public int getItemCount() {
        return posts.size();
    }

     // ViewHolder class that holds references to the views for each post item.
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView user, content, likes, date;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link UI elements from item_post.xml
            user = itemView.findViewById(R.id.postUser);
            content = itemView.findViewById(R.id.postContent);
            likes = itemView.findViewById(R.id.postLikes);
            date = itemView.findViewById(R.id.postDate);
        }
    }

    public interface OnPostClickListener {
        void onPostClick(PostModel post);
    }

    private final OnPostClickListener listener;

    public PostAdapter(List<PostModel> posts, OnPostClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }
}

package com.example.interestgroups.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.PostModel;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for showing posts in RecyclerView.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostModel> postList;

    // Constructor
    public PostAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for single post item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = postList.get(position);

        // Display post content
        holder.textContent.setText(post.getContent());

        // Display user email who posted
        holder.textUserEmail.setText(post.getUserEmail());

        // Format and display timestamp nicely
        Timestamp timestamp = post.getTimestamp();
        if (timestamp != null) {
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            holder.textTimestamp.setText(sdf.format(date));
        } else {
            holder.textTimestamp.setText("");
        }

        // Optionally, display likes count (you can add a TextView for that if needed)
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * ViewHolder class caches views for each post item for performance.
     */
    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textContent, textUserEmail, textTimestamp;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textContent = itemView.findViewById(R.id.textPostContent);
            textUserEmail = itemView.findViewById(R.id.textPostUserEmail);
            textTimestamp = itemView.findViewById(R.id.textPostTimestamp);
        }
    }
}

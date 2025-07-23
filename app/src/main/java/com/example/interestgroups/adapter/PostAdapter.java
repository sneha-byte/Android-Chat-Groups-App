package com.example.interestgroups.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.PostModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostModel> postList;

    public PostAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = postList.get(position);
        holder.textContent.setText(post.getContent());
        holder.textUserEmail.setText(post.getUserEmail());
        holder.textLikes.setText(String.valueOf(post.getLikes()));

        if (post.getTimestamp() != null) {
            String time = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                    .format(post.getTimestamp().toDate());
            holder.textTimestamp.setText(time);
        } else {
            holder.textTimestamp.setText("...");
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textContent, textUserEmail, textLikes, textTimestamp;

        PostViewHolder(View itemView) {
            super(itemView);
            textContent = itemView.findViewById(R.id.textPostContent);
            textUserEmail = itemView.findViewById(R.id.textPostUser);
            textLikes = itemView.findViewById(R.id.textPostLikes);
            textTimestamp = itemView.findViewById(R.id.textPostTime);
        }
    }
}

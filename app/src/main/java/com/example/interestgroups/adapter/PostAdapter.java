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
 * PostAdapter is responsible for showing a list of posts inside a RecyclerView.
 * It also allows clicking on a post to open details (via PostClickListener).
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostModel> postList;
    private PostClickListener listener;

    /**
     * Interface for handling post item clicks.
     */
    public interface PostClickListener {
        void onPostClick(PostModel post);
    }

    /**
     * Constructor for PostAdapter.
     *
     * @param postList The list of posts to display.
     * @param listener Listener for post clicks.
     */
    public PostAdapter(List<PostModel> postList, PostClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = postList.get(position);

        holder.textContent.setText(post.getContent());
        holder.textUserEmail.setText(post.getUserEmail());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPostClick(post);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * ViewHolder class that represents each post item layout.
     */
    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textContent, textUserEmail;

        PostViewHolder(View itemView) {
            super(itemView);
            textContent = itemView.findViewById(R.id.textPostContent);
            textUserEmail = itemView.findViewById(R.id.textPostUserEmail);
        }
    }
}

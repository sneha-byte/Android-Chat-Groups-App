package com.example.interestgroups.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.interestgroups.R;
import com.example.interestgroups.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(User user);
    }

    private List<User> userList;
    private OnChatClickListener listener;

    public UserAdapter(List<User> userList, OnChatClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false); // layout for each user row
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.textViewName.setText(user.getDisplayName());
        holder.textViewEmail.setText(user.getEmail());

        // Load profile picture if available
        if (user.getProfilePicUrl() != null && !user.getProfilePicUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(user.getProfilePicUrl())
                    .into(holder.imageViewProfile);
        } else {
            holder.imageViewProfile.setImageResource(R.drawable.ic_default_profile); // fallback image
        }

        // Chat button click listener
        holder.buttonChat.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewName, textViewEmail;
        Button buttonChat;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            buttonChat = itemView.findViewById(R.id.buttonChat);
        }
    }
}

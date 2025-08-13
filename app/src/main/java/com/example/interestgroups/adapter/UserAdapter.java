package com.example.interestgroups.adapter;

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

    private List<User> users;
    private OnChatClickListener listener;

    public UserAdapter(List<User> users, OnChatClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        holder.name.setText(user.getDisplayName());
        holder.email.setText(user.getEmail());

        // Load profile picture
        if (user.getProfilePicUrl() != null && !user.getProfilePicUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(user.getProfilePicUrl())
                    .into(holder.profileImage);
        } else {
            holder.profileImage.setImageResource(R.drawable.default_profile);
        }

        // Chat button click
        holder.btnChat.setOnClickListener(v -> listener.onChatClick(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        ImageView profileImage;
        Button btnChat;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textDisplayName);
            email = itemView.findViewById(R.id.textEmail);
            profileImage = itemView.findViewById(R.id.imageProfile);
            btnChat = itemView.findViewById(R.id.btnChat);
        }
    }
}

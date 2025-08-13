package com.example.interestgroups.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.MessageModel;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<MessageModel> messageList;
    private String currentUserId;

    public MessagesAdapter(List<MessageModel> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return 1; // Sent message
        } else {
            return 0; // Received message
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            // Sent message layout (right aligned)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
        } else {
            // Received message layout (left aligned)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        holder.textViewMessage.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}

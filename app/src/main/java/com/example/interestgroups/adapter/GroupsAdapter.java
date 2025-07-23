package com.example.interestgroups.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interestgroups.R;
import com.example.interestgroups.model.GroupModel;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private List<GroupModel> groupList;
    private OnGroupActionListener listener;

    public interface OnGroupActionListener {
        void onGroupClick(GroupModel group);
        void onJoinLeaveClick(GroupModel group, boolean isJoining);
    }

    public GroupsAdapter(List<GroupModel> groupList, OnGroupActionListener listener) {
        this.groupList = groupList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel group = groupList.get(position);
        holder.textGroupName.setText(group.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onGroupClick(group);
        });

        holder.btnJoin.setOnClickListener(v -> {
            if (listener != null) listener.onJoinLeaveClick(group, true);
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView textGroupName;
        Button btnJoin;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            textGroupName = itemView.findViewById(R.id.textGroupName);
            btnJoin = itemView.findViewById(R.id.btnJoin);
        }
    }
}

package com.annguyen.android.parti.party.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annguyen.android.parti.R;
import com.annguyen.android.parti.entities.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annguyen on 6/22/2017.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.CustomViewHolder>{

    List<User> userList;

    public MemberListAdapter() {
        userList = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View memberListView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_bubble, parent, false);

        return new CustomViewHolder(memberListView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        String userName = userList.get(position).getName();
        holder.memberName.setText(userName);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addUser(User user) {
        userList.add(user);
        notifyItemInserted(userList.size() - 1);
    }

    public void removeUser(String userKey) {
        for (int i = 0; i < userList.size(); ++i) {
            if (userList.get(i).getKey().equals(userKey)) {
                userList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void setUserList(List<User> users) {
        userList.addAll(users);
        notifyDataSetChanged();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.member_name)
        TextView memberName;
        @BindView(R.id.member_container)
        CardView memberContainer;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

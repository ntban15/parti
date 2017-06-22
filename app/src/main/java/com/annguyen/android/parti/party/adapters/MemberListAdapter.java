package com.annguyen.android.parti.party.adapters;

import android.content.Context;
import android.support.v7.content.res.AppCompatResources;
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

    private String hostKey;
    private String userKey;
    private List<User> userList;
    private Context context;

    public MemberListAdapter() {
        userList = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View memberListView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_bubble, parent, false);

        return new CustomViewHolder(memberListView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        User user = userList.get(position);
        holder.memberName.setText(user.getName());

        if (user.getKey().equals(userKey)) {
            holder.memberContainer.setBackgroundTintList(
                    AppCompatResources.getColorStateList(context, R.color.bubble_owner_color));
            holder.memberName.setTextColor(
                    AppCompatResources.getColorStateList(context, R.color.text_owner_color));
        }

        if (user.getKey().equals(hostKey)) {
            holder.memberContainer.setBackgroundTintList(
                    AppCompatResources.getColorStateList(context, R.color.bubble_host_color));
            holder.memberName.setTextColor(
                    AppCompatResources.getColorStateList(context, R.color.text_host_color));
        }
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

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}

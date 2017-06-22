package com.annguyen.android.parti.party.adapters;

import android.content.Context;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annguyen.android.parti.R;
import com.annguyen.android.parti.entities.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annguyen on 6/22/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.CustomViewHolder> {

    private String hostKey;
    private String userKey;
    private List<Message> messageList;
    private Context context;

    public MessagesAdapter() {
        this.messageList = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_bubble, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Message message = messageList.get(position);

        holder.sentBy.setText("Sent by " + message.getName());
        holder.msgTxt.setText(message.getText());

        LinearLayout.LayoutParams msgParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (message.getFrom().equals(userKey)) {
            msgParams.gravity = Gravity.END;
            holder.messageContainer.setBackgroundTintList(
                    AppCompatResources.getColorStateList(context, R.color.bubble_owner_color));
            holder.msgTxt.setTextColor(
                    AppCompatResources.getColorStateList(context, R.color.text_owner_color));
            holder.sentBy.setVisibility(View.GONE);
        }
        else {
            msgParams.gravity = Gravity.START;
        }

        if (message.getFrom().equals(hostKey)) {
            holder.messageContainer.setBackgroundTintList(
                    AppCompatResources.getColorStateList(context, R.color.bubble_host_color));
            holder.msgTxt.setTextColor(
                    AppCompatResources.getColorStateList(context, R.color.text_host_color));
        }

        holder.messageContainer.setLayoutParams(msgParams);
        holder.sentBy.setLayoutParams(msgParams);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_container)
        CardView messageContainer;
        @BindView(R.id.message_sent_by)
        TextView sentBy;
        @BindView(R.id.message_text)
        TextView msgTxt;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}

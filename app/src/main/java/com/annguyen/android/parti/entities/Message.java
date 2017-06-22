package com.annguyen.android.parti.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by annguyen on 6/22/2017.
 */

@IgnoreExtraProperties
public class Message {
    private String from;
    private String name;
    private String text;
    @Exclude
    private String msgKey;

    public Message() {}

    public Message(String from, String name, String text) {
        this.from = from;
        this.name = name;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Exclude
    public String getMsgKey() {
        return msgKey;
    }

    @Exclude
    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }
}

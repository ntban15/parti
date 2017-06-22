package com.annguyen.android.parti.party.events;

import com.annguyen.android.parti.entities.Message;
import com.annguyen.android.parti.entities.User;

import java.util.List;

/**
 * Created by annguyen on 6/22/2017.
 */

public class PartyEvent {
    public static final int REQUEST_USER_NAME_FAIL = 487;
    public static final int PARTICIPATE_FAIL = 1211;
    public static final int FINISH_PARTICIPATE = 101;
    public static final int NEW_MEMBER = 102;
    public static final int USER_REMOVED = 404;
    public static final int NEW_MESSAGE = 222;
    public static final int HOST_LEAVE_SUCCESS = 333;
    public static final int USER_LEAVE_SUCCESS = 444;
    public static final int PARTY_REMOVED = 555;
    public static final int REQUEST_HOST_ID_FAIL = 666;

    private int eventCode;
    private User newUser;
    private String userKey;
    private String hostKey;
    private Message newMessage;

    public PartyEvent(int eventCode) {
        this.eventCode = eventCode;
    }

    public PartyEvent(int eventCode, User newUser) {
        this.eventCode = eventCode;
        this.newUser = newUser;
    }

    public PartyEvent(int eventCode, String userKey) {
        this.eventCode = eventCode;
        this.userKey = userKey;
    }

    public PartyEvent(int eventCode, Message newMessage) {
        this.eventCode = eventCode;
        this.newMessage = newMessage;
    }

    public PartyEvent(int eventCode, String userKey, String hostKey) {
        this.eventCode = eventCode;
        this.userKey = userKey;
        this.hostKey = hostKey;
    }

    public int getEventCode() {
        return eventCode;
    }

    public User getNewUser() {
        return newUser;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getHostKey() {
        return hostKey;
    }

    public Message getNewMessage() {
        return newMessage;
    }
}

package com.annguyen.android.parti.party.ui;

import com.annguyen.android.parti.entities.Message;
import com.annguyen.android.parti.entities.User;

import java.util.List;

/**
 * Created by annguyen on 6/20/2017.
 */

public interface PartyView {

    void addNewMessage(Message message);

    void addNewMember(User user);

    void removeMember(String userKey);

    void goToMain();

    void hideInput();

    void showInput();

    void hideProgressBar();

    void showProgressBar();

    void setCurrentUserId(String userKey);

    void setHostId(String hostKey);

    void clearMsgInput();
}

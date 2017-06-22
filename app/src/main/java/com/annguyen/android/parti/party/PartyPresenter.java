package com.annguyen.android.parti.party;

/**
 * Created by annguyen on 6/20/2017.
 */

public interface PartyPresenter {
    void start();

    void participate(String partyKey);

    void stop();

    void sendMsg(String msg);

    void leaveGroup(boolean asHost);
}

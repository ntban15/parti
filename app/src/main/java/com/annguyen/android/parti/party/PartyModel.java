package com.annguyen.android.parti.party;

/**
 * Created by annguyen on 6/20/2017.
 */

public interface PartyModel {

    void sendMsg(String msg);

    void participate(String partyKey);

    void leaveGroup(boolean asHost);

    void listenToMembers();

    void stopListenToMembers();

    void listenToMessages();

    void stopListenToMessages();

    void listenToPartyRemoval();

    void stopListenToPartyRemoval();
}

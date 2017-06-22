package com.annguyen.android.parti.party;

import android.support.annotation.NonNull;

import com.annguyen.android.parti.entities.Message;
import com.annguyen.android.parti.entities.User;
import com.annguyen.android.parti.party.events.PartyEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annguyen on 6/22/2017.
 */

public class PartyModelImpl implements PartyModel {

    private String currentPartyKey;
    private String currentUserName;
    private String currentUserId;

    private ChildEventListener membersListener;
    private ChildEventListener messagesListener;
    private ChildEventListener partiesListener;

//    private List<Message> tmpMessageList;
//    private List<User> tmpUserList;

    EventBus eventBus;
    private FirebaseAuth auth;
    private DatabaseReference dataRef;

    public PartyModelImpl() {
        eventBus = EventBus.getDefault();
        auth = FirebaseAuth.getInstance();
        dataRef = FirebaseDatabase.getInstance().getReference();
//        tmpMessageList = new ArrayList<>();
//        tmpUserList = new ArrayList<>();
    }

    @Override
    public void participate(String partyKey) {
        //get currentPartyKey
        currentPartyKey = partyKey;
        //get current user
        currentUserId = auth.getCurrentUser().getUid();
        //get current user name
        dataRef.child("users").child(currentUserId).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = (String) dataSnapshot.getValue();
                if (userName != null) {
                    currentUserName = userName;
                    participateWithName();
                }
                else {
                    eventBus.post(new PartyEvent(PartyEvent.REQUEST_USER_NAME_FAIL));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventBus.post(new PartyEvent(PartyEvent.REQUEST_USER_NAME_FAIL));
            }
        });
    }

    private void participateWithName() {
        dataRef.child("members").child(currentPartyKey).child(currentUserId)
                .setValue(currentUserName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listenToMembers();
                    listenToMessages();
                    listenToPartyRemoval();
                    eventBus.post(new PartyEvent(PartyEvent.FINISH_PARTICIPATE, currentUserId));
                }
                else {
                    eventBus.post(new PartyEvent(PartyEvent.PARTICIPATE_FAIL));
                }
            }
        });
    }

//    private void getMemberList() {
//        dataRef.child("members").child(currentPartyKey)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot member : dataSnapshot.getChildren()) {
//                    User memUser = new User((String) member.getValue());
//                    memUser.setKey(member.getKey());
//                    tmpUserList.add(memUser);
//                }
//                getMessageList();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                eventBus.post(new PartyEvent(PartyEvent.GET_MEMBERS_FAIL));
//            }
//        });
//    }
//
//    private void getMessageList() {
//        dataRef.child("messages").child(currentPartyKey)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot msgSnap : dataSnapshot.getChildren()) {
//                            Message message = msgSnap.getValue(Message.class);
//                            if (message != null) {
//                                message.setMsgKey(msgSnap.getKey());
//                            }
//                            tmpMessageList.add(message);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        eventBus.post(new PartyEvent(PartyEvent.GET_MESSAGES_FAIL));
//                    }
//                });
//    }

    @Override
    public void sendMsg(String msg) {
        Message message = new Message(currentUserId, currentUserName, msg);
        dataRef.child("messages").child(currentPartyKey).push().setValue(message);
    }

    @Override
    public void leaveGroup(boolean asHost) {
        if (asHost) {
            Map<String, Object> hostLeaveUpdate = new HashMap<>();
            hostLeaveUpdate.put("/members/" + currentPartyKey, null);
            hostLeaveUpdate.put("/messages/" + currentPartyKey, null);
            hostLeaveUpdate.put("/parties/" + currentPartyKey, null);
            dataRef.updateChildren(hostLeaveUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    stopListenToMessages();
                    stopListenToMembers();
                    stopListenToPartyRemoval();
                    eventBus.post(new PartyEvent(PartyEvent.HOST_LEAVE_SUCCESS));
                }
            });
        }
        else {
            Map<String, Object> userLeaveUpdate = new HashMap<>();
            userLeaveUpdate.put("/members/" + currentPartyKey + "/" + currentUserId, null);
            dataRef.updateChildren(userLeaveUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    stopListenToMessages();
                    stopListenToMembers();
                    stopListenToPartyRemoval();
                    eventBus.post(new PartyEvent(PartyEvent.USER_LEAVE_SUCCESS));
                }
            });
        }
    }

    @Override
    public void listenToMembers() {

        membersListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User newUser = new User((String) dataSnapshot.getValue());
                newUser.setKey(dataSnapshot.getKey());
                eventBus.post(new PartyEvent(PartyEvent.NEW_MEMBER, newUser));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String removedUserKey = dataSnapshot.getKey();
                eventBus.post(new PartyEvent(PartyEvent.USER_REMOVED, removedUserKey));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dataRef.child("members").child(currentPartyKey).addChildEventListener(membersListener);
    }

    @Override
    public void stopListenToMembers() {
        dataRef.child("members").child(currentPartyKey).removeEventListener(membersListener);
    }

    @Override
    public void listenToMessages() {

        messagesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newMessage = dataSnapshot.getValue(Message.class);
                if (newMessage != null) {
                    newMessage.setMsgKey(dataSnapshot.getKey());
                }
                eventBus.post(new PartyEvent(PartyEvent.NEW_MESSAGE, newMessage));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dataRef.child("messages").child(currentPartyKey).addChildEventListener(messagesListener);
    }

    @Override
    public void stopListenToMessages() {
        dataRef.child("messages").child(currentPartyKey).removeEventListener(messagesListener);
    }

    @Override
    public void listenToPartyRemoval() {

        partiesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String removedPartyKey = dataSnapshot.getKey();
                if (removedPartyKey.equals(currentPartyKey)) {
                    stopListenToMembers();
                    stopListenToMessages();
                    stopListenToPartyRemoval();
                    eventBus.post(new PartyEvent(PartyEvent.PARTY_REMOVE));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dataRef.child("parties").addChildEventListener(partiesListener);
    }

    @Override
    public void stopListenToPartyRemoval() {
        dataRef.child("parties").removeEventListener(partiesListener);
    }
}
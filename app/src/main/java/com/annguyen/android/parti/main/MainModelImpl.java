package com.annguyen.android.parti.main;
import android.support.annotation.NonNull;

import com.annguyen.android.parti.entities.Party;
import com.annguyen.android.parti.main.events.HostEvent;
import com.annguyen.android.parti.main.events.SignOutEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by annguyen on 6/21/2017.
 */

public class MainModelImpl implements MainModel {

    private EventBus eventBus;
    private DatabaseReference dataRef;
    private FirebaseAuth auth;

    public MainModelImpl() {
        this.eventBus = EventBus.getDefault();
        dataRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void hostParty(final double lat, final double lng, final String message) {
        //current user UID
        final String uid = auth.getCurrentUser().getUid();
        //get user name of current user
        dataRef.child("users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = (String) dataSnapshot.getValue();
                createParty(uid, userName, lat, lng, message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventBus.post(new HostEvent(HostEvent.HOST_FAIL,
                        databaseError.toException().getLocalizedMessage(), null));
            }
        });
    }

    private void createParty(String uid, String userName, double lat, double lng, final String message) {
        //create a new party
        Party newParty = new Party(uid, userName, lat, lng, message);
        //get ref to /parties/
        DatabaseReference partiesRef = dataRef.child("parties");
        //push a new kew for party into parties
        final String partyKey = partiesRef.push().getKey();
        //with new key and Party object, update children
        Map<String, Object> partyUpdate = new HashMap<>();
        partyUpdate.put("/parties/" + partyKey, newParty.toFullMap());

        //update the database
        dataRef.updateChildren(partyUpdate)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    eventBus.post(new HostEvent(HostEvent.HOST_SUCCESS, partyKey, message));
                }
                else {
                    eventBus.post(new HostEvent(HostEvent.HOST_FAIL,
                            task.getException().getLocalizedMessage()));
                }
                }
            });
    }

    @Override
    public void signOut() {
        auth.signOut();
        eventBus.post(new SignOutEvent());
    }
}

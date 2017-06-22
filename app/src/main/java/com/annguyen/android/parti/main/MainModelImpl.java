package com.annguyen.android.parti.main;
import android.support.annotation.NonNull;

import com.annguyen.android.parti.entities.Party;
import com.annguyen.android.parti.main.events.HostEvent;
import com.annguyen.android.parti.main.events.SignOutEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by annguyen on 6/21/2017.
 */

public class MainModelImpl implements MainModel {

    private EventBus eventBus;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    public MainModelImpl() {
        this.eventBus = EventBus.getDefault();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void hostParty(double lat, double lng, String message) {
        //current user UID
        String uid = auth.getCurrentUser().getUid();
        //create a new party
        Party newParty = new Party(uid, lat, lng, message);
        //get ref to /parties/
        DatabaseReference partiesRef = databaseReference.child("parties");
        //push a new kew for party into parties
        final String partyKey = partiesRef.push().getKey();

        //with new key and Party object, update children
        Map<String, Object> partyUpdate = new HashMap<>();
        partyUpdate.put("/parties/" + partyKey, newParty.toFullMap());

        //update the database
        databaseReference.updateChildren(partyUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    eventBus.post(new HostEvent(HostEvent.HOST_SUCCESS, null, partyKey));
                }
                else {
                    eventBus.post(new HostEvent(HostEvent.HOST_FAIL,
                            task.getException().getLocalizedMessage(), null));
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

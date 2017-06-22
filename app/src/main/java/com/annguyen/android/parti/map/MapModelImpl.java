package com.annguyen.android.parti.map;

import android.util.Log;

import com.annguyen.android.parti.entities.Party;
import com.annguyen.android.parti.map.events.PartyMapEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annguyen on 6/21/2017.
 */

class MapModelImpl implements MapModel {

    private static final double NEAR_DISTANCE = 30000;
    private EventBus eventBus;
    private DatabaseReference databaseReference;

    public MapModelImpl() {
        eventBus = EventBus.getDefault();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getParties(double lat, double lng) {
        final LatLng currentLatLng = new LatLng(lat, lng);
        DatabaseReference partiesRef = databaseReference.child("parties");
        //listen for parties one time to get list of parties
        partiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //list of parties to put appropriate party into
                List<Party> partyList = new ArrayList<>();

                //surf through list of parties
                for (DataSnapshot party : dataSnapshot.getChildren()) {

                    //create a new LatLng object from the party to calculate distance
                    LatLng partyLatLng = new LatLng((double) party.child("lat").getValue(),
                            (double) party.child("lng").getValue());

                    //compute distance between 2 points, if < NEAR_DISTANCE then show it
                    double distance = SphericalUtil.computeDistanceBetween(currentLatLng, partyLatLng);
                    if (distance <= NEAR_DISTANCE) {
                        Party newParty = party.getValue(Party.class);
                        if (newParty != null)
                            newParty.setKey(party.getKey());
                        partyList.add(newParty);
                    }
                }

                //when complete, post success event
                eventBus.post(new PartyMapEvent(PartyMapEvent.GET_SUCCESS, null, partyList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error occur -> fail event
                eventBus.post(new PartyMapEvent(PartyMapEvent.GET_FAIL,
                        databaseError.toException().getLocalizedMessage(), null));
            }
        });
    }
}

package com.annguyen.android.parti.map;

import com.annguyen.android.parti.api.DirectionsResponse;
import com.annguyen.android.parti.api.RetrofitClient;
import com.annguyen.android.parti.api.entities.Route;
import com.annguyen.android.parti.entities.Party;
import com.annguyen.android.parti.map.events.GetDirectionEvent;
import com.annguyen.android.parti.map.events.PartyMapEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by annguyen on 6/21/2017.
 */

class MapModelImpl implements MapModel {

    private static final double NEAR_DISTANCE = 30000;
    private EventBus eventBus;
    private DatabaseReference databaseReference;
    private RetrofitClient locClient;

    public MapModelImpl() {
        eventBus = EventBus.getDefault();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        locClient = new RetrofitClient();
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

    @Override
    public void getDirection(double currentLat, double currentLng, double latitude, double longitude) {
        String origin = String.valueOf(currentLat) + "," + String.valueOf(currentLng);
        String dest = String.valueOf(latitude) + "," + String.valueOf(longitude);
        locClient.getDirectionsService().getDirections(origin, dest)
                .enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.isSuccessful()) {
                    DirectionsResponse directionsResponse = response.body();
                    if (directionsResponse != null) {
                        //get list of routes
                        List<Route> routes = directionsResponse.getRoutes();

                        //polyline options for map to draw on
                        PolylineOptions polylineOptions = new PolylineOptions();

                        if (!routes.isEmpty()) {
                            //for each route, add list of points to polylineOptions
                            //and calculate total time and distance needed
                            for (Route route : routes) {
                                //points for polyline
                                List<LatLng> points = PolyUtil.decode(route.getOverviewPolyline().getPoints());
                                polylineOptions.addAll(points);
                                onRouteFound(polylineOptions);
                            }
                        }
                        else {
                            onNoRouteFound();
                        }
                    }
                    else {
                        onNoRouteFound();
                    }
                }
                else {
                    onNoRouteFound();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                eventBus.post(new GetDirectionEvent(GetDirectionEvent.GET_DIR_FAIL, t.getLocalizedMessage(), null));
            }
        });
    }

    private void onRouteFound(PolylineOptions polylineOptions) {
        eventBus.post(new GetDirectionEvent(GetDirectionEvent.GET_DIR_SUCCESS, null, polylineOptions));
    }

    private void onNoRouteFound() {
        eventBus.post(new GetDirectionEvent(GetDirectionEvent.GET_DIR_FAIL, "No route available", null));
    }
}

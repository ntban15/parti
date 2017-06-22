package com.annguyen.android.parti.map;

import com.annguyen.android.parti.entities.Party;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by annguyen on 6/21/2017.
 */

public interface MapPresenter {
    void start();

    void getParties(double lat, double lng);

    void stop();

    void onMapReady();

    void onMarkerClick(Marker marker);

    void onJoinClick();
}

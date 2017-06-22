package com.annguyen.android.parti.map;

/**
 * Created by annguyen on 6/21/2017.
 */

public interface MapModel {
    void getParties(double lat, double lng);

    void getDirection(double currentLat, double currentLng, double latitude, double longitude);
}

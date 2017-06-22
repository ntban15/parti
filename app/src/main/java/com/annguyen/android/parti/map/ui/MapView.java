package com.annguyen.android.parti.map.ui;

import com.annguyen.android.parti.entities.Party;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by annguyen on 6/21/2017.
 */

public interface MapView {

    void drawPolyline(PolylineOptions polylineOptions);

    void removeLastPolyline();

    void zoomToCurrentLoc();

    void showProgressBar();

    void hideProgressBar();

    void showPartyDetail(String userName, String partyMessage);

    void unhidePartyDetail();

    void hidePartyDetail();

    void collapsePartyDetail();

    void onError(String errMsg);

    void injectParties(List<Party> parties);

    void setMarkerInactive(Marker marker);

    void setMarkerActive(Marker marker);

    void goBackToMain(String partyKey, String partyMessage);

}

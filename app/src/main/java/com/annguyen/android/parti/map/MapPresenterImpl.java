package com.annguyen.android.parti.map;

import com.annguyen.android.parti.entities.Party;
import com.annguyen.android.parti.map.events.PartyMapEvent;
import com.annguyen.android.parti.map.ui.MapView;
import com.google.android.gms.maps.model.Marker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by annguyen on 6/21/2017.
 */

public class MapPresenterImpl implements MapPresenter {

    private Marker lastMarker = null;
    private String lastPartyKey = null;

    private MapView mapView;
    private MapModel mapModel;
    private EventBus eventBus;

    public MapPresenterImpl(MapView mapView) {
        this.mapView = mapView;
        mapModel = new MapModelImpl();
        eventBus = EventBus.getDefault();
    }

    @Override
    public void start() {
        eventBus.register(this);
    }

    @Override
    public void getParties(double lat, double lng) {
        mapView.showProgressBar();
        mapModel.getParties(lat, lng);
    }

    @Override
    public void stop() {
        mapView = null;
        eventBus.unregister(this);
    }

    @Override
    public void onMapReady() {
        mapView.zoomToCurrentLoc();
    }

    @Override
    public void onMarkerClick(Marker marker) {
        //check if marker is previously clicked
        if (((Party) marker.getTag()).getKey().equals(lastPartyKey)) {
            mapView.hidePartyDetail();
            //set the marker to inactive
            mapView.setMarkerInactive(lastMarker);
            lastMarker = null;
            lastPartyKey = null;
            return;
        }

        //if not, then show new party detail and set marker to active
        mapView.collapsePartyDetail();

        if (lastMarker != null)
            mapView.setMarkerInactive(lastMarker);

        Party party = (Party) marker.getTag();
        if (party != null)
            mapView.showPartyDetail(party.getHost(), party.getMessage());
        mapView.setMarkerActive(marker);

        lastMarker = marker;
        lastPartyKey = party.getKey();
    }

    @Override
    public void onJoinClick() {
        if (lastPartyKey != null) {
            mapView.goBackToMain(lastPartyKey);
        }
    }

    @Subscribe
    void onGetPartyEvent(PartyMapEvent partyMapEvent) {
        mapView.hideProgressBar();
        if (partyMapEvent.getResultCode() == PartyMapEvent.GET_SUCCESS) {
            List<Party> parties = partyMapEvent.getParties();
            mapView.injectParties(parties);
        }
        else {
            mapView.onError(partyMapEvent.getErrMsg());
        }
    }
}

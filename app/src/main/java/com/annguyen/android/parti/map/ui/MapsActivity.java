package com.annguyen.android.parti.map.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.annguyen.android.parti.R;
import com.annguyen.android.parti.entities.Party;
import com.annguyen.android.parti.map.MapPresenter;
import com.annguyen.android.parti.map.MapPresenterImpl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, MapView {

    @BindView(R.id.host_name_text)
    TextView hostNameText;
    @BindView(R.id.party_message)
    TextView partyMessage;
    @BindView(R.id.map_join_btn)
    Button mapJoinBtn;
    @BindView(R.id.map_bottom_sheet)
    NestedScrollView mapBottomSheet;
    @BindView(R.id.map_progress_bar)
    ProgressBar mapProgressBar;

    private double currentLat;
    private double currentLng;

    private GoogleMap mMap;
    private MapPresenter presenter;
    private BottomSheetBehavior bottomSheetBehavior;
    private List<Party> partyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        //init fields
        initInjection();
        //get intent extras
        getIntentExtra();
        //prepare presenter
        presenter.start();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getIntentExtra() {
        Bundle extras = getIntent().getExtras();
        currentLat = extras.getDouble("lat");
        currentLng = extras.getDouble("lng");
    }

    private void initInjection() {
        partyList = new ArrayList<>();
        bottomSheetBehavior = BottomSheetBehavior.from(mapBottomSheet);
        bottomSheetBehavior.setHideable(true);
        presenter = new MapPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_retro_style));

        //ready the map for use
        presenter.onMapReady();

        //get list of parties
        presenter.getParties(currentLat, currentLng);
    }

    @Override
    public void zoomToCurrentLoc() {
        LatLng currentLoc = new LatLng(currentLat, currentLng);
        //mMap.addMarker(new MarkerOptions().position(currentLoc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15)); //zoom to street level
    }

    @Override
    public void showProgressBar() {
        mapProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mapProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPartyDetail(String hostName, String partyMessage) {
        hostNameText.setText(hostName);
        this.partyMessage.setText(partyMessage);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void hidePartyDetail() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void collapsePartyDetail() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onError(String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void injectParties(List<Party> parties) {
        partyList.addAll(parties);
        for (Party party : partyList) {
            LatLng partyLatLng = new LatLng(party.getLat(), party.getLng());
            Marker marker = mMap.addMarker(new MarkerOptions().position(partyLatLng));
            marker.setTag(party);   //tag the party to marker
        }
    }

    @Override
    public void setMarkerInactive(Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }

    @Override
    public void setMarkerActive(Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }

    @OnClick(R.id.map_join_btn)
    public void onViewClicked() {
        presenter.onJoinClick();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        presenter.onMarkerClick(marker);
        return false;
    }

    @Override
    public void goBackToMain(String partyKey) {
        Intent pickIntent = new Intent();
        pickIntent.putExtra("partyKey", partyKey);
        setResult(RESULT_OK, pickIntent);
        finish();
    }
}

package com.annguyen.android.parti.main.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.annguyen.android.parti.R;
import com.annguyen.android.parti.login.ui.LoginActivity;
import com.annguyen.android.parti.main.MainPresenter;
import com.annguyen.android.parti.main.MainPresenterImpl;
import com.annguyen.android.parti.map.ui.MapsActivity;
import com.annguyen.android.parti.party.ui.PartyActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1808;
    public static final int REQUEST_MAP_PICK = 1401;

    @BindView(R.id.main_image)
    ImageView mainImage;
    @BindView(R.id.edit_group_message)
    EditText editGroupMessage;
    @BindView(R.id.edit_message_container)
    CardView editMessageContainer;
    @BindView(R.id.share_btn)
    Button shareBtn;
    @BindView(R.id.find_btn)
    Button findBtn;
    @BindView(R.id.main_progress_bar)
    ProgressBar mainProgressBar;
    @BindView(R.id.main_activity_container)
    RelativeLayout mainActivityContainer;

    private Location currentLoc = null;
    private MainPresenter presenter;
    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initInjection();
        presenter.start();

        //check for location permission first then get current location
        getLocation();
    }

    @Override
    public void getLocation() {
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    onError("Permission denied");
                }
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getCurrentLocation() {
        presenter.getLocStart();
        locationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        String err = null;
                        if (task.isSuccessful()) {
                            currentLoc = task.getResult();
                        }
                        else {
                            err = task.getException().getLocalizedMessage();
                        }
                        presenter.getLocFinish(err);
                    }
                });
    }

    private void initInjection() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        presenter = new MainPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    @OnClick({R.id.share_btn, R.id.find_btn})
    public void onViewClicked(View view) {
        String message = editGroupMessage.getText().toString();
        if (hasLocation()) {
            double lat = currentLoc.getLatitude();
            double lng = currentLoc.getLongitude();
            switch (view.getId()) {
                case R.id.share_btn:
                    presenter.hostParty(lat, lng, message);
                    break;
                case R.id.find_btn:
                    presenter.findParty(lat, lng);
                    break;
            }
        }
        else {
            getLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_action_sign_out:
                presenter.signOut();
                break;
        }
        return true;
    }

    @Override
    public void showProgressBar() {
        mainProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mainProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showInput() {
        editMessageContainer.setVisibility(View.VISIBLE);
        findBtn.setVisibility(View.VISIBLE);
        shareBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        editMessageContainer.setVisibility(View.GONE);
        findBtn.setVisibility(View.GONE);
        shareBtn.setVisibility(View.GONE);
    }

    @Override
    public void onError(String msg) {
        Snackbar.make(mainActivityContainer, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean hasLocation() {
        return currentLoc != null;
    }

    @Override
    public void goToParty(boolean asHost, String partyKey, String partyMessage) {
        Intent goToPartyIntent = new Intent(this, PartyActivity.class);
        goToPartyIntent.putExtra("asHost", asHost);
        goToPartyIntent.putExtra("partyKey", partyKey);
        goToPartyIntent.putExtra("partyMessage", partyMessage);
        startActivity(goToPartyIntent);
    }

    @Override
    public void goToLogin() {
        Intent goToLoginIntent = new Intent(this, LoginActivity.class);
        goToLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToLoginIntent);
    }

    @Override
    public void goToMap(double lat, double lng) {
        Intent goToMapIntent = new Intent(this, MapsActivity.class);
        goToMapIntent.putExtra("lat", lat);
        goToMapIntent.putExtra("lng", lng);
        startActivityForResult(goToMapIntent, REQUEST_MAP_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MAP_PICK) {
            if (resultCode == RESULT_OK) {
                String partyKey = data.getStringExtra("partyKey");
                String partyMessage = data.getStringExtra("partyMessage");
                goToParty(false, partyKey, partyMessage);
            }
        }
    }
}
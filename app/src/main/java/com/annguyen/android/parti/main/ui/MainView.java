package com.annguyen.android.parti.main.ui;

/**
 * Created by annguyen on 6/20/2017.
 */

public interface MainView {
    void showProgressBar();
    void hideProgressBar();
    void showInput();
    void hideInput();
    void onError(String msg);
    void getLocation();
    void goToMap(double lat, double lng);
    void goToParty(boolean asHost, String partyKey, String partyMessage);
    boolean hasLocation();
    void goToLogin();
}

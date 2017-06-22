package com.annguyen.android.parti.main;

/**
 * Created by annguyen on 6/20/2017.
 */

public interface MainPresenter {
    void start();

    void stop();

    void hostParty(double lat, double lng, String msg);

    void findParty(double lat, double lng);

    void getLocStart();

    void getLocFinish(String err);

    void signOut();
}

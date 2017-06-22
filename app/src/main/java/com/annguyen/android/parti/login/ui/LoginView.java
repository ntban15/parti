package com.annguyen.android.parti.login.ui;

/**
 * Created by annguyen on 6/20/2017.
 */

public interface LoginView {
    void showProgressBar();
    void hideProgressBar();
    void showInput();
    void hideInput();
    void onError(String msg);
    void goToMainScreen();
    void goToQuickStart();
}

package com.annguyen.android.parti.login;

/**
 * Created by annguyen on 6/20/2017.
 */

public interface LoginPresenter {
    void start();

    void checkAuth();

    void stop();

    void signIn(String email, String password);

    void createAccount(String email, String password);
}

package com.annguyen.android.parti.login;

import com.annguyen.android.parti.login.events.LoginEvent;
import com.annguyen.android.parti.login.ui.LoginView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by annguyen on 6/20/2017.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void checkAuth() {
        loginView.hideInput();
        loginView.showProgressBar();
        loginModel.checkAuth();
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
        loginView = null;
    }

    @Override
    public void signIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty())
            return;
        loginView.hideInput();
        loginView.showProgressBar();
        loginModel.signIn(email, password);
    }

    @Override
    public void createAccount(String email, String password) {
        loginView.hideInput();
        loginView.showProgressBar();
        loginModel.createAccount(email, password);
    }

    @Subscribe
    void onLoginEvent(LoginEvent loginEvent) {

        loginView.showInput();
        loginView.hideProgressBar();

        switch (loginEvent.getLoginCode()) {
            case LoginEvent.AUTH_SUCCESS:
                loginView.goToMainScreen();
                break;
            case LoginEvent.AUTH_FAIL:
                break;
            case LoginEvent.CREATE_ACC_SUCCESS:
                loginView.goToQuickStart();
                break;
            case LoginEvent.CREATE_ACC_FAIL:
                loginView.onError(loginEvent.getLoginMsg());
                break;
            case LoginEvent.SIGN_IN_SUCCESS:
                loginView.goToMainScreen();
                break;
            case LoginEvent.SIGN_IN_FAIL:
                loginView.onError(loginEvent.getLoginMsg());
                break;
        }
    }
}

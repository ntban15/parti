package com.annguyen.android.parti.main;

import com.annguyen.android.parti.main.events.HostEvent;
import com.annguyen.android.parti.main.events.SignOutEvent;
import com.annguyen.android.parti.main.ui.MainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by annguyen on 6/21/2017.
 */

public class MainPresenterImpl implements MainPresenter {

    EventBus eventBus;
    MainView mainView;
    MainModel mainModel;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        eventBus = EventBus.getDefault();
        mainModel = new MainModelImpl();
    }

    @Override
    public void start() {
        eventBus.register(this);
    }

    @Override
    public void stop() {
        eventBus.unregister(this);
        mainView = null;
    }

    @Override
    public void hostParty(double lat, double lng, String msg) {
        mainModel.hostParty(lat, lng, msg);
    }

    @Override
    public void findParty(double lat, double lng) {
        mainView.goToMap(lat, lng);
    }

    @Override
    public void getLocStart() {
        mainView.showProgressBar();
        mainView.hideInput();
    }

    @Override
    public void getLocFinish(String err) {
        mainView.hideProgressBar();
        mainView.showInput();
        if (err != null)
            mainView.onError(err);
    }

    @Override
    public void signOut() {
        mainModel.signOut();
    }

    @Subscribe
    void onHostEvent(HostEvent hostEvent) {
        switch (hostEvent.getResultCode()) {
            case HostEvent.HOST_SUCCESS:
                mainView.goToParty(true, hostEvent.getPartyKey());
                break;
            case HostEvent.HOST_FAIL:
                mainView.onError(hostEvent.getErrMsg());
                break;
        }
    }

    @Subscribe
    void onSignOutEvent(SignOutEvent signOutEvent) {
        mainView.goToLogin();
    }
}

package com.annguyen.android.parti.party;

import com.annguyen.android.parti.party.events.PartyEvent;
import com.annguyen.android.parti.party.ui.PartyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by annguyen on 22/06/2017.
 */

public class PartyPresenterImpl implements PartyPresenter {

    private EventBus eventBus;
    private PartyView partyView;
    private PartyModel partyModel;

    public PartyPresenterImpl(PartyView partyView) {
        this.partyView = partyView;
        eventBus = EventBus.getDefault();
        partyModel = new PartyModelImpl();
    }

    @Override
    public void start() {
        eventBus.register(this);
    }

    @Override
    public void participate(String partyKey) {
        partyView.showProgressBar();
        partyView.hideInput();
        partyModel.participate(partyKey);
    }

    @Override
    public void stop() {
        eventBus.unregister(this);
        partyView = null;
    }

    @Override
    public void sendMsg(String msg) {
        partyView.clearMsgInput();
        partyModel.sendMsg(msg);
    }

    @Override
    public void leaveGroup(boolean asHost) {
        partyModel.leaveGroup(asHost);
    }

    @Subscribe
    void onPartyEvent(PartyEvent partyEvent) {
        switch (partyEvent.getEventCode()) {
            case PartyEvent.FINISH_PARTICIPATE: {
                partyView.setCurrentUserId(partyEvent.getUserKey());
                partyView.setHostId(partyEvent.getHostKey());
                partyView.hideProgressBar();
                partyView.showInput();
                break;
            }
            case PartyEvent.NEW_MEMBER: {
                partyView.addNewMember(partyEvent.getNewUser());
                break;
            }
            case PartyEvent.NEW_MESSAGE: {
                partyView.addNewMessage(partyEvent.getNewMessage());
                break;
            }
            case PartyEvent.USER_REMOVED: {
                partyView.removeMember(partyEvent.getUserKey());
                break;
            }
            case PartyEvent.PARTY_REMOVED: {
                partyView.goToMain();
                break;
            }
            case PartyEvent.USER_LEAVE_SUCCESS: {
                partyView.goToMain();
                break;
            }
            case PartyEvent.HOST_LEAVE_SUCCESS: {
                partyView.goToMain();
                break;
            }
            case PartyEvent.REQUEST_USER_NAME_FAIL: {
                partyView.goToMain();
                break;
            }
            case PartyEvent.PARTICIPATE_FAIL: {
                partyView.goToMain();
                break;
            }
        }
    }
}

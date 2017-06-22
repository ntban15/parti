package com.annguyen.android.parti.map.events;

import com.annguyen.android.parti.entities.Party;

import java.util.List;

/**
 * Created by annguyen on 6/21/2017.
 */

public class PartyMapEvent {
    public static final int GET_SUCCESS = 1234;
    public static final int GET_FAIL = 3333;

    private int resultCode;
    private String errMsg;
    private List<Party> parties;

    public PartyMapEvent(int resultCode, String errMsg, List<Party> parties) {
        this.resultCode = resultCode;
        this.errMsg = errMsg;
        this.parties = parties;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public List<Party> getParties() {
        return parties;
    }
}

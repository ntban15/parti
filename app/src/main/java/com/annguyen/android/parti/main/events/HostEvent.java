package com.annguyen.android.parti.main.events;

/**
 * Created by annguyen on 6/21/2017.
 */

public class HostEvent {
    public static final int HOST_SUCCESS = 1001;
    public static final int HOST_FAIL = 4001;

    private int resultCode;
    private String errMsg;
    private String partyKey;

    public HostEvent(int resultCode, String errMsg, String partyKey) {
        this.resultCode = resultCode;
        this.errMsg = errMsg;
        this.partyKey = partyKey;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getPartyKey() {
        return partyKey;
    }
}

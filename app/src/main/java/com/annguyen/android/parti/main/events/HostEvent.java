package com.annguyen.android.parti.main.events;

/**
 * Created by annguyen on 6/21/2017.
 */

public class HostEvent {
    public static final int HOST_SUCCESS = 1001;
    public static final int HOST_FAIL = 4001;

    private String partyMessage;
    private int resultCode;
    private String errMsg;
    private String partyKey;

    public HostEvent(int resultCode, String partyKey, String partyMessage) {
        this.resultCode = resultCode;
        this.partyKey = partyKey;
        this.partyMessage = partyMessage;
    }

    public HostEvent(int resultCode, String errMsg) {
        this.resultCode = resultCode;
        this.errMsg = errMsg;
    }

    public String getPartyMessage() {
        return partyMessage;
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

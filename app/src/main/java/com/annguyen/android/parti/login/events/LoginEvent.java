package com.annguyen.android.parti.login.events;

/**
 * Created by annguyen on 6/20/2017.
 */

public class LoginEvent {
    public static final int SIGN_IN_SUCCESS = 101;
    public static final int SIGN_IN_FAIL = 401;
    public static final int CREATE_ACC_SUCCESS = 102;
    public static final int CREATE_ACC_FAIL = 402;
    public static final int AUTH_SUCCESS = 103;
    public static final int AUTH_FAIL = 403;

    private int loginCode;
    private String loginMsg;

    public LoginEvent(int loginCode, String loginMsg) {
        this.loginCode = loginCode;
        this.loginMsg = loginMsg;
    }

    public int getLoginCode() {
        return loginCode;
    }

    public String getLoginMsg() {
        return loginMsg;
    }
}

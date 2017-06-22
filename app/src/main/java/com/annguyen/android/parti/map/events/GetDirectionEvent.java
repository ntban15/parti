package com.annguyen.android.parti.map.events;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by annguyen on 6/22/2017.
 */

public class GetDirectionEvent {
    public static final int GET_DIR_SUCCESS = 101;
    public static final int GET_DIR_FAIL = 202;

    private int eventCode;
    private String errMsg;
    private PolylineOptions polylineOptions;

    public GetDirectionEvent(int eventCode, String errMsg, PolylineOptions polylineOptions) {
        this.eventCode = eventCode;
        this.errMsg = errMsg;
        this.polylineOptions = polylineOptions;
    }

    public int getEventCode() {
        return eventCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public PolylineOptions getPolylineOptions() {
        return polylineOptions;
    }
}

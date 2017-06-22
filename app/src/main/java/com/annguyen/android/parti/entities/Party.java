package com.annguyen.android.parti.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by annguyen on 6/21/2017.
 */

@IgnoreExtraProperties
public class Party {
    private String host;
    private String name;
    private double lat;
    private double lng;
    private String message;
    @Exclude
    private String key;

    public Party() {}

    public Party(String host, String name, double lat, double lng, String message) {
        this.host = host;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.message = message;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getMessage() {
        return message;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public Map<String, Object> toFullMap() {
        Map<String, Object> result = new HashMap<>();

        result.put("host", host);
        result.put("name", name);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("message", message);

        return result;
    }
}

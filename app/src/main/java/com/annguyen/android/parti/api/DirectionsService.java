package com.annguyen.android.parti.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionsService {
    @GET("https://maps.googleapis.com/maps/api/directions/json")
    Call<DirectionsResponse> getDirections(@Query("origin") String origin,
                                           @Query("destination") String destination);
        }

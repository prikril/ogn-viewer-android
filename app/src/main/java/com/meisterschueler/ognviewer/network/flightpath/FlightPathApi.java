package com.meisterschueler.ognviewer.network.flightpath;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FlightPathApi {

    @GET("flightpath/{address}")
    Call<FlightPath> getFlightPath(@Path("address") String address);

}

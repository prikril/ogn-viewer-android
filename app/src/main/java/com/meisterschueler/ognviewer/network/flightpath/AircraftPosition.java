package com.meisterschueler.ognviewer.network.flightpath;

import com.google.gson.annotations.SerializedName;

public class AircraftPosition {

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

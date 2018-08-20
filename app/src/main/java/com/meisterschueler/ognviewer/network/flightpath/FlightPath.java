package com.meisterschueler.ognviewer.network.flightpath;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlightPath {

    @SerializedName("address")
    private String address;

    @SerializedName("positions")
    private List<AircraftPosition> positions;


    public String getAddress() {
        return address;
    }

    public List<AircraftPosition> getPositions() {
        return positions;
    }

}

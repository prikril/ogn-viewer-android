package com.meisterschueler.ognviewer;

import org.ogn.commons.beacon.AircraftType;

import java.util.Date;

/**
 * Created by Dominik on 04.08.2017.
 */

public class Aircraft {
    final String address;
    final AircraftType aircraftType;
    float climbRate;
    double lat;
    double lon;
    float alt;
    float groundSpeed;
    String regNumber;
    String CN;
    String model;
    boolean isOgnPrivate;
    String receiverName;
    int track;
    Date lastSeen;

    public Aircraft(String address, AircraftType aircraftType, float climbRate, double lat, double lon,
                    float alt, float groundSpeed, String regNumber, String CN, String model, boolean isOgnPrivate,
                    String receiverName, int track) {
        this.address = address;
        this.aircraftType = aircraftType;
        this.climbRate = climbRate;
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.groundSpeed = groundSpeed;
        this.regNumber = regNumber;
        this.CN = CN;
        this.model = model;
        this.isOgnPrivate = isOgnPrivate;
        this.receiverName = receiverName;
        this.track = track;
        this.lastSeen = new Date();
    }

    public String getAddress() {
        return address;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public float getClimbRate() {
        return climbRate;
    }

    public void setClimbRate(float climbRate) {
        this.climbRate = climbRate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public float getAlt() {
        return alt;
    }

    public void setAlt(float alt) {
        this.alt = alt;
    }

    public float getGroundSpeed() {
        return groundSpeed;
    }

    public void setGroundSpeed(float groundSpeed) {
        this.groundSpeed = groundSpeed;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isOgnPrivate() {
        return isOgnPrivate;
    }

    public void setOgnPrivate(boolean ognPrivate) {
        isOgnPrivate = ognPrivate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }
}

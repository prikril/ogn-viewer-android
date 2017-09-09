package com.meisterschueler.ognviewer;

import android.location.Location;

import org.ogn.commons.beacon.AircraftBeacon;

public class FlarmMessage {
    private int AlarmLevel;      // decimal 0-3: 0 == no alarm, 1 == 13-18s to impact, 2 == 9-12s to impact, 3 == 0-8s to impact
    private int RelativeNorth;   // Decimal integer value. Range: from -32768 to 32767. Relative position in meters true north from own position.
    private int RelativeEast;    // Decimal integer value. Range: from -32768 to 32767. Relative position in meters true east from own position.
    private int RelativeVertical; // Decimal integer value. Range: from -32768 to 32767. Relative vertical separation in meters above own position.
    private int IDType;          // Decimal integer value. Range: from 0 to 3. 1 == official ICAO 24-bit aircraft address, 2 == stable FLARM ID (chosen by FLARM), 3 = anonymous ID
    private String ID;           // 6-digit hexadecimal value
    private int Track;           // Decimal integer value. Range: from 0 to 359.
    private int GroundSpeed;     // <GroundSpeed> Decimal integer value. Range: from 0 to 32767.
    private float ClimbRate;     // Decimal fixed point number with one digit after the radix. Range: from -32.7 to 32.7.
    private String AcftType;     // Hexadecimal value. Range: from 0 to F.

    public int getAlarmLevel() {
        return AlarmLevel;
    }

    public int getRelativeNorth() {
        return RelativeNorth;
    }

    public int getRelativeEast() {
        return RelativeEast;
    }

    public int getRelativeVertical() {
        return RelativeVertical;
    }

    public int getIDType() {
        return IDType;
    }

    public String getID() {
        return ID;
    }

    public int getTrack() {
        return Track;
    }

    public int getGroundSpeed() {
        return GroundSpeed;
    }

    public float getClimbRate() {
        return ClimbRate;
    }

    public String getAcftType() {
        return AcftType;
    }

    public FlarmMessage(AircraftBeacon ab, Location location) {
        Location beaconLocation = new Location("OGN");
        beaconLocation.setLatitude(ab.getLat());
        beaconLocation.setLongitude(ab.getLon());
        beaconLocation.setAltitude(ab.getAlt());
        beaconLocation.setBearing(ab.getTrack());
        beaconLocation.setSpeed(ab.getGroundSpeed());

        float bearing = location.bearingTo(beaconLocation);
        float distance = location.distanceTo(beaconLocation);

        this.AlarmLevel = 0;
        this.RelativeNorth = ((int) Math.round(Math.cos(bearing) * distance));
        this.RelativeEast = ((int) Math.round(Math.sin(bearing) * distance));
        this.RelativeVertical = ((int) Math.round(ab.getAlt() - location.getAltitude()));
        this.IDType = ab.getAddressType().getCode();
        this.ID = ab.getAddress();
        this.Track = ab.getTrack();
        this.GroundSpeed = (int) ab.getGroundSpeed();
        this.ClimbRate = ab.getClimbRate();
        this.AcftType = Integer.toHexString(ab.getAircraftType().getCode());
    }

    public String toString() {
        return "PFLAA," + AlarmLevel + "," + RelativeNorth + "," + RelativeEast + "," + RelativeVertical + "," + IDType + "," + ID + "," + Track + "," + GroundSpeed + "," + ClimbRate + "," + AcftType;
        // PFLAA,<AlarmLevel>,<RelativeNorth>,<RelativeEast>,
        // <RelativeVertical>,<IDType>,<ID>,<Track>,<TurnRate>,<GroundSpeed>,
        // <ClimbRate>,<AcftType>
    }
}
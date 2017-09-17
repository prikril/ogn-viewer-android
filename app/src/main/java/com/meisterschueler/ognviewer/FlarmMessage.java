package com.meisterschueler.ognviewer;

import android.location.Location;

import org.ogn.commons.beacon.AircraftBeacon;

import java.util.Calendar;
import java.util.Locale;

public class FlarmMessage {
    private Location beaconLocation;
    private long time;
    private double bearing = 0.0;
    private float distance = 0.0f;

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


    public long getTime() {
        return time;
    }

    public double getBearing() {
        return bearing;
    }

    public float getDistance() {
        return distance;
    }

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

    public FlarmMessage(AircraftBeacon aircraftBeacon) {
        this.time = Calendar.getInstance().getTime().getTime();

        this.beaconLocation = new Location("OGN");
        this.beaconLocation.setLatitude(aircraftBeacon.getLat());
        this.beaconLocation.setLongitude(aircraftBeacon.getLon());
        this.beaconLocation.setAltitude(aircraftBeacon.getAlt());
        this.beaconLocation.setBearing(aircraftBeacon.getTrack());
        this.beaconLocation.setSpeed(aircraftBeacon.getGroundSpeed());

        this.AlarmLevel = 0;
        this.IDType = aircraftBeacon.getAddressType().getCode();
        this.ID = aircraftBeacon.getAddress();
        this.Track = aircraftBeacon.getTrack();
        this.GroundSpeed = (int) aircraftBeacon.getGroundSpeed();
        this.ClimbRate = aircraftBeacon.getClimbRate();
        this.AcftType = Integer.toHexString(aircraftBeacon.getAircraftType().getCode());
    }

    public void setOwnLocation(Location ownLocation) {
        this.bearing = ownLocation.bearingTo(beaconLocation) / 180.0 * Math.PI;
        this.distance = ownLocation.distanceTo(beaconLocation);

        this.RelativeNorth = ((int) Math.round(Math.cos(bearing) * distance));
        this.RelativeEast = ((int) Math.round(Math.sin(bearing) * distance));
        this.RelativeVertical = ((int) Math.round(beaconLocation.getAltitude() - ownLocation.getAltitude()));
    }

    static public String checksum(String str) {
        int result = 0;
        for(int i = 0; i < str.length(); i++) {
            result ^= str.charAt(i);
        }
        return String.format("%02X", result);
    }

    public String toString() {
        String result = String.format(Locale.US,"PFLAA,%d,%d,%d,%d,%d,%s,%d,,%d,%.1f,%s", AlarmLevel, RelativeNorth, RelativeEast, RelativeVertical, IDType, ID, Track, GroundSpeed, ClimbRate, AcftType);
        return String.format(Locale.US,"$%s*%s", result, checksum(result));

        // PFLAA,<AlarmLevel>,<RelativeNorth>,<RelativeEast>,
        // <RelativeVertical>,<IDType>,<ID>,<Track>,<TurnRate>,<GroundSpeed>,
        // <ClimbRate>,<AcftType>
    }
}
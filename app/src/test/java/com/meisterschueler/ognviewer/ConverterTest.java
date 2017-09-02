package com.meisterschueler.ognviewer;

import android.location.Location;

import org.junit.Test;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.impl.aprs.AprsAircraftBeacon;

import static org.junit.Assert.assertEquals;

class Converter {
    public static FlarmMessage convert(AircraftBeacon ab, Location location) {
        Location beaconLocation = new Location("OGN");
        beaconLocation.setLatitude(ab.getLat());
        beaconLocation.setLongitude(ab.getLon());
        beaconLocation.setAltitude(ab.getAlt());
        beaconLocation.setBearing(ab.getTrack());
        beaconLocation.setSpeed(ab.getGroundSpeed());

        float bearing = location.bearingTo(beaconLocation);
        float distance = location.distanceTo(beaconLocation);

        FlarmMessage flarmMessage = new FlarmMessage();
        flarmMessage.setAlarmLevel(0);
        flarmMessage.setRelativeNorth((int) Math.round(Math.cos(bearing) * distance));
        flarmMessage.setRelativeEast((int) Math.round(Math.sin(bearing) * distance));
        flarmMessage.setRelativeVertical((int) Math.round(ab.getAlt() - location.getAltitude()));
        flarmMessage.setIDType(ab.getAddressType().getCode());
        flarmMessage.setID(ab.getId());
        flarmMessage.setTrack(ab.getTrack());
        flarmMessage.setGroundSpeed((int) ab.getGroundSpeed());

        return flarmMessage;
    }
}


public class ConverterTest {
    @Test
    public void test_blabla() {
        Location location;

        location = new Location("fake");
        location.setLatitude(51.3);
        location.setLongitude(13.3);
        location.setAltitude(602);

        AircraftBeacon ab = new AprsAircraftBeacon("ICA3ECE59>APRS,qAS,GLDRTR:/171254h5144.78N/00616.67E'263/000/A=000875 id093D0930 +000fpm +0.0rot");

        FlarmMessage flarmMessage = Converter.convert(ab, location);
        assertEquals(flarmMessage.toString(), "asdf");
        //assertEquals(f.getRelativeVertical(), (int) (ab.getAlt() - location.getAltitude()));
    }
}

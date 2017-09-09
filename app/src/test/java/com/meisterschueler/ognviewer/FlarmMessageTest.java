package com.meisterschueler.ognviewer;

import android.location.Location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.impl.aprs.AprsLineParser;

import static org.ogn.commons.utils.AprsUtils.feetsToMetres;


public class FlarmMessageTest {
    Location location;

    @Before
    public void setUp() {
        location = new Location("fake");
        location.setAccuracy(10);
        location.setTime(1000);
        location.setLatitude(51.0d);
        location.setLongitude(13.0d);
        location.setAltitude(600);

        System.err.println(location.getLatitude());
    }

    @Test
    public void test_above() {


        AprsLineParser parser = AprsLineParser.get();
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5100.00N\\01300.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab, location);
        Assert.assertEquals(flarmMessage.getRelativeEast(), 0, 0.01);
        Assert.assertEquals(flarmMessage.getRelativeNorth(), 0, 0.01);
        Assert.assertEquals(flarmMessage.getRelativeVertical(), feetsToMetres(6498) - location.getAltitude(), 1.0);
    }

    @Test
    public void test_east() {
        AprsLineParser parser = AprsLineParser.get();
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5100.00N\\01400.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab, location);
        Assert.assertTrue(flarmMessage.getRelativeEast() > 1000);
        Assert.assertEquals(flarmMessage.getRelativeNorth(), 0, 0.01);
        //Assert.assertEquals(flarmMessage.getRelativeVertical(), feetsToMetres(6498) - location.getAltitude(), 1.0);
    }
}

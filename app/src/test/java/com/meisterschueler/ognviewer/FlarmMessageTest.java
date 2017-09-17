package com.meisterschueler.ognviewer;

import android.location.Location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.impl.aprs.AprsLineParser;
import org.robolectric.RobolectricTestRunner;

import static org.ogn.commons.utils.AprsUtils.feetsToMetres;
import static org.ogn.commons.utils.AprsUtils.kntToKmh;


@RunWith(RobolectricTestRunner.class)
public class FlarmMessageTest {
    Location location;
    AprsLineParser parser;

    @Before
    public void setUp() throws Exception {
        location = new Location("Test");
        location.setAccuracy(10);
        location.setTime(1000);
        location.setLatitude(51.0d);
        location.setLongitude(13.0d);
        location.setAltitude(600);

        parser = AprsLineParser.get();
    }

    // 51N 13E to 51N 14E or 51N 12E == 69.98km
    // 51N 13E to 52N 13E or 50N 13E == 111.2km

    @Test
    public void test_basic() {
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5100.00N\\01300.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab);
        flarmMessage.setOwnLocation(location);
        Assert.assertEquals(flarmMessage.getID(), "DDA5BA");
        Assert.assertEquals(flarmMessage.getTrack(), 327, 0.01);
        Assert.assertEquals(flarmMessage.getGroundSpeed(), kntToKmh(149), 1.0);
        Assert.assertEquals(flarmMessage.getClimbRate(), feetsToMetres(-454)/60.0, 0.01);
        Assert.assertEquals(flarmMessage.getAcftType(), "2");
    }

    @Test
    public void test_above() {
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5100.00N\\01300.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab);
        flarmMessage.setOwnLocation(location);
        Assert.assertEquals(flarmMessage.getRelativeEast(), 0, 0.01);
        Assert.assertEquals(flarmMessage.getRelativeNorth(), 0, 0.01);
        Assert.assertEquals(flarmMessage.getRelativeVertical(), feetsToMetres(6498) - location.getAltitude(), 1.0);
    }

    @Test
    public void test_east() {
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5100.00N\\01400.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab);
        flarmMessage.setOwnLocation(location);
        Assert.assertEquals(flarmMessage.getRelativeNorth(), 0, 500);
        Assert.assertEquals(flarmMessage.getRelativeEast(), 69980, 500);
    }

    @Test
    public void test_west() {
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5100.00N\\01200.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab);
        flarmMessage.setOwnLocation(location);
        Assert.assertEquals(flarmMessage.getRelativeNorth(), 0, 500);
        Assert.assertEquals(flarmMessage.getRelativeEast(), -69980, 500);
    }

    @Test
    public void test_north() {
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5200.00N\\01300.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab);
        flarmMessage.setOwnLocation(location);
        Assert.assertEquals(flarmMessage.getRelativeNorth(), 111200, 500);
        Assert.assertEquals(flarmMessage.getRelativeEast(), 0, 500);
    }

    @Test
    public void test_south() {
        AircraftBeacon ab = (AircraftBeacon) parser.parse("ICA4B0E3A>APRS,qAS,Letzi:/165319h5000.00N\\01300.00E^327/149/A=006498 id0ADDA5BA -454fpm -1.1rot 8.8dB 0e +51.2kHz gps4x5");

        FlarmMessage flarmMessage = new FlarmMessage(ab);
        flarmMessage.setOwnLocation(location);
        Assert.assertEquals(flarmMessage.getRelativeNorth(), -111200, 500);
        Assert.assertEquals(flarmMessage.getRelativeEast(), 0, 500);
    }

    @Test
    public void test_checksum() {
        String s = "PFLAA,0,1,80,-6,2,DDC10E,95,,37,2.3,8";//*4E
        Assert.assertEquals("4E", FlarmMessage.checksum(s));

        s = "PFLAA,0,-21,-49,-4,2,DDA659,0,,0,0.0,1";//*07
        Assert.assertEquals("07", FlarmMessage.checksum(s));
    }
}

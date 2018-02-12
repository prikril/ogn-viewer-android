package com.meisterschueler.ognviewer.common;

import org.ogn.commons.beacon.ReceiverBeacon;
import org.ogn.commons.utils.Version;

/**
 * Created by Dominik on 07.02.2018.
 * For details see Code from AprsReceiverBeacon in org.ogn.commons.beacon.impl.aprs
 */

public class ReceiverBeaconImplReplacement implements ReceiverBeacon {


    /**
     * name of the server receiving the packet
     */
    protected String srvName;

    /**
     * receiver's version
     */
    protected String version;

    /**
     * hardware platform on which the receiver runs
     */
    protected String platform;

    /**
     * CPU load (as indicated by the linux 'uptime' command)
     */
    protected float cpuLoad;

    /**
     * CPU temperature of the board (in deg C) or <code>Float.NaN</code> if not
     * set
     */
    protected float cpuTemp = Float.NaN;

    /**
     * total size of RAM available in the system (in MB)
     */
    protected float totalRam;

    /**
     * size of free RAM (in MB)
     */
    protected float freeRam;

    /**
     * estimated NTP error (in ms)
     */
    protected float ntpError;

    /**
     * real time crystal correction(set in the configuration) (in ppm)
     */
    protected float rtCrystalCorrection;

    /**
     * receiver (DVB-T stick's) crystal correction (in ppm)
     */
    protected int recCrystalCorrection;

    /**
     * receiver correction measured taking GSM for a reference (in ppm)
     */
    protected float recCrystalCorrectionFine;

    /**
     * receiver's input noise (in dB)
     */
    protected float recInputNoise;



    @Override
    public float getCpuLoad() {
        return cpuLoad;
    }

    @Override
    public float getCpuTemp() {
        return cpuTemp;
    }

    @Override
    public float getFreeRam() {
        return freeRam;
    }

    @Override
    public float getTotalRam() {
        return totalRam;
    }

    @Override
    public float getNtpError() {
        return ntpError;
    }

    @Override
    public float getRtCrystalCorrection() {
        return rtCrystalCorrection;
    }

    @Override
    public int getRecCrystalCorrection() {
        return recCrystalCorrection;
    }

    @Override
    public float getRecCrystalCorrectionFine() {
        return recCrystalCorrectionFine;
    }

    @Override
    public float getRecAbsCorrection() {
        return recCrystalCorrection + recCrystalCorrectionFine;
    }

    @Override
    public float getRecInputNoise() {
        return recInputNoise;
    }

    @Override
    public String getServerName() {
        return srvName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getPlatform() {
        return platform;
    }

    @Override
    public int getNumericVersion() {
        return version == null ? 0 : Version.fromString(version);
    }


    //the following things are from OgnBeacon
    protected String id;
    protected long timestamp;
    protected double lat;
    protected double lon;
    protected float alt;

    /**
     * deg
     */
    protected int track;

    /**
     * km/h
     */
    protected float groundSpeed;

    protected String rawPacket;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    @Override
    public float getAlt() {
        return alt;
    }

    @Override
    public int getTrack() {
        return track;
    }

    @Override
    public float getGroundSpeed() {
        return groundSpeed;
    }

    @Override
    public String getRawPacket() {
        return rawPacket;
    }

    private ReceiverBeaconImplReplacement() {
        // no default implementation
    }

    public ReceiverBeaconImplReplacement(ReceiverBeacon beacon) {
        this.alt = beacon.getAlt();
        this.cpuLoad = beacon.getCpuLoad();
        this.cpuTemp = beacon.getCpuTemp();
        this.cpuLoad = beacon.getCpuLoad();
        this.freeRam = beacon.getFreeRam();
        this.groundSpeed = beacon.getGroundSpeed();
        this.id = beacon.getId();
        this.lat = beacon.getLat();
        this.lon = beacon.getLon();
        this.ntpError = beacon.getNtpError();
        this.platform = beacon.getPlatform();
        this.rawPacket = beacon.getRawPacket();
        this.recCrystalCorrection = beacon.getRecCrystalCorrection();
        this.recCrystalCorrectionFine = beacon.getRecCrystalCorrectionFine();
        this.recInputNoise = beacon.getRecInputNoise();
        this.rtCrystalCorrection = beacon.getRtCrystalCorrection();
        this.srvName = beacon.getServerName();
        this.timestamp = beacon.getTimestamp();
        this.totalRam = beacon.getTotalRam();
        this.track = beacon.getTrack();
        this.version = beacon.getVersion();
    }

    public ReceiverBeaconImplReplacement update(ReceiverBeacon beacon) {
        if (beacon.getAlt() != 0) {
            this.alt = beacon.getAlt();
        }
        if (beacon.getLat() != 0) {
            this.lat = beacon.getLat();
        }
        if (beacon.getLon() != 0) {
            this.lon = beacon.getLon();
        }
        this.timestamp = beacon.getTimestamp();
        //add more details to update if needed

        return this;
    }
}

package com.meisterschueler.ognviewer.common;

import org.ogn.commons.beacon.ReceiverBeacon;

import java.util.ArrayList;
import java.util.List;

public class ReceiverBundle {
    public ReceiverBeacon receiverBeacon;
    public int beaconCount;
    static public int maxBeaconCounter;
    public List<String> aircrafts;
    static public int maxAircraftCounter;

    public ReceiverBundle(ReceiverBeacon receiverBeacon) {
        this.receiverBeacon = receiverBeacon;

        this.beaconCount = 0;
        this.aircrafts = new ArrayList<>();
    }
}
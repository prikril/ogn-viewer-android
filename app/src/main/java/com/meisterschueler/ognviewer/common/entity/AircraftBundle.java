package com.meisterschueler.ognviewer.common.entity;

import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;

public class AircraftBundle {
    public AircraftBeacon aircraftBeacon;
    public AircraftDescriptor aircraftDescriptor;

    public AircraftBundle(AircraftBeacon aircraftBeacon, AircraftDescriptor aircraftDescriptor) {
        this.aircraftBeacon = aircraftBeacon;
        this.aircraftDescriptor = aircraftDescriptor;
    }
}

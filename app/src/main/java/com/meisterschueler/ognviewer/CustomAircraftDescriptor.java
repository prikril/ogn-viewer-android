package com.meisterschueler.ognviewer;

import org.ogn.commons.beacon.AircraftDescriptor;

import co.uk.rushorm.core.RushObject;

/**
 * CAUTION: moving to other package requires DB migration!!!
 */
public class CustomAircraftDescriptor extends RushObject {
    public String address;

    public String regNumber;
    public String CN;
    public String owner;
    public String homeBase;
    public String model;
    public String freq;

    boolean favourite;

    public CustomAircraftDescriptor() {
    }

    public CustomAircraftDescriptor(String address, AircraftDescriptor ad) {
        this.address = address;

        this.regNumber = ad.getRegNumber();
        this.CN = ad.getCN();
        this.owner = ad.getOwner();
        this.homeBase = ad.getHomeBase();
        this.model = ad.getModel();
        this.freq = ad.getFreq();
    }

    public CustomAircraftDescriptor(String address, String regNumber, String CN, String owner, String homeBase, String model, String freq) {
        this.address = address;

        this.regNumber = regNumber;
        this.CN = CN;
        this.owner = owner;
        this.homeBase = homeBase;
        this.model = model;
        this.freq = freq;
    }

    public boolean isEmpty() {
        return (regNumber.isEmpty() && CN.isEmpty() && owner.isEmpty() && homeBase.isEmpty() && model.isEmpty() && freq.isEmpty());
    }
}

package com.meisterschueler.ognviewer;

import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.beacon.impl.AircraftDescriptorImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.uk.rushorm.core.RushSearch;

public class CustomAircraftDescriptorProvider implements AircraftDescriptorProvider {

    private Map<String, CustomAircraftDescriptor> aircraftDescriptorMap = new HashMap<String, CustomAircraftDescriptor>();

    public CustomAircraftDescriptorProvider() {
        List<CustomAircraftDescriptor> cads = new RushSearch().find(CustomAircraftDescriptor.class);
        for (CustomAircraftDescriptor cad : cads) {
            aircraftDescriptorMap.put(cad.address, new CustomAircraftDescriptor(cad.address, cad.regNumber, cad.CN, cad.owner, cad.homeBase, cad.model, cad.freq));
        }
    }

    public void saveCustomAircraftDescriptor(CustomAircraftDescriptor cad) {
        if (cad.isEmpty()) {
            aircraftDescriptorMap.remove(cad.address);
            cad = new RushSearch().whereEqual("address", cad.address).findSingle(CustomAircraftDescriptor.class);
            if (cad != null) {
                cad.delete();
            }
        } else {
            aircraftDescriptorMap.put(cad.address, cad);
            cad.save();
        }
    }

    @Override
    public AircraftDescriptor findDescriptor(String address) {
        CustomAircraftDescriptor cad = aircraftDescriptorMap.get(address);
        if (cad != null) {
            return new AircraftDescriptorImpl(cad.regNumber, cad.CN, cad.owner, cad.homeBase, cad.model, cad.freq, true, true);
        } else {
            return null;
        }
    }

    public Map<String, CustomAircraftDescriptor> getAircraftDescriptorMap() {
        return aircraftDescriptorMap;
    }
}

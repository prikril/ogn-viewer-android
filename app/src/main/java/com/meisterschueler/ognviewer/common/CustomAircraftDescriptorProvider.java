package com.meisterschueler.ognviewer.common;

import android.os.Environment;
import android.util.Log;

import com.meisterschueler.ognviewer.CustomAircraftDescriptor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.beacon.impl.AircraftDescriptorImpl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class CustomAircraftDescriptorProvider implements AircraftDescriptorProvider {

    private Map<String, CustomAircraftDescriptor> aircraftDescriptorMap = new HashMap<>();

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

    public void removeCustomAircraftDescriptor(CustomAircraftDescriptor cad) {
        aircraftDescriptorMap.remove(cad.address);
        cad = new RushSearch().whereEqual("address", cad.address).findSingle(CustomAircraftDescriptor.class);
        if (cad != null) {
            cad.delete();
        }
    }

    public void removeAll() {
        RushCore.getInstance().deleteAll(CustomAircraftDescriptor.class);
        aircraftDescriptorMap = new HashMap<>();
    }

    public void writeToFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, "ognviewer_export.csv");
        try (
                FileWriter fileWriter = new FileWriter(file);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT
                        .withHeader("Address", "RegNumber", "CN", "Owner", "Model"));
        ) {
            for (CustomAircraftDescriptor cad : aircraftDescriptorMap.values()) {
                csvPrinter.printRecord(cad.address, cad.regNumber, cad.CN, cad.owner, cad.model);
            }
        } catch (IOException ex) {
            Log.d("WTF", ex.getMessage());
        }
    }

    public void readFromFile(File file) {
        try (
                FileReader reader = new FileReader(file);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withHeader("Address", "RegNumber", "CN", "Owner", "Model")
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            for (CSVRecord csvRecord : csvParser) {
                if (!aircraftDescriptorMap.containsKey(csvRecord.get("Address"))) {
                    CustomAircraftDescriptor cad = new CustomAircraftDescriptor(csvRecord.get("Address"),
                            csvRecord.get("RegNumber"), csvRecord.get("CN"), csvRecord.get("Owner"), "", csvRecord.get("Model"), "");
                    saveCustomAircraftDescriptor(cad);
                }
            }
        } catch (IOException ex) {
            Log.d("WTF", ex.getMessage());
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

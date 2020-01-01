package com.meisterschueler.ognviewer.common;

import android.os.Environment;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;
import timber.log.Timber;

public class CustomAircraftDescriptorProvider implements AircraftDescriptorProvider {

    private Map<String, CustomAircraftDescriptor> aircraftDescriptorMap = new HashMap<>();

    private final String CSV_ADDRESS_KEY = "Address";
    private final String CSV_REGNUMBER_KEY = "RegNumber";
    private final String CSV_COMPETITIONNAME_KEY = "CN";
    private final String CSV_OWNER_KEY = "Owner";
    private final String CSV_MODEL_KEY = "Model";


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
        aircraftDescriptorMap.clear();
    }

    public String writeToFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        dir = new File(dir, "ogn");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String currentTimeString = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        String defaultFilename = "ognviewer_export_" + currentTimeString +".csv";

        File file = new File(dir, defaultFilename);
        try (
                FileWriter fileWriter = new FileWriter(file);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT
                        .withHeader(CSV_ADDRESS_KEY, CSV_REGNUMBER_KEY, CSV_COMPETITIONNAME_KEY,
                                CSV_OWNER_KEY, CSV_MODEL_KEY));
        ) {
            for (CustomAircraftDescriptor cad : aircraftDescriptorMap.values()) {
                csvPrinter.printRecord(cad.address, cad.regNumber, cad.CN, cad.owner, cad.model);
            }
            return file.getCanonicalPath();
        } catch (IOException ex) {
            Timber.wtf("Error while exporting aircraft to file. %s", ex.getMessage());
            return null;
        }
    }

    public int readFromFile(File file) {
        int importCount = -1;
        try (
                FileReader reader = new FileReader(file);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withHeader(CSV_ADDRESS_KEY, CSV_REGNUMBER_KEY, CSV_COMPETITIONNAME_KEY, CSV_OWNER_KEY, CSV_MODEL_KEY)
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            importCount = 0;
            for (CSVRecord csvRecord : csvParser) {
                if (!aircraftDescriptorMap.containsKey(csvRecord.get(CSV_ADDRESS_KEY))) {
                    CustomAircraftDescriptor cad = new CustomAircraftDescriptor(csvRecord.get(CSV_ADDRESS_KEY),
                            csvRecord.get(CSV_REGNUMBER_KEY), csvRecord.get(CSV_COMPETITIONNAME_KEY),
                            csvRecord.get(CSV_OWNER_KEY), "", csvRecord.get(CSV_MODEL_KEY), "");
                    saveCustomAircraftDescriptor(cad);
                    importCount++;
                }
            }
            return importCount;
        } catch (IOException ex) {
            Timber.wtf("Error while importing aircraft from file. %s", ex.getMessage());
            return -1;
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

    public String getAprsBudlistFilter() {
        String buddies = "";
        for (CustomAircraftDescriptor cad : aircraftDescriptorMap.values()) {
            buddies += "/FLR" + cad.address + "/ICA" + cad.address + "/OGN" + cad.address + "/FNT" + cad.address;
        }
        if (buddies.isEmpty()) {
            return "";
        } else {
            return "b" + buddies;
        }
    }
}

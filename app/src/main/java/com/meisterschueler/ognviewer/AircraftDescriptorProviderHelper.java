package com.meisterschueler.ognviewer;


import org.ogn.client.OgnClient;
import org.ogn.client.OgnClientFactory;
import org.ogn.commons.beacon.descriptor.AircraftDescriptorProvider;
import org.ogn.commons.db.FileDbDescriptorProvider;
import org.ogn.commons.db.ogn.OgnDb;

public enum AircraftDescriptorProviderHelper {
    INSTANCE;

    private static final AircraftDescriptorProvider adp1 = new CustomAircraftDescriptorProvider();
    private static final AircraftDescriptorProvider adp2 = new FileDbDescriptorProvider<OgnDb>(OgnDb.class);
    private static final OgnClient ognClient = OgnClientFactory.createClient(new AircraftDescriptorProvider[]{adp1, adp2});

    public static final AircraftDescriptorProvider getCustomDbAircraftDescriptorProvider() {
        return adp1;
    }

    public static final AircraftDescriptorProvider getOgnDbAircraftDescriptorProvider() {
        return adp2;
    }

    public static final OgnClient getOgnClient() {
        return ognClient;
    }
}

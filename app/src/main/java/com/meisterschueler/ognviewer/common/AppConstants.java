package com.meisterschueler.ognviewer.common;


public class AppConstants {
    // permission request codes
    public static final int REQUEST_CODE_LOCATION_ZOOM = 54321;
    public static final int REQUEST_CODE_LOCATION_FILTER = 2468;
    public static final int REQUEST_CODE_LOCATION_TCP_UPDATES = 1122334455;
    public static final int REQUEST_CODE_LOCATION_TCP_UPDATES_FROM_SERVICE = 1234;

    /**
     * after this time inactive aircraft are removed
     */
    public static final int DEFAULT_AIRCRAFT_TIMEOUT_IN_SEC = 300;

    /**
     * zoom factor for "go to current location"
     */
    public static final int DEFAULT_MAP_ZOOM = 7;

    /**
     * request code for settings activity
     */
    public static final int ACTIVITY_REQUEST_CODE_SETTINGS = 2000;

    public static final int TCP_SERVER_PORT = 4353;
}

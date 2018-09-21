package com.meisterschueler.ognviewer.common;


public class AppConstants {
    // permission request codes
    public static final int REQUEST_CODE_LOCATION_ZOOM = 54321;
    public static final int REQUEST_CODE_LOCATION_FILTER = 2468;
    public static final int REQUEST_CODE_LOCATION_TCP_UPDATES = 1122334455;
    public static final int REQUEST_CODE_LOCATION_TCP_UPDATES_FROM_SERVICE = 1234;
    public static final int REQUEST_CODE_STORAGE_IMPORT = 2018092101;
    public static final int REQUEST_CODE_STORAGE_EXPORT = 2018092102;

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

    /**
     * TCP port for other apps to connect
     */
    public static final int TCP_SERVER_PORT = 4353;

    /**
     * REST API base url for flightpath
     */
    public static final String FLIGHTPATH_API_BASE_URL = "http://ogn.dominik-p.de:18820/api/";

    /**
     * restore markers after a little delay to prevent black screen issue
     */
    public static final int RESTORE_MAP_AFTER_DELAY_IN_MS = 700;

    /**
     * minimal time between beacons for the same aircraft in MS
     */
    public static final int MINIMAL_AIRCRAFT_DIFF_TIME_IN_MS = 500;

    /**
     * action name for emergency exit intent
     */
    public static final String EMERGENCY_EXIT_INTENT_ACTION_NAME = "EMERGENCY_EXIT";

    /**
     * minimal value for coloration of altitude in meters
     */
    public static final float MIN_ALT_FOR_COLORATION = 500.0f;

    /**
     * maximal value for coloration of altitude in meters
     */
    public static final float MAX_ALT_FOR_COLORATION = 3000.0f;


}

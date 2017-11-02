package com.meisterschueler.ognviewer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.meisterschueler.ognviewer.common.FlarmMessage;
import com.meisterschueler.ognviewer.common.ReceiverBundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.ogn.client.AircraftBeaconListener;
import org.ogn.client.OgnClient;
import org.ogn.client.ReceiverBeaconListener;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.AircraftType;
import org.ogn.commons.beacon.ReceiverBeacon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;

public class OgnService extends Service implements AircraftBeaconListener, ReceiverBeaconListener {

    TcpServer tcpServer;

    OgnClient ognClient;
    boolean connected = false;
    LocalBroadcastManager localBroadcastManager;
    IBinder binder = new LocalBinder();
    Map<String, ReceiverBundle> receiverMap = new ConcurrentHashMap<>();
    //Map<String, AircraftBundle> aircraftMap = new ConcurrentHashMap<>(); //von upstream 2017-11-02
    private Map<String,Aircraft> aircraftMap = new HashMap<>(); //TODO: von dominik, entfernen? 2017-11-02

    int maxAircraftCounter = 0;
    int maxBeaconCounter = 0;
    Map<String, ReceiverBundle> receiverBundleMap = new ConcurrentHashMap<String, ReceiverBundle>();
    Map<String, AircraftBundle> aircraftBundleMap = new ConcurrentHashMap<String, AircraftBundle>();


    LocationManager locManager;
    Location currentLocation = null;
    ScheduledExecutorService scheduleTaskExecutor;
    boolean refreshingActive = true;

    public interface UpdateListener {
        public void updateAircraftBundle(AircraftBundle bundle);
    }

    public OgnService() {
        tcpServer = new TcpServer();
        tcpServer.startServer();
        Runnable locationUpdate = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    try {
                        currentLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (currentLocation != null) {
                            tcpServer.updatePosition(currentLocation);
                        }
                    } catch (SecurityException se) {
                        // accessing location is forbidden
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        Thread locationUpdateThread = new Thread(locationUpdate);
        locationUpdateThread.start();
    }

    @Override
    public void onUpdate(AircraftBeacon aircraftBeacon, AircraftDescriptor aircraftDescriptor) {
        AircraftBundle bundle = new AircraftBundle(aircraftBeacon, aircraftDescriptor);
        aircraftBundleMap.put(aircraftBeacon.getAddress(), bundle);

        ReceiverBundle receiverBundle = receiverMap.get(aircraftBeacon.getReceiverName());
        if (receiverBundle != null) {
            if (!receiverBundle.aircrafts.contains(aircraftBeacon.getId())) {
                receiverBundle.aircrafts.add(aircraftBeacon.getId());
            }
            receiverBundle.beaconCount++;

            maxAircraftCounter = Math.max(maxAircraftCounter, receiverBundle.aircrafts.size());
            maxBeaconCounter = Math.max(maxBeaconCounter, receiverBundle.beaconCount);
        }
        if (!mapCurrentlyUpdating) {
            sendAircraftToMap(bundle);
        }



        //for debugging
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        System.out.println(aircraftBundleMap.size() + " AircraftBeacons " + minutes + ":" + seconds); //debug
        System.out.println("Last address: " + aircraftBeacon.getAddress()); //debug
    }

    private void sendAircraftToMap(AircraftBundle aircraftBundle) {
        AircraftBeacon aircraftBeacon = aircraftBundle.aircraftBeacon;
        AircraftDescriptor aircraftDescriptor = aircraftBundle.aircraftDescriptor;
        Intent intent = new Intent("AIRCRAFT-BEACON");

        // AircraftBeacon
        intent.putExtra("receiverName", aircraftBeacon.getReceiverName());
        intent.putExtra("addressType", aircraftBeacon.getAddressType().getCode());
        intent.putExtra("address", aircraftBeacon.getAddress());
        intent.putExtra("aircraftType", aircraftBeacon.getAircraftType().getCode());
        intent.putExtra("stealth", aircraftBeacon.isStealth());
        intent.putExtra("climbRate", aircraftBeacon.getClimbRate());
        intent.putExtra("turnRate", aircraftBeacon.getTurnRate());
        intent.putExtra("signalStrength", aircraftBeacon.getSignalStrength());
        intent.putExtra("frequencyOffset", aircraftBeacon.getFrequencyOffset());
        intent.putExtra("gpsStatus", aircraftBeacon.getGpsStatus());
        intent.putExtra("errorCount", aircraftBeacon.getErrorCount());
        //String[] getHeardAircraftIds();

        // OgnBeacon
        intent.putExtra("id", aircraftBeacon.getId());
        intent.putExtra("timestamp", aircraftBeacon.getTimestamp());
        intent.putExtra("lat", aircraftBeacon.getLat());
        intent.putExtra("lon", aircraftBeacon.getLon());
        intent.putExtra("alt", aircraftBeacon.getAlt());
        intent.putExtra("track", aircraftBeacon.getTrack());
        intent.putExtra("groundSpeed", aircraftBeacon.getGroundSpeed());
        intent.putExtra("rawPacket", aircraftBeacon.getRawPacket());

        // AircraftDescriptor
        if (aircraftDescriptor != null) {
            intent.putExtra("known", aircraftDescriptor.isKnown());
            intent.putExtra("regNumber", aircraftDescriptor.getRegNumber());
            intent.putExtra("CN", aircraftDescriptor.getCN());
            intent.putExtra("owner", aircraftDescriptor.getOwner());
            intent.putExtra("homeBase", aircraftDescriptor.getHomeBase());
            intent.putExtra("model", aircraftDescriptor.getModel());
            intent.putExtra("freq", aircraftDescriptor.getFreq());
            intent.putExtra("tracked", aircraftDescriptor.isTracked());
            intent.putExtra("identified", aircraftDescriptor.isIdentified());
        }

        if (refreshingActive) {
            localBroadcastManager.sendBroadcast(intent);
        }

        tcpServer.addFlarmMessage(new FlarmMessage(aircraftBeacon));
    }

    @Override
    public void onUpdate(ReceiverBeacon receiverBeacon) {
        ReceiverBundle bundle = receiverMap.get(receiverBeacon.getId());
        if (bundle == null) {
            bundle = new ReceiverBundle(receiverBeacon);
        }

        receiverMap.put(receiverBeacon.getId(), bundle);

        Intent intent = new Intent("RECEIVER-BEACON");

        // ReceiverBeacon
        //intent.putExtra("cpuLoad", receiverBeacon.getCpuLoad());
        //intent.putExtra("cpuTemp", receiverBeacon.getCpuTemp());
        //intent.putExtra("freeRam", receiverBeacon.getFreeRam());
        //intent.putExtra("totalRam", receiverBeacon.getTotalRam());
        //intent.putExtra("ntpError", receiverBeacon.getNtpError());
        //intent.putExtra("rtCrystalCorrection", receiverBeacon.getRtCrystalCorrection());
        //intent.putExtra("recCrystalCorrection", receiverBeacon.getRecCrystalCorrection());
        //intent.putExtra("recCrystalCorrectionFine", receiverBeacon.getRecCrystalCorrectionFine());
        //intent.putExtra("recAbsCorrection", receiverBeacon.getRecAbsCorrection());
        intent.putExtra("recInputNoise", receiverBeacon.getRecInputNoise());
        //intent.putExtra("serverName", receiverBeacon.getServerName());
        intent.putExtra("version", receiverBeacon.getVersion());
        intent.putExtra("platform", receiverBeacon.getPlatform());
        intent.putExtra("numericVersion", receiverBeacon.getNumericVersion());

        // OgnBeacon
        intent.putExtra("id", receiverBeacon.getId());
        intent.putExtra("timestamp", receiverBeacon.getTimestamp());
        intent.putExtra("lat", receiverBeacon.getLat());
        intent.putExtra("lon", receiverBeacon.getLon());
        intent.putExtra("alt", receiverBeacon.getAlt());
        //intent.putExtra("track", receiverBeacon.getTrack());
        //intent.putExtra("groundSpeed", receiverBeacon.getGroundSpeed());
        //intent.putExtra("rawPacket", receiverBeacon.getRawPacket());

        // Computed Values
        maxAircraftCounter = Math.max(maxAircraftCounter, bundle.aircrafts.size());
        maxBeaconCounter = Math.max(maxBeaconCounter, bundle.beaconCount);

        intent.putExtra("aircraftCounter", bundle.aircrafts.size());
        intent.putExtra("maxAircraftCounter", maxBeaconCounter);
        intent.putExtra("beaconCounter", bundle.beaconCount);
        intent.putExtra("maxBeaconCounter", maxBeaconCounter);

        localBroadcastManager.sendBroadcast(intent);
        //for debugging
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        System.out.println(receiverBundleMap.size() + " ReceiverBeacons " + minutes + ":" + seconds);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //RushCore.initialize(new AndroidInitializeConfig(getApplicationContext()));
        List<Class<? extends Rush>> classes = new ArrayList<>();
        classes.add(CustomAircraftDescriptor.class);
        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
        config.setClasses(classes);
        RushCore.initialize(config);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        ognClient = AircraftDescriptorProviderHelper.getOgnClient();
        ognClient.subscribeToAircraftBeacons(this);
        ognClient.subscribeToReceiverBeacons(this);

        String versionName = "?";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MapsActivity.class), 0);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_stat)
                .setContentTitle("OGN Viewer")
                .setContentText("Version " + versionName)
                .setContentIntent(pendingIntent)
                .getNotification();

        startForeground(R.string.notification_id, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String aprs_filter = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.key_aprsfilter_preference), "");

        if (connected) {
            ognClient.disconnect();
        }

        if (aprs_filter.isEmpty()) {
            ognClient.connect();
            connected = true;
            Toast.makeText(this, "Connected to OGN without filter", Toast.LENGTH_LONG).show();
        } else {
            ognClient.connect(aprs_filter);
            connected = true;
            Toast.makeText(this, "Connected to OGN. Filter: " + aprs_filter, Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ognClient.disconnect();
        connected = false;

        Toast.makeText(this, "Disconnected from OGN", Toast.LENGTH_LONG).show();

        tcpServer.stopServer();
    }


    private void updateAircraftBeaconMarkerInDB(String address, AircraftType aircraftType, float climbRate,
                                                double lat, double lon, float alt, float groundSpeed,
                                                String regNumber, String cn, String model, boolean isOgnPrivate,
                                                String receiverName, int track) {
        if (!aircraftMap.containsKey(address)) {
            Aircraft aircraft = new Aircraft(address, aircraftType, climbRate, lat, lon, alt, groundSpeed, regNumber,
                    cn, model, isOgnPrivate, receiverName, track);
            aircraftMap.put(address, aircraft);
        } else {
            Aircraft aircraft = aircraftMap.get(address);
            aircraft.setAlt(alt);
            aircraft.setClimbRate(climbRate);
            aircraft.setCN(cn);
            aircraft.setGroundSpeed(groundSpeed);
            aircraft.setLastSeen(new Date());
            aircraft.setLat(lat);
            aircraft.setLon(lon);
            aircraft.setReceiverName(receiverName);
            aircraft.setTrack(track);
        }

    }
    boolean timerCurrentlyRunning = false;
    LatLngBounds latLngBounds = new LatLngBounds(new LatLng(0, 0), new LatLng(0, 0));

    public void resumeUpdatingMap(LatLngBounds latLngBounds) {
        this.latLngBounds = latLngBounds;
        refreshingActive = true;
        resumeUpdatingMap_Alt();
        System.out.println("Service resumed updating map");
    }

    public void resumeUpdatingMap_Alt() {
        scheduleTaskExecutor = new ScheduledThreadPoolExecutor(1);
        scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //TODO: add try catches
                if (timerCurrentlyRunning) {
                    return;
                } else {
                    timerCurrentlyRunning = true;
                    System.out.println("UpdateMap by timer " + new Date());
                }
                aircraftMap = convertAricraftMap(aircraftBundleMap);
                Iterator<String> it = aircraftMap.keySet().iterator();
                while (it.hasNext()) {
                    String address = it.next();
                    Aircraft aircraft = aircraftMap.get(address);
                    Date now = new Date();
                    long diffSeconds = (now.getTime() - aircraftMap.get(address).getLastSeen().getTime()) / 1000;
                    if (diffSeconds > 60) { // remove markers that are older than 60 seconds
                        Intent intent = new Intent("AIRCRAFT_ACTION");
                        //intent.setAction("REMOVE_AIRCRAFT");
                        intent.putExtra("AIRCRAFT_ACTION", "REMOVE_AIRCRAFT");
                        intent.putExtra("address", address);
                        localBroadcastManager.sendBroadcast(intent);

                        it.remove();
                        aircraftBundleMap.remove(address); //possible on concurrent map?
                        System.out.println("Removed " + address + ", diff: " + diffSeconds + "s"); //debug
                        continue;
                    }
                    if (latLngBounds.contains(new LatLng(aircraft.getLat(), aircraft.getLon()))) {
                        /*while (mapCurrentlyUpdating) {
                            //wait for finishing updateMaker
                            System.out.println("Waiting for end of updateMarker: " + address);
                        }
                        sendAircraftToMap(aircraftBundleMap.get(address)); //refresh only visible markers*/
                    }
                    /*LatLngBounds latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                    if (latLngBounds.contains(new LatLng(aircraft.getLat(), aircraft.getLon()))) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateAircraftBeaconMarkerOnMap(aircraft.getAddress(), aircraft.getAircraftType(),
                                        aircraft.getClimbRate(), aircraft.getLat(), aircraft.getLon(),
                                        aircraft.getAlt(), aircraft.getGroundSpeed(), aircraft.getRegNumber(),
                                        aircraft.getCN(), aircraft.getModel(), aircraft.isOgnPrivate(),
                                        aircraft.getReceiverName(), aircraft.getTrack());
                            }

                        });
                    }*/
                }
                System.out.println("Finished updating map.");
                timerCurrentlyRunning = false;
            }
        }, 2000, 2000, TimeUnit.MILLISECONDS); //Update aircrafts every second
    }

    public void pauseUpdatingMap() {
        refreshingActive = false; //blocks intents to activity
        //mapTimer.cancel();
        //mapTimer.purge();
        if (scheduleTaskExecutor != null) {
            scheduleTaskExecutor.shutdownNow();
            scheduleTaskExecutor = null;
        }

        timerCurrentlyRunning = false;

        System.out.println("Service paused updating map");
    }

    private Map<String, Aircraft> convertAricraftMap(Map<String, AircraftBundle> aircraftBundleMap) {
        Map<String, Aircraft> resultMap = new HashMap<>();
        for (String address : aircraftBundleMap.keySet()) {
            AircraftBundle bundle = aircraftBundleMap.get(address);
            AircraftBeacon beacon = bundle.aircraftBeacon;
            AircraftDescriptor descriptor = bundle.aircraftDescriptor;

            boolean isOgnPrivate = descriptor.isKnown() && (!descriptor.isTracked() || !descriptor.isIdentified());
            Aircraft aircraft = new Aircraft(beacon.getAddress(),
                    beacon.getAircraftType(),beacon.getClimbRate(), beacon.getLat(), beacon.getLon(), beacon.getAlt(), beacon.getGroundSpeed(),
                    descriptor.getRegNumber(), descriptor.getCN(), descriptor.getModel(), isOgnPrivate, beacon.getReceiverName(), beacon.getTrack());
            aircraft.setLastSeen(new Date(beacon.getTimestamp()));
            resultMap.put(address, aircraft);
        }

        return resultMap;
    }

    boolean mapCurrentlyUpdating = false;
    public void mapUpdatingStatus(boolean updating) {
        mapCurrentlyUpdating = updating;
    }


    public class LocalBinder extends Binder {
        OgnService getService() {
            return OgnService.this;
        }
    }
}

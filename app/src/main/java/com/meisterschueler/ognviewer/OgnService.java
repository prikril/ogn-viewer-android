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

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.ogn.client.AircraftBeaconListener;
import org.ogn.client.OgnClient;
import org.ogn.client.ReceiverBeaconListener;
import org.ogn.commons.beacon.AircraftBeacon;
import org.ogn.commons.beacon.AircraftDescriptor;
import org.ogn.commons.beacon.ReceiverBeacon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;

public class OgnService extends Service implements AircraftBeaconListener, ReceiverBeaconListener {

    TcpServer tcpServer;

    OgnClient ognClient;
    boolean connected = false;
    LocalBroadcastManager localBroadcastManager;
    IBinder binder = new LocalBinder();
    Map<String, ReceiverBundle> receiverMap = new ConcurrentHashMap<String, ReceiverBundle>();
    Map<String, AircraftBundle> aircraftMap = new ConcurrentHashMap<String, AircraftBundle>();

    int maxAircraftCounter = 0;
    int maxBeaconCounter = 0;

    LocationManager locManager;
    Location currentLocation = null;

    public OgnService() {
        tcpServer = new TcpServer();
        tcpServer.startServer();
    }

    @Override
    public void onUpdate(AircraftBeacon aircraftBeacon, AircraftDescriptor aircraftDescriptor) {
        AircraftBundle bundle = new AircraftBundle(aircraftBeacon, aircraftDescriptor);
        aircraftMap.put(aircraftBeacon.getAddress(), bundle);

        ReceiverBundle receiverBundle = receiverMap.get(aircraftBeacon.getReceiverName());
        if (receiverBundle != null) {
            if (!receiverBundle.aircrafts.contains(aircraftBeacon.getId())) {
                receiverBundle.aircrafts.add(aircraftBeacon.getId());
            }
            receiverBundle.beaconCount++;

            maxAircraftCounter = Math.max(maxAircraftCounter, receiverBundle.aircrafts.size());
            maxBeaconCounter = Math.max(maxBeaconCounter, receiverBundle.beaconCount);
        }

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

        localBroadcastManager.sendBroadcast(intent);


        // TCP stuff
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        FlarmMessage flarmMessage = new FlarmMessage(aircraftBeacon, currentLocation);
        tcpServer.sendMessage(flarmMessage.toString());
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

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_stat)
                .getNotification();

        String versionName = "?";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MapsActivity.class), 0);
        notification.setLatestEventInfo(this, "OGN Viewer", "Version " + versionName, pendingIntent);

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

    public class LocalBinder extends Binder {
        OgnService getService() {
            return OgnService.this;
        }
    }
}

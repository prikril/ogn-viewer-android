package com.meisterschueler.ognviewer.common;

import android.location.Location;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FlarmMessageSenderTask extends AsyncTask<Object, Void, Void> {
    private static final long MAX_TIME = 30000; // keep flarm position for 30s
    private static final int MIN_DISTANCE = 2000; // if distance to flarm is < 2000m it wont be shown
    private Socket clientSocket;
    private Map<String, FlarmMessage> messageMap = new ConcurrentHashMap<>();
    private Location ownLocation;

    private  FlarmMessageSenderTask() {
        // don't allow instances without params
    }

    public FlarmMessageSenderTask(Socket clientSocket, Map<String, FlarmMessage> messageMap, Location ownLocation) {
        this.clientSocket = clientSocket;
        this.messageMap = messageMap;
        this.ownLocation = ownLocation;
    }

    @Override
    protected Void doInBackground(Object[] objects) {
        long time = Calendar.getInstance().getTime().getTime();
        DataOutputStream objectOutput;

        try {
            objectOutput = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        for(Iterator<Map.Entry<String, FlarmMessage>> it = messageMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, FlarmMessage> entry = it.next();
            if(time - entry.getValue().getTime() > MAX_TIME) {
                it.remove();
            } else {
                FlarmMessage flarmMessage = entry.getValue();
                flarmMessage.setOwnLocation(ownLocation);
                if (flarmMessage.getDistance() < MIN_DISTANCE) {
                    continue;
                }
                String message = flarmMessage.toString();
                try {
                    objectOutput.write((message + "\r\n").getBytes("US-ASCII"));
                } catch (IOException e) {
                    //e.printStackTrace();
                    try {
                        clientSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}

package com.meisterschueler.ognviewer;


import android.location.Location;

import com.meisterschueler.ognviewer.common.FlarmMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TcpServer {
    private static final long MAX_TIME = 30000; // keep flarm position for 30s
    private static final int MIN_DISTANCE = 2000; // if distance to flarm is < 2000m it wont be shown
    private Socket clientSocket = null;
    private boolean stopped = true;
    private Map<String, FlarmMessage> messageMap = new ConcurrentHashMap<>();

    public void startServer() {
        stopped = false;
        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(4353);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (!stopped && serverSocket != null) {
                    if (clientSocket == null || clientSocket.isClosed()) {
                        try {
                            clientSocket = serverSocket.accept();
                            //playNMEA();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }

    public void stopServer() {
        stopped = true;
        if (clientSocket != null) {
            try {
                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePosition(Location location) {
        if (clientSocket == null || clientSocket.isClosed()) {
            return;
        }

        long time = Calendar.getInstance().getTime().getTime();
        DataOutputStream objectOutput = null;

        try {
            objectOutput = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for(Iterator<Map.Entry<String, FlarmMessage>> it = messageMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, FlarmMessage> entry = it.next();
            if(time - entry.getValue().getTime() > MAX_TIME) {
                it.remove();
            } else {
                FlarmMessage flarmMessage = entry.getValue();
                flarmMessage.setOwnLocation(location);
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
    }

    public void addFlarmMessage(FlarmMessage flarmMessage) {
        messageMap.put(flarmMessage.getID(), flarmMessage);
    }
}

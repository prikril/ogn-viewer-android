package com.meisterschueler.ognviewer;


import android.location.Location;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TcpServer {
    private static final long MAX_TIME = 10000;
    private Socket clientSocket = null;
    private boolean stopped = true;
    private Map<String, FlarmMessage> messageMap = new HashMap<String, FlarmMessage>();

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

    public void playNMEA() {
        String file = "res/raw/logfile.nmea";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
        BufferedInputStream bi = new BufferedInputStream(in);

        BufferedReader r = new BufferedReader(new InputStreamReader(bi));
        DataOutputStream objectOutput = null;
        try {
            while (true) {
                String line = r.readLine();
                objectOutput = new DataOutputStream(clientSocket.getOutputStream());
                objectOutput.write((line + "\r\n").getBytes("US-ASCII"));
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void updatePosition(Location location) {
        if (clientSocket == null) {
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
                String message = flarmMessage.toString();
                try {
                    objectOutput.write((message + "\r\n").getBytes("US-ASCII"));
                    System.err.println(message);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println("Dat ding is zu...");
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

package com.meisterschueler.ognviewer.service;


import android.location.Location;

import com.meisterschueler.ognviewer.common.AppConstants;
import com.meisterschueler.ognviewer.common.FlarmMessage;
import com.meisterschueler.ognviewer.common.FlarmMessageSenderTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TcpServer {
    private Thread serverThread;
    private ServerSocket serverSocket;
    private Socket clientSocket = null;
    private boolean stopped = true;
    private Map<String, FlarmMessage> messageMap = new ConcurrentHashMap<>();

    public void startServer() {
        stopped = false;
        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(AppConstants.TCP_SERVER_PORT);
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
        serverThread = new Thread(serverTask);
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
        if (serverThread != null) {
            serverThread.interrupt();
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePosition(Location ownLocation) {
        if (clientSocket == null || clientSocket.isClosed()) {
            return;
        }
        // since Android 7 network requests must be async to avoid NetworkOnMainThreadException
        new FlarmMessageSenderTask(clientSocket, messageMap, ownLocation).execute();
    }

    public void addFlarmMessage(FlarmMessage flarmMessage) {
        messageMap.put(flarmMessage.getID(), flarmMessage);
    }
}

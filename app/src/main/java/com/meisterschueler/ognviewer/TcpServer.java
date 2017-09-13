package com.meisterschueler.ognviewer;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    private Socket clientSocket = null;
    private boolean stopped = true;

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
                            System.out.println("Waiting for client to connect...");
                            clientSocket = serverSocket.accept();
                            System.out.println("Connected");
                        } catch (IOException e) {
                            System.err.println("Unable to process client request");
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
        System.err.println("Called stopServer");
        if (clientSocket != null) {
            try {
                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        System.err.println("Called sendMessage: " + message);
        DataOutputStream objectOutput = null;
        try {
            objectOutput = new DataOutputStream(clientSocket.getOutputStream());
            objectOutput.write((message + "\n").getBytes("US-ASCII"));
        } catch (IOException e) {
            e.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}

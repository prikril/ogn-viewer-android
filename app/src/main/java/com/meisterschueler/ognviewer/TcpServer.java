package com.meisterschueler.ognviewer;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                            clientSocket = serverSocket.accept();
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

    public void sendMessage(String message) {
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

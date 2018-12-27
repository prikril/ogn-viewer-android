package com.meisterschueler.ognviewer.common;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class FlarmMessagePFLAUSenderTask extends AsyncTask<Object, Void, Void> {

    // This sender task sends a PFLAU message. This messages shows that the "flarm" is alive.
    // See here: https://github.com/Meisterschueler/ogn-viewer-android/issues/17

    private Socket clientSocket;

    private FlarmMessagePFLAUSenderTask() {
        // don't allow instances without params
    }

    public FlarmMessagePFLAUSenderTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    protected Void doInBackground(Object[] objects) {
        DataOutputStream objectOutput;

        try {
            objectOutput = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String message = "$PFLAU,0,1,1,1,0,,0,,*63";

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

        return null;
    }
}

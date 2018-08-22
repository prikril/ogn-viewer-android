package com.meisterschueler.ognviewer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ClosingActivity extends Activity {



    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            stopService(new Intent(getBaseContext(), OgnService.class));
            Intent exitIntent = new Intent("EMERGENCY_EXIT");
            sendBroadcast(exitIntent);
            finish();
        }

        public void onServiceDisconnected(ComponentName className) {
            //this only happens when something goes wrong
            //it does not happen when activity is paused or destroyed
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, OgnService.class), mConnection, Context.BIND_AUTO_CREATE);
    }
}

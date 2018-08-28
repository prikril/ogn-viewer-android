package com.meisterschueler.ognviewer.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.meisterschueler.ognviewer.common.AppConstants;
import com.meisterschueler.ognviewer.service.OgnService;

public class ClosingActivity extends Activity {

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            stopService(new Intent(getBaseContext(), OgnService.class));
            Intent exitIntent = new Intent(AppConstants.EMERGENCY_EXIT_INTENT_ACTION_NAME);
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

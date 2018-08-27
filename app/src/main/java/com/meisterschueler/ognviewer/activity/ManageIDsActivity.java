package com.meisterschueler.ognviewer.activity;


import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import com.meisterschueler.ognviewer.activity.KillBroadcastReceiver;
import com.meisterschueler.ognviewer.ui.ManageIDsFragment;

public class ManageIDsActivity extends Activity {

    KillBroadcastReceiver killBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        killBroadcastReceiver = new KillBroadcastReceiver(this);
        registerReceiver(killBroadcastReceiver, new IntentFilter("EMERGENCY_EXIT"));

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ManageIDsFragment())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(killBroadcastReceiver);
    }

}

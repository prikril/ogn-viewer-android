package com.meisterschueler.ognviewer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillBroadcastReceiver extends BroadcastReceiver{
// idea from https://stackoverflow.com/questions/5453206/how-to-close-all-the-activities-of-my-application/5453228#5453228
    Activity callbackActivity;

    public KillBroadcastReceiver(Activity activity) {
        this.callbackActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (callbackActivity != null) {
            callbackActivity.finish();
        }
    }

}

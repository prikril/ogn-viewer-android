package com.meisterschueler.ognviewer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.meisterschueler.ognviewer.ui.PrefsFragment;

import timber.log.Timber;

public class PrefsActivity extends Activity {
    PrefsFragment prefsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefsFragment = new PrefsFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, prefsFragment)
                .commit();

        Intent intent = new Intent();
        intent.putExtra("MESSAGE","Prefs Activity finished");
        setResult(2, intent); //TODO: replace 2 with constant
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        final int REQUEST_CODE = 1122334455; // TODO: extract this constant
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    prefsFragment.setTCPServerActiveState(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // Toast.makeText(getApplicationContext(), R.string.empty_aprs_filter_toast, Toast.LENGTH_LONG).show();
                    Timber.d("Location permission for TCP Server denied");
                    prefsFragment.setTCPServerActiveState(false);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }

    }
}

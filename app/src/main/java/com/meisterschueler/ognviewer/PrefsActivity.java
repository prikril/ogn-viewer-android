package com.meisterschueler.ognviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.meisterschueler.ognviewer.ui.PrefsFragment;

public class PrefsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();

        Intent intent=new Intent();
        intent.putExtra("MESSAGE","Prefs Activity finished");
        setResult(2, intent); //TODO: replace 2 with constant
    }
}

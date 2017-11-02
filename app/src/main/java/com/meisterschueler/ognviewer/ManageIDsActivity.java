package com.meisterschueler.ognviewer;


import android.app.Activity;
import android.os.Bundle;

import com.meisterschueler.ognviewer.ui.ManageIDsFragment;

public class ManageIDsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ManageIDsFragment())
                .commit();
    }
}

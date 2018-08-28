package com.meisterschueler.ognviewer.activity;


import android.os.Bundle;

import com.meisterschueler.ognviewer.activity.base.BaseActivity;
import com.meisterschueler.ognviewer.ui.ManageIDsFragment;

public class ManageIDsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ManageIDsFragment())
                .commit();
    }

}

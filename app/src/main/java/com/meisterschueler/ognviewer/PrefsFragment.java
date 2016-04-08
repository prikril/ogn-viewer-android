package com.meisterschueler.ognviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        updateFragmentValues(sharedPreferences, getString(R.string.key_aprsfilter_preference));
        //updateFragmentValues(sharedPreferences, getString(R.string.key_symbol_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_colorisation_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_showaircrafts_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_showreceivers_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_shownonmoving_preference));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateFragmentValues(sharedPreferences, key);
        if (key.equals(getString(R.string.key_aprsfilter_preference))) {
            getActivity().startService(new Intent(getActivity(), OgnService.class));
        }
    }

    private void updateFragmentValues(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (key.equals(getString(R.string.key_aprsfilter_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_aprsfilter_preference), "");
            if (value.isEmpty()) {
                pref.setSummary(getString(R.string.empty_aprsfilter_preference));
            } else {
                pref.setSummary(value);
            }
        } else if (key.equals(getString(R.string.key_symbol_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_symbol_preference), "(default)");
            pref.setSummary(value);
        } else if (key.equals(getString(R.string.key_colorisation_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_colorisation_preference), getString(R.string.altitude));
            pref.setSummary(value);
        } else if (key.equals(getString(R.string.key_showaircrafts_preference))) {
            Boolean value = sharedPreferences.getBoolean(getString(R.string.key_showaircrafts_preference), true);
            if (value) {
                pref.setSummary("on");
            } else {
                pref.setSummary("off");
            }
        } else if (key.equals(getString(R.string.key_showreceivers_preference))) {
            Boolean value = sharedPreferences.getBoolean(getString(R.string.key_showreceivers_preference), false);
            if (value) {
                pref.setSummary("on");
            } else {
                pref.setSummary("off");
            }
        } else if (key.equals(getString(R.string.key_shownonmoving_preference))) {
            Boolean value = sharedPreferences.getBoolean(getString(R.string.key_shownonmoving_preference), true);
            if (value) {
                pref.setSummary("on");
            } else {
                pref.setSummary("off");
            }
        }
    }
}

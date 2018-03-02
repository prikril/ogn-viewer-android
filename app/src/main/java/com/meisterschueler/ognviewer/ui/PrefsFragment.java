package com.meisterschueler.ognviewer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.meisterschueler.ognviewer.OgnService;
import com.meisterschueler.ognviewer.R;

public class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        updateFragmentValues(sharedPreferences, getString(R.string.key_aprsfilter_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_showaircrafts_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_showreceivers_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_map_type_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_keepscreenon_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_shownonmoving_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_showregistration_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_rotate_aircraft_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_aircraft_colorisation_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_aircraft_timeout_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_shownotactive_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_receiver_colorisation_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_altitude_unit_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_groundspeed_unit_preference));
        updateFragmentValues(sharedPreferences, getString(R.string.key_verticalspeed_unit_preference));
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

        } else if (key.equals(getString(R.string.key_map_type_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_map_type_preference), getString(R.string.terrain));
            pref.setSummary(value);

        } else if (key.equals(getString(R.string.key_keepscreenon_preference))) {
            Boolean value = sharedPreferences.getBoolean(getString(R.string.key_keepscreenon_preference), false);
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
        } else if (key.equals(getString(R.string.key_showregistration_preference))) {
            Boolean value = sharedPreferences.getBoolean(getString(R.string.key_showregistration_preference), true);
            if (value) {
                pref.setSummary("on");
            } else {
                pref.setSummary("off");
            }
        } else if (key.equals(getString(R.string.key_rotate_aircraft_preference))) {
            Boolean value = sharedPreferences.getBoolean(getString(R.string.key_rotate_aircraft_preference), false);
            if (value) {
                pref.setSummary("on");
            } else {
                pref.setSummary("off");
            }
        } else if (key.equals(getString(R.string.key_shownotactive_preference))) {
            Boolean value = sharedPreferences.getBoolean(getString(R.string.key_shownotactive_preference), true);
            if (value) {
                pref.setSummary("on");
            } else {
                pref.setSummary("off");
            }
        } else if (key.equals(getString(R.string.key_aircraft_colorisation_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_aircraft_colorisation_preference), getString(R.string.altitude));
            pref.setSummary(value);
        } else if (key.equals(getString(R.string.key_aircraft_timeout_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_aircraft_timeout_preference), getString(R.string.time_5m));
            pref.setSummary(value);
        } else if (key.equals(getString(R.string.key_receiver_colorisation_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_receiver_colorisation_preference), getString(R.string.aircraft_count));
            pref.setSummary(value);
        } else if (key.equals(getString(R.string.key_altitude_unit_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_altitude_unit_preference), getString(R.string.unit_meters));
            pref.setSummary(value);
        } else if (key.equals(getString(R.string.key_groundspeed_unit_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_groundspeed_unit_preference), getString(R.string.unit_kmh));
            pref.setSummary(value);
        } else if (key.equals(getString(R.string.key_verticalspeed_unit_preference))) {
            String value = sharedPreferences.getString(getString(R.string.key_verticalspeed_unit_preference), getString(R.string.unit_ms));
            pref.setSummary(value);
        }
    }
}

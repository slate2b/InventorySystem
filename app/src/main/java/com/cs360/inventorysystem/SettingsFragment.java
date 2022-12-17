package com.cs360.inventorysystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static String PREFERENCE_SMS = "pref_sms";
    public static String PREFERENCE_PHONE = "pref_phone";
    public static String PREFERENCE_IN_APP = "pref_in_app";

    private SharedPreferences mSharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preference layout
        addPreferencesFromResource(R.xml.preferences);

        // Access the default shared prefs
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        setPrefSummaryPhone(sharedPrefs);
    }

    /**
     * Set the summary value for the phone preference according to the user's current preferences
     * @param sharedPrefs
     */
    private void setPrefSummaryPhone(SharedPreferences sharedPrefs) {
        String phone = sharedPrefs.getString(PREFERENCE_PHONE, "");
        phone = phone.trim();
        Preference phonePref = findPreference(PREFERENCE_PHONE);
        if (phone.length() == 0) {
            phonePref.setSummary("None");
        }
        else {
            phonePref.setSummary(phone);
        }
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
        if (key.equals(PREFERENCE_SMS)) {
            getActivity().recreate();
        }
        else if (key.equals(PREFERENCE_PHONE)) {
            setPrefSummaryPhone(sharedPreferences);
        }
        else if (key.equals(PREFERENCE_IN_APP)) {
            getActivity().recreate();
        }
    }
}

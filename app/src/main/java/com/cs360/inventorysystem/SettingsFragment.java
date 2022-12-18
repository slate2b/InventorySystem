package com.cs360.inventorysystem;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int SMS_PERMISSION_CODE = 10;

    public static String PREFERENCE_SMS = "pref_sms";
    public static String PREFERENCE_PHONE = "pref_phone";
    public static String PREFERENCE_IN_APP = "pref_in_app";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preference layout
        addPreferencesFromResource(R.xml.preferences);

        // Access shared prefs
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Set preference summary for phone number to show current value
        setPrefSummaryPhone(sharedPreferences);
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
        // If the SMS preference was changed
        if (key.equals(PREFERENCE_SMS)) {

            // Retrieve sms preferences
            boolean smsNotifications = sharedPreferences.getBoolean(SettingsFragment.PREFERENCE_SMS,
                    false);

            // If sms preference is set to true, then request permission
            if (smsNotifications) {
                requestPermission(Manifest.permission.SEND_SMS, SMS_PERMISSION_CODE);
            }

            // Recreate the activity
            getActivity().recreate();
        }
        else if (key.equals(PREFERENCE_PHONE)) {
            setPrefSummaryPhone(sharedPreferences);
        }
        else if (key.equals(PREFERENCE_IN_APP)) {
            getActivity().recreate();
        }
    }

    /**
     * Requests permissions
     * @param permission - The Android Manifest permission to be requested
     * @param requestCode - The request code
     */
    public void requestPermission(String permission, int requestCode)
    {
        // If the permission is currently denied
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            // Request the permission
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            // If permission granted leave SMS preference value as true
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "SMS Permission Granted",
                        Toast.LENGTH_SHORT).show();
            }
            // If permission denied update SMS preference value to false
            else {
                // Access shared preferences
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getActivity());

                // Create a shared preferences editor
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Set the SMS preference to false
                editor.putBoolean(PREFERENCE_SMS, false);
                editor.apply();

                Toast.makeText(getActivity(), "SMS Permission Denied",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}

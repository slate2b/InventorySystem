package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean smsNotifications = sharedPreferences.getBoolean(SettingsFragment.PREFERENCE_SMS, false);
        if (smsNotifications) {
            //TODO: IMPLEMENT SMS NOTIFICATIONS
        }

        setTitle("Settings");

        super.onCreate(savedInstanceState);

        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
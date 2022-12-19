package com.cs360.inventorysystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 10;

    public static String PREFERENCE_SMS = "pref_sms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Settings");



        super.onCreate(savedInstanceState);

        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
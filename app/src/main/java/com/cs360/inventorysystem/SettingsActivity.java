package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    private boolean mSms;
    private String mPhone;
    private boolean mInApp;

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
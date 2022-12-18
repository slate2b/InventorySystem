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


//
//        // Retrieve sms preferences
//        boolean smsNotifications = sharedPreferences.getBoolean(SettingsFragment.PREFERENCE_SMS,
//                false);
//        // If sms preference is set to true
//        if (smsNotifications) {
//            // If permission is already granted
//            if (ContextCompat.checkSelfPermission(
//                    this, Manifest.permission.SEND_SMS) ==
//                    PackageManager.PERMISSION_GRANTED) {
//                // Leave preference set to true and recreate the activity
//                getActivity().recreate();
//            }
//            else {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                getActivity().getRequestPermissionLauncher().launch(
//                        Manifest.permission.SEND_SMS);
//            }
//        }

        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

//    private ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//
//                } else {
//                    // Explain to the user that the feature is unavailable because the
//                    // feature requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            });



}
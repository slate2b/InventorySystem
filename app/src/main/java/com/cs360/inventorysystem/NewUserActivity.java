package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewUserActivity extends AppCompatActivity {

    private EditText mTextUsername;
    private EditText mTextPassword;
    private EditText mTextReenterPassword;
    private TextView mTextInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mTextUsername = findViewById(R.id.username);
        mTextPassword = findViewById(R.id.password);
        mTextReenterPassword = findViewById(R.id.reenterPassword);
        mTextInstructions = findViewById(R.id.instructionsText);

        Button mButtonCreate = findViewById(R.id.createButton);
        mButtonCreate.setOnClickListener(listener -> handleNewUser());

        Button mButtonCancel = findViewById(R.id.cancelButton);
        mButtonCancel.setOnClickListener(listener -> handleCancel());
    }

    /**
     * Handles user input when user clicks the create user button
     */
    private void handleNewUser() {
        String username = mTextUsername.getText().toString();
        String password = mTextPassword.getText().toString();
        String reenterPassword = mTextReenterPassword.getText().toString();

        if (username.isEmpty()) {
            mTextInstructions.setText(R.string.enter_username);
        }
        else if (password.isEmpty()) {
            mTextInstructions.setText(R.string.valid_password);
        }
        else if (reenterPassword.isEmpty()) {
            mTextInstructions.setText(R.string.re_enter_password);
        }
        else if (!password.equals(reenterPassword)) {
            mTextInstructions.setText(R.string.passwords_do_not_match);

            // Clear password EditText views to allow user to easily try again
            mTextPassword.setText("");
            mTextReenterPassword.setText("");
        }
        // Calling the createLogin method. If successful, then start InventoryActivity
        else if (InventoryDatabase.getInstance(getApplicationContext()).createLogin(username, password)) {
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
        }
        else {
            mTextInstructions.setText(R.string.unable_register_user);
        }
    }

    /**
     * Handles app flow when user clicks the cancel button
     */
    private void handleCancel() {
        // Start the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
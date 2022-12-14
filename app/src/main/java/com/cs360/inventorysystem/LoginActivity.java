package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.cs360.inventorysystem.username";

    private EditText mTextUsername;
    private EditText mTextPassword;
    private TextView mTextAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextUsername = findViewById(R.id.username);
        mTextPassword = findViewById(R.id.password);
        mTextAlert = findViewById(R.id.loginAlert);

        Button mButtonLogin = findViewById(R.id.loginButton);
        mButtonLogin.setOnClickListener(listener -> handleLogin());
    }

    /**
     * Handles login when user clicks the login button
     */
    private void handleLogin() {
        String username = mTextUsername.getText().toString();
        String password = mTextPassword.getText().toString();

        if (username.isEmpty()) {
            mTextAlert.setText(R.string.valid_username);
        }
        else if (password.isEmpty()) {
            mTextAlert.setText(R.string.valid_password);
        }
        else if (InventoryDatabase.getInstance(getApplicationContext()).existingUsername(username, password)) {
            if (InventoryDatabase.getInstance(getApplicationContext()).authenticateLogin(username, password)) {
                Intent intent = new Intent(this, InventoryActivity.class);
                intent.putExtra(EXTRA_USERNAME, username);
                startActivity(intent);
            }
            else {
                mTextAlert.setText(R.string.invalid_login);
            }
        }
        else if (!InventoryDatabase.getInstance(getApplicationContext()).existingUsername(username, password)) {
            Intent intent = new Intent(this, NewUserActivity.class);
            startActivity(intent);
        }
        else {
            mTextAlert.setText("Something went wrong. Please try again.");
        }
    }
}
package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText Enter_username_log, Enter_password_log;
    Button login_button;
    DatabaseHelper db;

    // The password must be at least 6 characters
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        Enter_username_log = findViewById(R.id.Enter_username_log);
        Enter_password_log = findViewById(R.id.Enter_password_log);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Enter_username_log.getText().toString().trim();
                String password = Enter_password_log.getText().toString().trim();

                // Checking for empty fields
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Both username and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Checking password length
                if (password.length() < MIN_PASSWORD_LENGTH) {
                    Toast.makeText(LoginActivity.this, "Password must be at least " + MIN_PASSWORD_LENGTH + " characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticating users
                try {
                    boolean isAuthenticated = db.authenticateUser(username, password);
                    if (isAuthenticated) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // This is to help handle any SQLite errors
                    Toast.makeText(LoginActivity.this, "An error occurred during login. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}

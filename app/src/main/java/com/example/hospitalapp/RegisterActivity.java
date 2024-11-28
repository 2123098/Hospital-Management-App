package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText Enter_name_reg, password_Enter_reg, co_password_reg;
    Button register_Button;
    DatabaseHelper db;

    // The password must be at least 6 characters long
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        Enter_name_reg = findViewById(R.id.Enter_name_reg);
        password_Enter_reg = findViewById(R.id.password_Enter_reg);
        co_password_reg = findViewById(R.id.co_password_reg);
        register_Button = findViewById(R.id.register_Button);

        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Enter_name_reg.getText().toString().trim();
                String password = password_Enter_reg.getText().toString().trim();
                String confirmPassword = co_password_reg.getText().toString().trim();

                // Checking for empty fields
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Checking password length
                if (password.length() < MIN_PASSWORD_LENGTH) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Checking if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Checking if the username already exists
                    if (db.isUserExist(username)) {
                        Toast.makeText(RegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Inserting users into the database
                    boolean insert = db.insertUser(username, password);
                    if (insert) {
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Handle any SQLite errors during registration
                    Toast.makeText(RegisterActivity.this, "An error occurred during registration. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}

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



    EditText Enter_username_log, Enter_password_log, display_login_register_result;
    Button login_button;
    DatabaseHelper db;



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
                String username = Enter_username_log.getText().toString();
                String password = Enter_password_log.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    // Use authenticateUser to check if the credentials are correct
                    boolean isAuthenticated = db.authenticateUser(username, password);
                    if (isAuthenticated) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();  // Optional: close the login activity
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}
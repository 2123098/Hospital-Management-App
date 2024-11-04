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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        db = new DatabaseHelper(this);
        Enter_name_reg = findViewById(R.id.Enter_name_reg);
        password_Enter_reg = findViewById(R.id.password_Enter_reg);
        co_password_reg = findViewById(R.id.co_password_reg);
        register_Button = findViewById(R.id.register_Button);



        //Store Registered User to the Database
        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Enter_name_reg.getText().toString();
                String password = password_Enter_reg.getText().toString();
                String confirmPassword = co_password_reg.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    boolean insert = db.insertUser(username, password);
                    if (insert) {
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
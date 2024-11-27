package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


public class DashboardActivity extends AppCompatActivity {


    Button btn_Manage_Doctors, btn_Manage_Patients, btn_schedule_appointment, btn_view_appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        // Open Doctor Management
        btn_Manage_Doctors = findViewById(R.id.btn_Manage_Doctors);
        btn_Manage_Doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DoctorActivity.class);
                startActivity(intent);
            }
        });

        // Open Patient Management
        btn_Manage_Patients = findViewById(R.id.btn_Manage_Patients);
        btn_Manage_Patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PatientActivity.class);
                startActivity(intent);
            }
        });


        // Open Appointment Management
        btn_schedule_appointment = findViewById(R.id.btn_schedule_appointment);
        btn_schedule_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AppointmentActivity.class);
                startActivity(intent);
            }
        });




    }
}
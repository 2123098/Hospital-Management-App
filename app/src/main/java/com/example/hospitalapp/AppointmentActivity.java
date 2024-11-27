package com.example.hospitalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {

    private EditText doctorIdInput, patientIdInput, dateInput, timeInput;
    private Button scheduleButton, deleteAppointmentButton;
    private ListView appointmentListView;
    private DatabaseHelper db;
    private ArrayList<String> appointmentList;
    private ArrayAdapter<String> appointmentAdapter;
    private int selectedAppointmentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        db = new DatabaseHelper(this);

        // Initialize UI
        doctorIdInput = findViewById(R.id.doctorIdInput);
        patientIdInput = findViewById(R.id.patientIdInput);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        scheduleButton = findViewById(R.id.scheduleButton);
        deleteAppointmentButton = findViewById(R.id.deleteAppointmentButton);
        appointmentListView = findViewById(R.id.appointmentListView);

        // Appointment list setup
        appointmentList = new ArrayList<>();
        appointmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
        appointmentListView.setAdapter(appointmentAdapter);

        loadAppointments();

        // Schedule appointment
        scheduleButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String doctorName = doctorIdInput.getText().toString().trim();
                String patientName = patientIdInput.getText().toString().trim();
                String date = dateInput.getText().toString().trim();
                String time = timeInput.getText().toString().trim();

                int doctorId = db.getDoctorIdByName(doctorName);
                int patientId = db.getPatientIdByName(patientName);

                if (doctorId == -1 || patientId == -1) {
                    Toast.makeText(this, "Invalid Doctor or Patient name", Toast.LENGTH_SHORT).show();
                    return;
                }

                long result = db.addAppointment(doctorId, patientId, date, time);
                if (result != -1) {
                    Toast.makeText(this, "Appointment scheduled successfully", Toast.LENGTH_SHORT).show();
                    loadAppointments();
                    clearInputs();
                } else {
                    Toast.makeText(this, "Scheduling conflict! Please choose a different time.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Delete appointment
        deleteAppointmentButton.setOnClickListener(v -> {
            if (selectedAppointmentId != -1) {
                db.deleteAppointment(selectedAppointmentId);
                Toast.makeText(this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show();
                loadAppointments();
                selectedAppointmentId = -1;
            } else {
                Toast.makeText(this, "Select an appointment to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle appointment selection
        appointmentListView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = db.getAppointments();
            if (cursor.moveToPosition(position)) {
                selectedAppointmentId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_APPOINTMENT_ID));
                String doctorName = cursor.getString(cursor.getColumnIndex("doctor_name"));
                String patientName = cursor.getString(cursor.getColumnIndex("patient_name"));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));

                doctorIdInput.setText(doctorName);
                patientIdInput.setText(patientName);
                dateInput.setText(date);
                timeInput.setText(time);
            }
            cursor.close();
        });

        // Date input listener
        dateInput.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Show DatePickerDialog
            new DatePickerDialog(AppointmentActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Format the selected date and set it to the EditText
                        String formattedDate = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year1);
                        dateInput.setText(formattedDate);
                    }, year, month, day).show();
        });

        // Time input listener
        timeInput.setOnClickListener(v -> {
            // Get the current time
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Show TimePickerDialog
            new TimePickerDialog(AppointmentActivity.this,
                    (view, hourOfDay, minute1) -> {
                        // Format the selected time and set it to the EditText
                        String formattedTime = String.format("%02d:%02d", hourOfDay, minute1);
                        timeInput.setText(formattedTime);
                    }, hour, minute, true).show();
        });


    }

    private boolean validateInputs() {
        return !TextUtils.isEmpty(doctorIdInput.getText()) &&
                !TextUtils.isEmpty(patientIdInput.getText()) &&
                !TextUtils.isEmpty(dateInput.getText()) &&
                !TextUtils.isEmpty(timeInput.getText());
    }

    private void loadAppointments() {
        appointmentList.clear();
        Cursor cursor = db.getAppointments();
        if (cursor.moveToFirst()) {
            do {
                String doctorName = cursor.getString(cursor.getColumnIndex("doctor_name"));
                String patientName = cursor.getString(cursor.getColumnIndex("patient_name"));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));

                appointmentList.add("Doctor: " + doctorName + ", Patient: " + patientName + ", Date: " + date + ", Time: " + time);
            } while (cursor.moveToNext());
        }
        cursor.close();
        appointmentAdapter.notifyDataSetChanged();
    }

    private void clearInputs() {
        doctorIdInput.setText("");
        patientIdInput.setText("");
        dateInput.setText("");
        timeInput.setText("");
    }
}

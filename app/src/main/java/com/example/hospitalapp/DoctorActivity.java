package com.example.hospitalapp;

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

public class DoctorActivity extends AppCompatActivity {

    private EditText enter_doctor_name, enter_spec, enter_phone_number;
    private Button add_doctor_btn, update_doctor_btn, delete_doctor_btn;
    private DatabaseHelper db;
    private int selectedDoctorId = -1;
    private ListView doctor_list_view;
    private ArrayList<String> doctorList;
    private ArrayAdapter<String> doctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        db = new DatabaseHelper(this);

        // Initialise UI
        doctor_list_view = findViewById(R.id.doctor_list_view);
        enter_doctor_name = findViewById(R.id.enter_doctor_name);
        enter_spec = findViewById(R.id.enter_spec);
        enter_phone_number = findViewById(R.id.enter_phone_number);
        add_doctor_btn = findViewById(R.id.add_doctor_btn);
        update_doctor_btn = findViewById(R.id.update_doctor_btn);
        delete_doctor_btn = findViewById(R.id.delete_doctor_btn);

        // Initialise the doctor list and adapter
        doctorList = new ArrayList<>();
        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorList);
        doctor_list_view.setAdapter(doctorAdapter);

        // Loading existing doctors into the list
        loadDoctors();

        // Add Doctor
        add_doctor_btn.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = enter_doctor_name.getText().toString().trim();
                String specialisation = enter_spec.getText().toString().trim();
                String phone = enter_phone_number.getText().toString().trim();

                db.addDoctor(name, specialisation, phone);
                Toast.makeText(this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadDoctors();
            }
        });

        // Update Doctor
        update_doctor_btn.setOnClickListener(v -> {
            if (selectedDoctorId != -1) {
                if (validateInputs()) {
                    String name = enter_doctor_name.getText().toString().trim();
                    String specialisation = enter_spec.getText().toString().trim();
                    String phone = enter_phone_number.getText().toString().trim();

                    db.updateDoctor(selectedDoctorId, name, specialisation, phone);
                    Toast.makeText(this, "Doctor updated successfully", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadDoctors();
                    selectedDoctorId = -1;
                }
            } else {
                Toast.makeText(this, "Please select a doctor to update", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete Doctor
        delete_doctor_btn.setOnClickListener(v -> {
            if (selectedDoctorId != -1) {
                db.deleteDoctor(selectedDoctorId);
                Toast.makeText(this, "Doctor deleted successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadDoctors();
                selectedDoctorId = -1;
            } else {
                Toast.makeText(this, "Please select a doctor to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle ListView item click
        doctor_list_view.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = db.getAllDoctors();
            if (cursor.moveToPosition(position)) {
                selectedDoctorId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOCTOR_ID));
                enter_doctor_name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOCTOR_NAME)));
                enter_spec.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIALISATION)));
                enter_phone_number.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE)));
            }
            cursor.close();
        });
    }

    // Validating user inputs
    private boolean validateInputs() {
        String name = enter_doctor_name.getText().toString().trim();
        String specialisation = enter_spec.getText().toString().trim();
        String phone = enter_phone_number.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Doctor Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(specialisation)) {
            Toast.makeText(this, "Specialisation is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phone) || !phone.matches("\\d{10}")) {
            Toast.makeText(this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Loading doctors from the database
    private void loadDoctors() {
        doctorList.clear();
        Cursor cursor = db.getAllDoctors();
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DOCTOR_NAME);
            int specializationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIALISATION);
            int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);

            do {
                String doctorName = cursor.getString(nameIndex);
                String specialization = cursor.getString(specializationIndex);
                String phone = cursor.getString(phoneIndex);
                doctorList.add(doctorName + " - " + specialization + " - " + phone);
            } while (cursor.moveToNext());
        }
        cursor.close();
        doctorAdapter.notifyDataSetChanged();
    }

    // Clear input fields
    private void clearInputs() {
        enter_doctor_name.setText("");
        enter_spec.setText("");
        enter_phone_number.setText("");
    }
}

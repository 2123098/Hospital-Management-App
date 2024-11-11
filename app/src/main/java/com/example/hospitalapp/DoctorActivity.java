package com.example.hospitalapp;

import android.database.Cursor;
import android.os.Bundle;
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

        doctor_list_view = findViewById(R.id.doctor_list_view);
        enter_doctor_name = findViewById(R.id.enter_doctor_name);
        enter_spec = findViewById(R.id.enter_spec);
        enter_phone_number = findViewById(R.id.enter_phone_number);
        add_doctor_btn = findViewById(R.id.add_doctor_btn);
        update_doctor_btn = findViewById(R.id.update_doctor_btn);
        delete_doctor_btn = findViewById(R.id.delete_doctor_btn);

        doctorList = new ArrayList<>();  // Initialize doctorList

        // Set the adapter for the ListView
        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorList);
        doctor_list_view.setAdapter(doctorAdapter);

        loadDoctors();  // Load the doctors list into the ListView

        // Add Doctor
        add_doctor_btn.setOnClickListener(v -> {
            String name = enter_doctor_name.getText().toString();
            String specialisation = enter_spec.getText().toString();
            String phone = enter_phone_number.getText().toString();

            if (!name.isEmpty() && !specialisation.isEmpty() && !phone.isEmpty()) {
                db.addDoctor(name, specialisation, phone);
                Toast.makeText(this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadDoctors();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Update Doctor
        update_doctor_btn.setOnClickListener(v -> {
            if (selectedDoctorId != -1) {
                String name = enter_doctor_name.getText().toString();
                String specialisation = enter_spec.getText().toString();
                String phone = enter_phone_number.getText().toString();

                db.updateDoctor(selectedDoctorId, name, specialisation, phone);
                Toast.makeText(this, "Doctor updated successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadDoctors();
                selectedDoctorId = -1;
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
                loadDoctors(); // Refresh the doctor list
                selectedDoctorId = -1; // Reset after delete
            } else {
                Toast.makeText(this, "Please select a doctor to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle ListView item click for selecting a doctor to update or delete
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

    // Loading doctors into the ListView
    private void loadDoctors() {
        doctorList.clear(); // Clear old data before loading new
        Cursor cursor = db.getAllDoctors();
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DOCTOR_NAME);
            int specializationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIALISATION);
            int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);

            do {
                String doctorName = cursor.getString(nameIndex);
                String specialization = cursor.getString(specializationIndex);
                String phone = cursor.getString(phoneIndex);

                // Add doctor data to list (format: DoctorName - Specialization - Phone)
                doctorList.add(doctorName + " - " + specialization + " - " + phone);

            } while (cursor.moveToNext());
        }
        cursor.close();

        // Notify adapter of data change
        doctorAdapter.notifyDataSetChanged();
    }

    // Clear input fields after adding, updating, or deleting a doctor
    private void clearInputs() {
        enter_doctor_name.setText("");
        enter_spec.setText("");
        enter_phone_number.setText("");
    }
}

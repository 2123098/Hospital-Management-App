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

public class PatientActivity extends AppCompatActivity {

    private EditText enter_patient_name, enter_diagnose, enter_number;
    private Button add_patient_btn, update_patient_btn, delete_patient_btn;
    private DatabaseHelper db;
    private int selectedPatientId = -1;
    private ListView patient_list_view;
    private ArrayList<String> patientList;
    private ArrayAdapter<String> patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        db = new DatabaseHelper(this);

        patient_list_view = findViewById(R.id.patient_list_view);
        enter_patient_name = findViewById(R.id.enter_patient_name);
        enter_diagnose = findViewById(R.id.enter_diagnose);
        enter_number = findViewById(R.id.enter_number);
        add_patient_btn = findViewById(R.id.add_patient_btn);
        update_patient_btn = findViewById(R.id.update_patient_btn);
        delete_patient_btn = findViewById(R.id.delete_patient_btn);

        patientList = new ArrayList<>();

        // Set the adapter for the ListView
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patientList);
        patient_list_view.setAdapter(patientAdapter);

        loadPatients();

        // Add Patient
        add_patient_btn.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = enter_patient_name.getText().toString().trim();
                String diagnose = enter_diagnose.getText().toString().trim();
                String number = enter_number.getText().toString().trim();

                db.addPatient(name, diagnose, number);
                Toast.makeText(this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadPatients();
            }
        });

        // Update Patient
        update_patient_btn.setOnClickListener(v -> {
            if (selectedPatientId != -1 && validateInputs()) {
                String name = enter_patient_name.getText().toString().trim();
                String diagnose = enter_diagnose.getText().toString().trim();
                String number = enter_number.getText().toString().trim();

                db.updatePatient(selectedPatientId, name, diagnose, number);
                Toast.makeText(this, "Patient updated successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadPatients();
                selectedPatientId = -1;
            } else {
                Toast.makeText(this, "Please select a patient to update", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete Patient
        delete_patient_btn.setOnClickListener(v -> {
            if (selectedPatientId != -1) {
                db.deletePatient(selectedPatientId);
                Toast.makeText(this, "Patient deleted successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadPatients();
                selectedPatientId = -1;
            } else {
                Toast.makeText(this, "Please select a patient to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle ListView item click for selecting a patient
        patient_list_view.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = db.getAllPatients();
            if (cursor.moveToPosition(position)) {
                selectedPatientId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATIENT_ID));
                enter_patient_name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATIENT_NAME)));
                enter_diagnose.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DIAGNOSE)));
                enter_number.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER)));
            }
            cursor.close();
        });
    }

    // Load patients into the ListView
    private void loadPatients() {
        patientList.clear();
        Cursor cursor = db.getAllPatients();
        if (cursor.moveToFirst()) {
            do {
                String patientName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATIENT_NAME));
                String diagnose = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DIAGNOSE));
                String number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));

                // Adding patients details to list
                patientList.add(patientName + " - " + diagnose + " - " + number);

            } while (cursor.moveToNext());
        }
        cursor.close();

        // Notify adapter of data changes
        patientAdapter.notifyDataSetChanged();
    }

    // Validate user inputs
    private boolean validateInputs() {
        String name = enter_patient_name.getText().toString().trim();
        String diagnose = enter_diagnose.getText().toString().trim();
        String number = enter_number.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            enter_patient_name.setError("Patient name is required");
            return false;
        }
        if (TextUtils.isEmpty(diagnose)) {
            enter_diagnose.setError("Diagnose is required");
            return false;
        }
        if (TextUtils.isEmpty(number) || !number.matches("\\d{10}")) {
            enter_number.setError("Valid 10-digit phone number is required");
            return false;
        }
        return true;
    }

    // Clear input fields
    private void clearInputs() {
        enter_patient_name.setText("");
        enter_diagnose.setText("");
        enter_number.setText("");
    }
}

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

        // The UI components
        enter_patient_name = findViewById(R.id.enter_patient_name);
        enter_diagnose = findViewById(R.id.enter_diagnose);
        enter_number = findViewById(R.id.enter_number);
        add_patient_btn = findViewById(R.id.add_patient_btn);
        update_patient_btn = findViewById(R.id.update_patient_btn);
        delete_patient_btn = findViewById(R.id.delete_patient_btn);
        patient_list_view = findViewById(R.id.patient_list_view);

        // Setup ListView and Adapter
        patientList = new ArrayList<>();
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patientList);
        patient_list_view.setAdapter(patientAdapter);

        loadPatients();

        // These are the the button listeners
        add_patient_btn.setOnClickListener(v -> handlePatientAction("add"));
        update_patient_btn.setOnClickListener(v -> handlePatientAction("update"));
        delete_patient_btn.setOnClickListener(v -> handlePatientAction("delete"));

        patient_list_view.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = null;
            try {
                cursor = db.getAllPatients();
                if (cursor != null && cursor.moveToPosition(position)) {
                    selectedPatientId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATIENT_ID));
                    enter_patient_name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATIENT_NAME)));
                    enter_diagnose.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DIAGNOSE)));
                    enter_number.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER)));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }

    private void loadPatients() {
        patientList.clear();
        Cursor cursor = null;
        try {
            cursor = db.getAllPatients();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String patientDetails = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATIENT_NAME)) +
                            " - " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DIAGNOSE)) +
                            " - " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
                    patientList.add(patientDetails);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        patientAdapter.notifyDataSetChanged();

        // Displaying a message if no patients exist
        if (patientList.isEmpty()) {
            Toast.makeText(this, "No patients found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        String name = enter_patient_name.getText().toString().trim();
        String diagnose = enter_diagnose.getText().toString().trim();
        String number = enter_number.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            enter_patient_name.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(diagnose)) {
            enter_diagnose.setError("Diagnosis is required");
            return false;
        }
        if (TextUtils.isEmpty(number) || !number.matches("\\d{10}")) {
            enter_number.setError("Enter a valid 10-digit phone number");
            return false;
        }
        return true;
    }

    // Handling patient action
    private void handlePatientAction(String action) {
        if (action.equals("add")) {
            if (validateInputs()) {
                String name = enter_patient_name.getText().toString().trim();
                String diagnose = enter_diagnose.getText().toString().trim();
                String number = enter_number.getText().toString().trim();

                boolean isAdded = db.addPatient(name, diagnose, number);

                if (isAdded) {
                    Toast.makeText(this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadPatients();
                } else {
                    Toast.makeText(this, "Patient already exists", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (action.equals("update")) {
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
                Toast.makeText(this, "Select a patient to update", Toast.LENGTH_SHORT).show();
            }
        } else if (action.equals("delete")) {
            if (selectedPatientId != -1) {
                db.deletePatient(selectedPatientId);
                Toast.makeText(this, "Patient deleted successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
                loadPatients();
                selectedPatientId = -1;
            } else {
                Toast.makeText(this, "Select a patient to delete", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void clearInputs() {
        enter_patient_name.setText("");
        enter_diagnose.setText("");
        enter_number.setText("");
    }
}

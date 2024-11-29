package com.example.hospitalapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewAppointmentsActivity extends AppCompatActivity {

    ListView appointmentsListView;
    // List to hold appointment data
    ArrayList<String> appointmentsList;
    // Custom adapter to display data
    AppointmentsAdapter appointmentsAdapter;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        // The ListView
        appointmentsListView = findViewById(R.id.appointments_list_view);
        appointmentsList = new ArrayList<>();

        // Database helper
        dbHelper = new DatabaseHelper(this);

        // Here, fetching appointments data from the database
        loadAppointments();

        // Setting up the adapter to display appointments
        appointmentsAdapter = new AppointmentsAdapter(this, appointmentsList);
        appointmentsListView.setAdapter(appointmentsAdapter);
    }

    private void loadAppointments() {
        Cursor cursor = dbHelper.getAppointments();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extracting appointment details from cursor
                String doctorName = cursor.getString(cursor.getColumnIndex("doctor_name"));
                String patientName = cursor.getString(cursor.getColumnIndex("patient_name"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));

                // Adding appointment details to the list
                String appointmentDetails = "Dr. " + doctorName + " - " + patientName + " - " + date + " at " + time;
                appointmentsList.add(appointmentDetails);
            }
            cursor.close();
        }
    }
}

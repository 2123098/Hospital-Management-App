package com.example.hospitalapp;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.Cursor;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DatabaseHelperTest {

    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        // This is the database helper
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
    }

    @After
    public void tearDown() {
        // Clean up the database after each test
        dbHelper.close();
    }

    @Test
    public void testAddPatientAndVerifyInDatabase() {
        // Launching the patient activity
        try (ActivityScenario<PatientActivity> scenario = ActivityScenario.launch(PatientActivity.class)) {

            // Input test data
            String patientName = "Paul";
            String diagnosis = "Flu";
            String phoneNumber = "9870043210";

            // Entering text into the patient name field
            onView(withId(R.id.enter_patient_name))
                    .perform(typeText(patientName), closeSoftKeyboard());

            // Entering diagnosis
            onView(withId(R.id.enter_diagnose))
                    .perform(typeText(diagnosis), closeSoftKeyboard());

            // Entering phone number
            onView(withId(R.id.enter_number))
                    .perform(typeText(phoneNumber), closeSoftKeyboard());

            // Clicking the add patient button
            onView(withId(R.id.add_patient_btn)).perform(click());

            // Update the ListView
            Espresso.onIdle();

            // Verifying if new patient appears in the ListView
            onData(Matchers.anything())
                    .inAdapterView(withId(R.id.patient_list_view))
                    .atPosition(0)
                    .check(matches(isDisplayed()));

            // Verifying patient is added in the database
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                    "SELECT * FROM " + DatabaseHelper.TABLE_PATIENTS + " WHERE name = ?",
                    new String[]{patientName}
            );
            assertTrue(cursor.getCount() > 0);
            cursor.close();
        }
    }

    @Test
    public void testAddDoctorAndVerifyInDatabase() {
        // Launching the doctor activity
        try (ActivityScenario<DoctorActivity> scenario = ActivityScenario.launch(DoctorActivity.class)) {

            // Input test data for doctor
            String doctorName = "Dr. Siaw";
            String specialisation = "Dentist";
            String phoneNumber = "1234567890";

            // Entering text into the doctor name field
            onView(withId(R.id.enter_doctor_name))
                    .perform(typeText(doctorName), closeSoftKeyboard());

            // Entering specialisation
            onView(withId(R.id.enter_spec))
                    .perform(typeText(specialisation), closeSoftKeyboard());

            // Entering phone number
            onView(withId(R.id.enter_phone_number))
                    .perform(typeText(phoneNumber), closeSoftKeyboard());

            // Clicking the add doctor button
            onView(withId(R.id.add_doctor_btn)).perform(click());

            // Update the ListView
            Espresso.onIdle();

            // Verifying if new doctor appears in the ListView
            onData(anything())
                    .inAdapterView(withId(R.id.doctor_list_view))
                    .atPosition(0)
                    .check(matches(isDisplayed()));

            // Verifying doctor is added in the database
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                    "SELECT * FROM " + DatabaseHelper.TABLE_DOCTORS + " WHERE name = ?",
                    new String[]{doctorName}
            );

            // Checking that the doctor exists in the database and verify details
            assertTrue(cursor.getCount() > 0);
            if (cursor.moveToFirst()) {
                assertEquals(doctorName, cursor.getString(cursor.getColumnIndex("name")));
                assertEquals(specialisation, cursor.getString(cursor.getColumnIndex("specialisation")));
                assertEquals(phoneNumber, cursor.getString(cursor.getColumnIndex("phone")));
            }
            cursor.close();
        }
    }
}

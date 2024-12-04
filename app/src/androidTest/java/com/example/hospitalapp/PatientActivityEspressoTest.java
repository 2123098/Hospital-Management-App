package com.example.hospitalapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PatientActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<PatientActivity> activityRule =
            new ActivityScenarioRule<>(PatientActivity.class);

    @Test
    public void testAddPatientButton() {
        // Entering text into the patient name field
        onView(withId(R.id.enter_patient_name))
                .perform(typeText("Siaw"), closeSoftKeyboard());

        // Same here, filling the diagnosis field
        onView(withId(R.id.enter_diagnose))
                .perform(typeText("Flu"), closeSoftKeyboard());

        // Here too entering a valid phone number
        onView(withId(R.id.enter_number))
                .perform(typeText("1234567890"), closeSoftKeyboard());

        // Clicking the add patient button
        onView(withId(R.id.add_patient_btn)).perform(click());

        // Verifying if new patient appears in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.patient_list_view))
                .atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void testUpdatePatientButton() {
        // Add patient
        testAddPatientButton();

        // Selecting the patient in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.patient_list_view))
                .atPosition(0)
                .perform(click());

        // Updating the patient's details
        onView(withId(R.id.enter_patient_name))
                .perform(typeText("Siaw Paul"), closeSoftKeyboard());
        onView(withId(R.id.enter_diagnose))
                .perform(typeText("Cold"), closeSoftKeyboard());
        onView(withId(R.id.enter_number))
                .perform(typeText("9876543210"), closeSoftKeyboard());

        // Click update patient button
        onView(withId(R.id.update_patient_btn)).perform(click());

        // Verifying that the updated patient details appear in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.patient_list_view))
                .atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDeletePatientButton() {
        // Add patient
        testAddPatientButton();

        // Selecting the patient in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.patient_list_view))
                .atPosition(0)
                .perform(click());

        // This is delete patient button
        onView(withId(R.id.delete_patient_btn)).perform(click());

        // Verifying that the ListView no longer displays the deleted patient
        onView(withId(R.id.patient_list_view))
                .check(matches(ViewMatchers.hasChildCount(0)));
    }


    @Test
    public void testEmptyPatientName() {
        // Leaving patient name empty
        onView(withId(R.id.enter_patient_name))
                .perform(typeText(""), closeSoftKeyboard());

        // Filling valid diagnosis and phone number
        onView(withId(R.id.enter_diagnose))
                .perform(typeText("Flu"), closeSoftKeyboard());
        onView(withId(R.id.enter_number))
                .perform(typeText("1234567890"), closeSoftKeyboard());

        // Trying clicking Add Patient button
        onView(withId(R.id.add_patient_btn)).perform(click());

        // when the patient is NOT added
        onView(withId(R.id.patient_list_view))
                .check(matches(ViewMatchers.hasChildCount(0)));
    }

    @Test
    public void testInvalidPhoneNumberFormat() {
        // Filling valid name and diagnosis
        onView(withId(R.id.enter_patient_name))
                .perform(typeText("Ronald"), closeSoftKeyboard());
        onView(withId(R.id.enter_diagnose))
                .perform(typeText("Flu"), closeSoftKeyboard());

        // Checking when entered invalid phone number
        onView(withId(R.id.enter_number))
                .perform(typeText("abc123"), closeSoftKeyboard());

        // Add Patient button
        onView(withId(R.id.add_patient_btn)).perform(click());

        // When the patient is NOT added
        onView(withId(R.id.patient_list_view))
                .check(matches(ViewMatchers.hasChildCount(0)));
    }

}

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
public class DoctorActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<DoctorActivity> activityRule =
            new ActivityScenarioRule<>(DoctorActivity.class);

    @Test
    public void testAddDoctorButton() {

        // When enter text into the doctor name field
        onView(withId(R.id.enter_doctor_name))
                .perform(typeText("Dr. Paul"), closeSoftKeyboard());

        // Same here, when enter text into the specialisation field
        onView(withId(R.id.enter_spec))
                .perform(typeText("Cardiologist"), closeSoftKeyboard());

        // Then again, when enter text into the phone number field
        onView(withId(R.id.enter_phone_number))
                .perform(typeText("1234567890"), closeSoftKeyboard());


        // Click the add doctor button
        onView(withId(R.id.add_doctor_btn)).perform(click());


        // Verifying that a new entry is added to the ListView
        onData(anything())
                .inAdapterView(withId(R.id.doctor_list_view))
                .atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void testUpdateDoctorButton() {

        // Add doctor
        testAddDoctorButton();

        // Selecting doctor in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.doctor_list_view))
                .atPosition(0)
                .perform(click());

        // Updating the doctor's details
        onView(withId(R.id.enter_doctor_name))
                .perform(typeText("Updated Dr. Paul"), closeSoftKeyboard());
        onView(withId(R.id.enter_spec))
                .perform(typeText("Updated Cardiologist"), closeSoftKeyboard());
        onView(withId(R.id.enter_phone_number))
                .perform(typeText("9876543210"), closeSoftKeyboard());


        // Click the update doctor button
        onView(withId(R.id.update_doctor_btn)).perform(click());


        // Verifying that the updated entry is displayed in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.doctor_list_view))
                .atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteDoctorButton() {

        testAddDoctorButton();


        // Selecting doctor in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.doctor_list_view))
                .atPosition(0)
                .perform(click());

        // Click the delete doctor button
        onView(withId(R.id.delete_doctor_btn)).perform(click());


        // Verifying that the ListView no longer displays the deleted doctor
        onView(withId(R.id.doctor_list_view))
                .check(matches(ViewMatchers.hasChildCount(0)));
    }
}

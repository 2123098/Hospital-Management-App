package com.example.hospitalapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.CoreMatchers.is;


import android.view.View;

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
        addDoctor("Dr. Paul", "Cardiologist", "1234567890");

        // Verifying that a new entry is added to the ListView
        onData(anything())
                .inAdapterView(withId(R.id.doctor_list_view))
                .atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void testUpdateDoctorButton() {
        testAddDoctorButton();

        // Selecting doctor in the ListView
        onData(anything())
                .inAdapterView(withId(R.id.doctor_list_view))
                .atPosition(0)
                .perform(click());

        // Updating the doctor's details
        addDoctor("Dr. Paul Paul", "Dentist", "9876543210");

        // Update doctor button
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


    @Test
    public void testBoundaryPhoneNumberLength() {
        //Trying with 9 digits
        addDoctor("Dr. Paul", "Cardiologist", "123456789");

        // Displaying phone number error
        onView(withText("Valid 10-digit phone number is required"))
                .inRoot(withDecorView(not(is(getActivityDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidPhoneNumberFormat() {
        // Trying with invalid phone number including text
        addDoctor("Dr. Paul", "Cardiologist", "123ABC7890");

        // Displaying phone number error
        onView(withText("Valid 10-digit phone number is required"))
                .inRoot(withDecorView(not(is(getActivityDecorView()))))
                .check(matches(isDisplayed()));
    }

    // Helper method to add a doctor
    private void addDoctor(String name, String spec, String phone) {
        onView(withId(R.id.enter_doctor_name))
                .perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_spec))
                .perform(typeText(spec), closeSoftKeyboard());
        onView(withId(R.id.enter_phone_number))
                .perform(typeText(phone), closeSoftKeyboard());

        onView(withId(R.id.add_doctor_btn)).perform(click());
    }

    // Helper method to get the DecorView for Toast verification
    private View getActivityDecorView() {
        final View[] decorView = new View[1];
        activityRule.getScenario().onActivity(activity -> {
            decorView[0] = activity.getWindow().getDecorView();
        });
        return decorView[0];
    }
}

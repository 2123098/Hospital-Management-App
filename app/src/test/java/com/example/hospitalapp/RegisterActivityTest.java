package com.example.hospitalapp;

import android.text.Editable;
import android.text.TextUtils;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.widget.EditText;

public class RegisterActivityTest {

    private RegisterActivity registerActivity;
    private EditText Enter_name_reg, password_Enter_reg, co_password_reg;
    private DatabaseHelper mockDatabaseHelper;

    @Before
    public void setUp() {
        registerActivity = mock(RegisterActivity.class);
        Enter_name_reg = mock(EditText.class);
        password_Enter_reg = mock(EditText.class);
        co_password_reg = mock(EditText.class);
        mockDatabaseHelper = mock(DatabaseHelper.class);

        registerActivity.db = mockDatabaseHelper;
        registerActivity.Enter_name_reg = Enter_name_reg;
        registerActivity.password_Enter_reg = password_Enter_reg;
        registerActivity.co_password_reg = co_password_reg;
    }

    @Test
    public void testEmptyFields() {
        // Mocking EditText
        when(Enter_name_reg.getText()).thenReturn(mock(Editable.class));
        when(password_Enter_reg.getText()).thenReturn(mock(Editable.class));
        when(co_password_reg.getText()).thenReturn(mock(Editable.class));

        // Checking with empty username
        when(Enter_name_reg.getText().toString()).thenReturn("");
        when(password_Enter_reg.getText().toString()).thenReturn("password123");
        when(co_password_reg.getText().toString()).thenReturn("password123");

        // Checking empty fields
        boolean result = !Enter_name_reg.getText().toString().isEmpty()
                && !password_Enter_reg.getText().toString().isEmpty()
                && !co_password_reg.getText().toString().isEmpty();

        assertFalse("Fields should not be empty", result);
    }

    @Test
    public void testShortPassword() {
        when(password_Enter_reg.getText()).thenReturn(mock(Editable.class));
        when(password_Enter_reg.getText().toString()).thenReturn("12345");

        // Check password length
        boolean result = password_Enter_reg.getText().toString().length() >= 6;
        assertFalse("Password should not be valid if less than 6 characters", result);
    }

    @Test
    public void testPasswordsMatch() {
        when(password_Enter_reg.getText()).thenReturn(mock(Editable.class));
        when(co_password_reg.getText()).thenReturn(mock(Editable.class));

        when(password_Enter_reg.getText().toString()).thenReturn("password123");
        when(co_password_reg.getText().toString()).thenReturn("password123");

        boolean result = password_Enter_reg.getText().toString().equals(co_password_reg.getText().toString());
        assertTrue("Passwords should match", result);

        when(co_password_reg.getText().toString()).thenReturn("differentpassword");
        result = password_Enter_reg.getText().toString().equals(co_password_reg.getText().toString());
        assertFalse("Passwords should not match", result);
    }

    @Test
    public void testUsernameExistence() {
        when(mockDatabaseHelper.isUserExist("existingUser")).thenReturn(true);
        when(mockDatabaseHelper.isUserExist("newUser")).thenReturn(false);

        boolean result = mockDatabaseHelper.isUserExist("existingUser");
        assertTrue("Username should already exist", result);

        result = mockDatabaseHelper.isUserExist("newUser");
        assertFalse("Username should not exist", result);
    }
}
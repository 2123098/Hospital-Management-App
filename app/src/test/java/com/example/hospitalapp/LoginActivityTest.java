package com.example.hospitalapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginActivityTest {

    // Checking if empty password is invalid
    @Test
    public void testEmptyPassword() {
        String password = "";
        boolean isPasswordValid = password.length() >= 6;
        assertFalse("Password should not be valid when empty", isPasswordValid);
    }

    // Checking for password length if a short password meaning less than 6 characters is invalid
    @Test
    public void testShortPassword() {
        String password = "12345";
        boolean isPasswordValid = password.length() >= 6;
        assertFalse("Password should not be valid if less than 6 characters", isPasswordValid);
    }

    // Checking if password with 6 or more characters in valid
    @Test
    public void testValidPassword() {
        String password = "123456";
        boolean isPasswordValid = password.length() >= 6;
        assertTrue("Password should be valid if it has 6 or more characters", isPasswordValid);
    }

    @Test
    public void tearDown() {
        System.out.println("Done with testing");
    }
}

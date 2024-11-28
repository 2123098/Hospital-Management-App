package com.example.hospitalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hospital_database.db";
    private static final int DATABASE_VERSION = 4;

    // Creating User table
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Creating Doctor Table
    public static final String TABLE_DOCTORS = "doctors";
    public static final String COLUMN_DOCTOR_ID = "id";
    public static final String COLUMN_DOCTOR_NAME = "name";
    public static final String COLUMN_SPECIALISATION = "specialisation";
    public static final String COLUMN_PHONE = "phone";

    // Creating Patient Table
    public static final String TABLE_PATIENTS = "patients";
    public static final String COLUMN_PATIENT_ID = "id";
    public static final String COLUMN_PATIENT_NAME = "name";
    public static final String COLUMN_DIAGNOSE = "diagnose";
    public static final String COLUMN_NUMBER = "number";

    // Creating Appointments Table
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String COLUMN_APPOINTMENT_ID = "id";
    public static final String COLUMN_APPOINTMENT_DOCTOR_ID = "doctor_id";
    public static final String COLUMN_APPOINTMENT_PATIENT_ID = "patient_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_DOCTORS + "("
                + COLUMN_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DOCTOR_NAME + " TEXT,"
                + COLUMN_SPECIALISATION + " TEXT,"
                + COLUMN_PHONE + " TEXT" + ")";
        db.execSQL(CREATE_DOCTORS_TABLE);

        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("
                + COLUMN_PATIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PATIENT_NAME + " TEXT,"
                + COLUMN_DIAGNOSE + " TEXT,"
                + COLUMN_NUMBER + " TEXT" + ")";
        db.execSQL(CREATE_PATIENTS_TABLE);

        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_APPOINTMENTS + "("
                + COLUMN_APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_APPOINTMENT_DOCTOR_ID + " INTEGER,"
                + COLUMN_APPOINTMENT_PATIENT_ID + " INTEGER,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + "FOREIGN KEY (" + COLUMN_APPOINTMENT_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + "(" + COLUMN_DOCTOR_ID + "),"
                + "FOREIGN KEY (" + COLUMN_APPOINTMENT_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + "(" + COLUMN_PATIENT_ID + "))";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        onCreate(db);
    }

    // Doctor Management
    public void addDoctor(String name, String specialisation, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_NAME, name);
        values.put(COLUMN_SPECIALISATION, specialisation);
        values.put(COLUMN_PHONE, phone);

        try {
            db.insert(TABLE_DOCTORS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void updateDoctor(int id, String name, String specialisation, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_NAME, name);
        values.put(COLUMN_SPECIALISATION, specialisation);
        values.put(COLUMN_PHONE, phone);

        try {
            db.update(TABLE_DOCTORS, values, COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_DOCTORS, COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Patient Management
    public void addPatient(String name, String diagnose, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_NAME, name);
        values.put(COLUMN_DIAGNOSE, diagnose);
        values.put(COLUMN_NUMBER, number);

        try {
            db.insert(TABLE_PATIENTS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void updatePatient(int id, String name, String diagnose, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_NAME, name);
        values.put(COLUMN_DIAGNOSE, diagnose);
        values.put(COLUMN_NUMBER, number);

        try {
            db.update(TABLE_PATIENTS, values, COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void deletePatient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_PATIENTS, COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Appointment Management
    public long addAppointment(int doctorId, int patientId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check for scheduling conflicts
        String query = "SELECT * FROM " + TABLE_APPOINTMENTS +
                " WHERE " + COLUMN_APPOINTMENT_DOCTOR_ID + " = ? AND " +
                COLUMN_DATE + " = ? AND " + COLUMN_TIME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(doctorId), date, time});

        if (cursor.getCount() > 0) {
            cursor.close();
            return -1; // Conflict found
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_APPOINTMENT_DOCTOR_ID, doctorId);
        values.put(COLUMN_APPOINTMENT_PATIENT_ID, patientId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);

        try {
            return db.insert(TABLE_APPOINTMENTS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return -1;
    }

    public void deleteAppointment(int appointmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_APPOINTMENTS, COLUMN_APPOINTMENT_ID + " = ?", new String[]{String.valueOf(appointmentId)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Get all doctors and patients
    public Cursor getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DOCTORS, null);
    }

    public Cursor getAllPatients() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PATIENTS, null);
    }

    // Method to hash passwords using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Inserting Users with hashed password
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);

        // Hash the password before storing it
        String hashedPassword = hashPassword(password);
        contentValues.put(COLUMN_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Authenticate User with hashed password
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Hash the password before comparison
        String hashedPassword = hashPassword(password);

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, hashedPassword});

        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Helper to get doctor ID by name
    public int getDoctorIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS, new String[]{COLUMN_DOCTOR_ID},
                COLUMN_DOCTOR_NAME + " = ?", new String[]{name},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_DOCTOR_ID));
            cursor.close();
            return id;
        }
        return -1; // Not found
    }

    // Helper to get patient ID by name
    public int getPatientIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{COLUMN_PATIENT_ID},
                COLUMN_PATIENT_NAME + " = ?", new String[]{name},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_PATIENT_ID));
            cursor.close();
            return id;
        }
        return -1; // Not found
    }

    // Method to get all appointments
    public Cursor getAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a." + COLUMN_APPOINTMENT_ID + ", " +
                "d." + COLUMN_DOCTOR_NAME + " AS doctor_name, " +
                "p." + COLUMN_PATIENT_NAME + " AS patient_name, " +
                "a." + COLUMN_DATE + ", a." + COLUMN_TIME +
                " FROM " + TABLE_APPOINTMENTS + " a " +
                "JOIN " + TABLE_DOCTORS + " d ON a." + COLUMN_APPOINTMENT_DOCTOR_ID + " = d." + COLUMN_DOCTOR_ID + " " +
                "JOIN " + TABLE_PATIENTS + " p ON a." + COLUMN_APPOINTMENT_PATIENT_ID + " = p." + COLUMN_PATIENT_ID;
        return db.rawQuery(query, null);
    }

    // Check if a username already exists
    public boolean isUserExist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


}

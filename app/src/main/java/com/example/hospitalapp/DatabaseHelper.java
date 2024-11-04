package com.example.hospitalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hospital_database.db";
    private static final int DATABASE_VERSION = 3;

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        onCreate(db);
    }



    // Inserting Users
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;  // return true if data inserted successfully
    }


    // Insert doctor
    public void addDoctor(String name, String specialisation, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_NAME, name);
        values.put(COLUMN_SPECIALISATION, specialisation);
        values.put(COLUMN_PHONE, phone);

        db.insert(TABLE_DOCTORS, null, values);
        db.close();
    }


    // Update doctor
    public void updateDoctor(int id, String name, String specialisation, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_NAME, name);
        values.put(COLUMN_SPECIALISATION, specialisation);
        values.put(COLUMN_PHONE, phone);

        db.update(TABLE_DOCTORS, values, COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    // Delete doctor
    public void deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOCTORS, COLUMN_DOCTOR_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }




    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?  AND " + COLUMN_PASSWORD + "=?", new String[]{username, password});
        return cursor.getCount() > 0;
    }



    // Authenticate User
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    // Get all doctors
    public Cursor getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DOCTORS, null);
    }
}

package com.main.tapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 10;

    private static final String TABLE_JOBS = "table_jobs";
    private static final String COL_ID = "ID";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_EST_TIME = "est_time";
    private static final String COL_SALARY = "salary";
    private static final String COL_DATE = "date";
    private static final String COL_DATE_TIME = "date_time";
    private static final String COL_CREATED = "created";


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_JOBS, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_JOBS + " ("+
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_EST_TIME + " TEXT, " +
                COL_SALARY + " INTEGER, " +
                COL_DATE + " TEXT, " +
                COL_CREATED + " TEXT, " +
                COL_DATE_TIME + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        onCreate(db);
    }

    public boolean addJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, job.getTitle());
        cv.put(COL_DESCRIPTION, job.getDescription());
        cv.put(COL_EST_TIME, String.valueOf(job.getEstTime()));
        cv.put(COL_SALARY, job.getSalary());
        cv.put(COL_DATE, job.getDate());
        cv.put(COL_DATE_TIME, job.getDateTime());
        cv.put(COL_CREATED, job.getCreatedDate());

        Log.d(TAG, "addJob: Adding " + job + "to " + TABLE_JOBS);

        long result = db.insert(TABLE_JOBS, null, cv);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public Cursor getJobs() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_JOBS;
        Cursor data = db.rawQuery(query, null);

        return data;
    }
}

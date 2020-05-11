package com.main.tapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.main.tapp.metadata.Job;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 12;

    private static final String TABLE_JOBS = "table_jobs";
    private static final String COL_ID = "ID";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_EST_TIME = "est_time";
    private static final String COL_SALARY = "salary";
    private static final String COL_DATE = "date";
    private static final String COL_CREATED_DATE = "created_date";
    private static final String COL_CREATED_BY = "created_by";
    private static final String COL_CREATED_BY_NAME = "created_by_name";
    private static final String COL_DATE_TIME = "date_time";


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
                COL_CREATED_DATE + " TEXT, " +
                COL_CREATED_BY + " TEXT, " +
                COL_CREATED_BY_NAME + " TEXT, " +
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
        cv.put(COL_CREATED_DATE, job.getCreatedDate());
        cv.put(COL_CREATED_BY, job.getCreatedByUID());
        cv.put(COL_CREATED_BY_NAME, job.getCreatedByName());

        Log.d(TAG, "addJob: Adding " + job + "to " + TABLE_JOBS);

        long result = db.insert(TABLE_JOBS, null, cv);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<Job> getJobs() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_JOBS;
        Cursor data = db.rawQuery(query, null);

        return getJobList(data);
    }

    public ArrayList<Job> getJobsBy(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_JOBS + " WHERE " + COL_CREATED_BY + "='" + uid+"'";
        Cursor data = db.rawQuery(query, null);


        return getJobList(data);
    }

    private ArrayList<Job> getJobList(Cursor data) {
        ArrayList<Job> jobList = new ArrayList<>();

        while (data.moveToNext()) {
            Job job = new Job().
                    id(Integer.valueOf(data.getString(0))).
                    title(data.getString(1)).
                    description(data.getString(2)).
                    estTime(Double.valueOf(data.getString(3))).
                    salary(data.getInt(4)).
                    date(data.getString(5)).
                    createdDate(data.getString(6)).
                    createdByUID(data.getString(7)).
                    createdByName(data.getString(8)).
                    dateTime(data.getString(9));

            jobList.add(job);
        }
        return jobList;
    }
}

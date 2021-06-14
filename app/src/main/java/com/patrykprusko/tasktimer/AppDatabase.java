package com.patrykprusko.tasktimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * basic database class for the application,
 * the only class that should use this is AppProvider
 */
class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "TaskTimer.db";
    public static final int DATABASE_VERSION = 1;
    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    /**
     *Get an instance of the app's singleton database helper object
     * @param context -> content providers context
     * @return SQLite database helper object
     */
    static AppDatabase getInstance(Context context) {
        if(instance == null) {
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        String sql = "CREATE TABLE " + TasksContact.TABLE_NAME + "(" +
                TasksContact.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                TasksContact.Columns.TASKS_NAME + " TEXT NOT NULL, " +
                TasksContact.Columns.TASKS_DESCRIPTION + " TEXT, " +
                TasksContact.Columns.TASKS_SORTORDER + " INTEGER);";
        Log.d(TAG, sql);
        db.execSQL(sql);

        Log.d(TAG, "onCreate: ends");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion) {
            case 1:
                // upgrade logic from version 1
                break;
            default:
                throw  new IllegalStateException("onUpgrade() with unlnown newVersion: " + newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }
}

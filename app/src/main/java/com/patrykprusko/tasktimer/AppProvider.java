package com.patrykprusko.tasktimer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    // todo created method -> buildUriMatcher()
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.patrykprusko.tasktimer.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int TASKS = 100;
    private static final int TASKS_ID = 101;

    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;

    /*
    private static final int TASK_TIMINGS = 300;
    private static final int TASK_TIMINGS_ID = 301;
     */

    private static final int  TASK_DURATIONS = 400;
    private static final int TASK_DURATIONS_ID = 401;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // content://com.patrykprusko.tasktimer.provider/Tasks
        matcher.addURI(CONTENT_AUTHORITY, TasksContact.TABLE_NAME, TASKS);
        // content://com.patrykprusko.tasktimer.provider/Tasks/8
        matcher.addURI(CONTENT_AUTHORITY, TasksContact.TABLE_NAME + "/#", TASKS_ID);

//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS);
//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS + "/#", TIMINGS_ID);
//
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS);
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS + "/#", TASK_DURATIONS_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case TASKS:
                queryBuilder.setTables(TasksContact.TABLE_NAME);
                break;

            case TASKS_ID:
                queryBuilder.setTables(TasksContact.TABLE_NAME);
                long taskId = TasksContact.getTaskId(uri);
                queryBuilder.appendWhere(TasksContact.Columns._ID + " = " + taskId);
                break;

//            case TIMINGS:
//                queryBuilder.setTables(TimingsContact.TABLE_NAME);
//                break;
//            case TIMINGS_ID:
//                queryBuilder.setTables(TimingsContact.TABLE_NAME);
//                long timingId = TimingsContact.getTimingId(uri);
//                queryBuilder.appendWhere(TimingsContact.Columns._ID + " = " + timingId);
//                break;
//
//            case TASK_DURATIONS:
//                queryBuilder.setTables(DurationsContact.TABLE_NAME);
//                break;
//            case TASK_DURATIONS_ID:
//                queryBuilder.setTables(DurationsContact.TABLE_NAME);
//                long durationId = DurationsContact.getDurationId(uri);
//                queryBuilder.appendWhere(DurationsContact.Columns._ID + " = " + durationId);
//                break;

            default:
            throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return TasksContact.CONTENT_TYPE;

            case TASKS_ID:
                return TasksContact.CONTENT_ITEM_TYPE;

//            case TIMINGS:
//                return TimingsContact.Timings.CONTENT_TYPE;
//
//            case TIMINGS_ID:
//                return TimingsContact.Timings.CONTENT_ITEM_TYPE;
//
//            case TASK_DURATIONS:
//                return DurationsContact.TaskDurations.CONTENT_TYPE;
//
//            case TASK_DURATIONS_ID:
//                return DurationsContact.TaskDurations.CONTENT_ITEM_TYPE;

        default:
        throw new IllegalArgumentException("Unknown Uri: " + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert: called with uri : " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TasksContact.TABLE_NAME, null, values); // values -> keys = column names and values = column values
                if(recordId >= 0) {
                    returnUri = TasksContact.buildTaskUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                recordId = db.insert(TimingsContact.Timings.buildTimingUri(recordId));
//                if(recordId >= 0) {
//                    returnUri = TimingsContract.Timings.buildTimingUri(recordId);
//                } else {
//                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
//                }
//                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "insert: exiting, returning " + returnUri);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete: called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TasksContact.TABLE_NAME, selection, selectionArgs);
                break;

            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContact.getTaskId(uri);
                selectionCriteria = TasksContact.Columns._ID + " = " + taskId; // _id = 2

                if( (selection != null) && (selection.length() > 0) ) {
                    selectionCriteria += " AND (" + selection + ")"; // _id = 2 AND ( selection )
                }

                count = db.delete(TasksContact.TABLE_NAME, selectionCriteria, selectionArgs); //selectionCriteria -> example _id = 2
                break;

//            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.delete(TimingsContact.TABLE_NAME, selection, selectionArgs);
//                break;
//
//            case TIMINGS_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long timingId = TimingsContact.getTaskId(uri);
//                selectionCriteria = TimingsContact.Columns._ID + " = " + timingId; // _id = 2
//
//                if( (selection != null) && (selection.length() > 0) ) {
//                    selectionCriteria += " AND (" + selection + ")"; // _id = 2 AND ( selection )
//                }
//
//                count = db.delete(TimingsContact.TABLE_NAME,selectionCriteria, selectionArgs); //selectionCriteria -> example _id = 2
//                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "delete: exiting, returning " + count);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update: called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TasksContact.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContact.getTaskId(uri);
                selectionCriteria = TasksContact.Columns._ID + " = " + taskId; // _id = 2

                if( (selection != null) && (selection.length() > 0) ) {
                    selectionCriteria += " AND (" + selection + ")"; // _id = 2 AND ( selection )
                }

                count = db.update(TasksContact.TABLE_NAME, values, selectionCriteria, selectionArgs); //selectionCriteria -> example _id = 2
                break;

//            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.update(TimingsContact.TABLE_NAME, values, selection, selectionArgs);
//                break;
//
//            case TIMINGS_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long timingId = TimingsContact.getTaskId(uri);
//                selectionCriteria = TimingsContact.Columns._ID + " = " + timingId; // _id = 2
//
//                if( (selection != null) && (selection.length() > 0) ) {
//                    selectionCriteria += " AND (" + selection + ")"; // _id = 2 AND ( selection )
//                }
//
//                count = db.update(TimingsContact.TABLE_NAME, values, selectionCriteria, selectionArgs); //selectionCriteria -> example _id = 2
//                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "update: exiting, returning " + count);
        return count;
    }
}

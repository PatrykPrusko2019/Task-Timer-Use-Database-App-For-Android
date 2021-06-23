package com.patrykprusko.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] projection = { TasksContact.Columns._ID,
                                TasksContact.Columns.TASKS_NAME,
                                TasksContact.Columns.TASKS_DESCRIPTION,
                                TasksContact.Columns.TASKS_SORTORDER }; //Name, Description

        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();

        //delete
//        values.put(TasksContact.Columns.TASKS_SORTORDER, "99");
//        values.put(TasksContact.Columns.TASKS_DESCRIPTION, "For deletion");
//        String selection = TasksContact.Columns.TASKS_SORTORDER + " = ?";
//        String[] args = {"99"};

//        int count = contentResolver.delete(TasksContact.buildTaskUri(3), null, null);
//        Log.d(TAG, "onCreate: " + count + " record(s) deleted");

        //delete
        String selection = TasksContact.Columns.TASKS_SORTORDER + " = ?";
        String[] args = {"13"};
        int count = contentResolver.delete(TasksContact.CONTENT_URI, selection, args);
        Log.d(TAG, "onCreate: " + count + " record(s) deleted");

        //update
//        values.put(TasksContact.Columns.TASKS_SORTORDER, "99");
//        values.put(TasksContact.Columns.TASKS_DESCRIPTION, "Completed");
//        String selection = TasksContact.Columns.TASKS_SORTORDER + " = " + 2;
//        int count = contentResolver.update(TasksContact.CONTENT_URI, values, selection, null);
//        Log.d(TAG, "onCreate: " + count + " record(s) updated");

        //update
//        values.put(TasksContact.Columns.TASKS_NAME, "Content provider change name");
//        values.put(TasksContact.Columns.TASKS_DESCRIPTION, "Record content provider video");
//        int count = contentResolver.update(TasksContact.CONTENT_URI, values, null, null);
//        Log.d(TAG, "onCreate: " + count + " record(s) updated");

        //insert
//        values.put(TasksContact.Columns.TASKS_NAME, "New Task 1");
//        values.put(TasksContact.Columns.TASKS_DESCRIPTION, "Description 1");
//        values.put(TasksContact.Columns.TASKS_SORTORDER, 2);
//        Uri uri = contentResolver.insert(TasksContact.CONTENT_URI, values);





        Cursor cursor = contentResolver.query(TasksContact.CONTENT_URI, // show all records
//        Cursor cursor = contentResolver.query(TasksContact.buildTaskUri(3), //show id 3 record
                projection,
                null,
                null,
                TasksContact.Columns.TASKS_SORTORDER);
        
        if(cursor != null) {
            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
            while(cursor.moveToNext()) {
                for(int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
                }
                Log.d(TAG, "onCreate: ================================");
            }
            cursor.close();
        }

//        AppDatabase appDatabase = AppDatabase.getInstance(this);
//        final SQLiteDatabase db = appDatabase.getReadableDatabase();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

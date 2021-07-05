package com.patrykprusko.tasktimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private boolean twoPane = false; //activity in 2-pane mode (tablet or mobile phone)

    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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

        switch (id) {
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDurations:
                break;
            case R.id.menumain_settings:
                break;
            case R.id.menumain_showAbout:
                break;
            case R.id.menumain_generate:
                break;
        }
        
        if (id == R.id.menumain_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void taskEditRequest(Task task) {
        Log.d(TAG, "taskEditRequest: starts");
        if(twoPane) {
            Log.d(TAG, "taskEditRequest: in two-pane mode (tablet)");
            
        } else {
            Log.d(TAG, "taskEditRequest: in single-pane model (phone)");
            // in single-pane mode, start the detail activity for the selected item Id
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if(task != null) { //editing a task
                detailIntent.putExtra(Task.class.getSimpleName(), task);
                startActivity(detailIntent);
            } else { //adding a new task
                startActivity(detailIntent);
            }
        }
    }
}

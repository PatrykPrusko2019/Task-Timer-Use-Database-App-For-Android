package com.patrykprusko.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";

    public enum FragmentEditMode { EDIT, ADD }
    private FragmentEditMode mode;

    private EditText nameTextView;
    private EditText descriptionTextView;
    private EditText sortOrderTextView;
    private Button saveButton;


    public AddEditActivityFragment() {
        Log.d(TAG, "AddEditActivityFragment: constructor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        nameTextView = (EditText) view.findViewById( R.id.addedit_name );
        descriptionTextView = (EditText) view.findViewById(R.id.addedit_description);
        sortOrderTextView = (EditText) view.findViewById(R.id.addedit_sortorder);
        saveButton = (Button) view.findViewById(R.id.addedit_save);

        Bundle arguments = getActivity().getIntent().getExtras();

        final Task task;
        if(arguments != null) {
            Log.d(TAG, "onCreateView: retrieving task details");
            
            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            if(task != null) {
                Log.d(TAG, "onCreateView:  Task details found, editing ...");
                nameTextView.setText( task.getName() );
                sortOrderTextView.setText( Integer.toString(task.getSortOrder()) );
                mode = FragmentEditMode.EDIT;
            } else { //adding a new task
                mode = FragmentEditMode.ADD;
            }
        } else {
            task = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record");
            mode = FragmentEditMode.ADD;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the database if at least one field has changed
                int so; // to save repeated conversions to int
                if(sortOrderTextView.length() > 0) {
                    so = Integer.parseInt(sortOrderTextView.getText().toString());
                } else {
                    so = 0;
                }

                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();

                switch (mode) {
                    case EDIT:
                        if( ! nameTextView.getText().toString().equals(task.getName()) ) {
                            values.put(TasksContact.Columns.TASKS_NAME, nameTextView.getText().toString());
                        }
                        if( ! descriptionTextView.getText().toString().equals(task.getDescription()) ) {
                            values.put(TasksContact.Columns.TASKS_DESCRIPTION, descriptionTextView.getText().toString());
                        }
                        if( so != task.getSortOrder() ) {
                            values.put(TasksContact.Columns.TASKS_SORTORDER, so);
                        }
                        if(values.size() != 0) {
                            Log.d(TAG, "onClick: updating task");
                            contentResolver.update(TasksContact.buildTaskUri(task.getId()), values, null, null);
                        }
                        break;
                    case ADD:
                        if(nameTextView.length() > 0) {
                            Log.d(TAG, "onClick: adding new task");
                            values.put(TasksContact.Columns.TASKS_NAME, nameTextView.getText().toString());
                            values.put(TasksContact.Columns.TASKS_DESCRIPTION, sortOrderTextView.getText().toString());
                            values.put(TasksContact.Columns.TASKS_SORTORDER, so);
                            contentResolver.insert(TasksContact.CONTENT_URI, values);
                        }
                        break;
                }
                Log.d(TAG, "onClick: Done editing");

            }
        });
        Log.d(TAG, "onCreateView: exiting ");

        return view;
    }
}

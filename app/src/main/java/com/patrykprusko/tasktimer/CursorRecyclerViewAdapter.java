package com.patrykprusko.tasktimer;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor cursor;

    public CursorRecyclerViewAdapter(Cursor cursor) {
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: starts");

        if( (cursor == null) || (cursor.getCount() == 0)) {
            Log.d(TAG, "onBindViewHolder: providing instructions");
            holder.name.setText(R.string.instructions_heading);
            holder.description.setText(R.string.instructions);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);

        } else {
            if( ! cursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }
            holder.name.setText( cursor.getString(cursor.getColumnIndex(TasksContact.Columns.TASKS_NAME)) );
            holder.description.setText( cursor.getString(cursor.getColumnIndex(TasksContact.Columns.TASKS_DESCRIPTION)) );
            holder.editButton.setVisibility(View.VISIBLE);  // TODO add onCLick listener
            holder.deleteButton.setVisibility(View.VISIBLE); // TODO add onCLick listener


        }

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: starts");
        if( (cursor == null) || (cursor.getCount() == 0) ) {
            return 1; // fib, because populate a single ViewHolder with instructions
        } else {
            return cursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     *
     * @param newCursor new cursor to be used
     * @return Returns the previously set Cursor, or null if there wasn't one
     * If the given new Cursor is the same instance as the previously set Cursor, null is also returned.
     */
    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor ) {
            return null;
        }

        final Cursor oldCursor = cursor;
        cursor = newCursor;
        if (newCursor != null) {
            //notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            //notify the observers about the lack of a data set
            notifyItemRangeChanged(0, getItemCount());
        }
        return oldCursor;
    }


    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TaskViewHolder";

        TextView name = null;
        TextView description = null;
        ImageButton editButton = null;;
        ImageButton deleteButton = null;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "TaskViewHolder: starts");

            this.name = (TextView) itemView.findViewById(R.id.tli_name);
            this.description = (TextView) itemView.findViewById(R.id.tli_description);
            this.editButton = (ImageButton) itemView.findViewById(R.id.tli_edit);
            this.deleteButton = (ImageButton) itemView.findViewById(R.id.tli_delete);
        }
    }

}

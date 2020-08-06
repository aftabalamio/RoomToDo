package com.mdaftabalam.todolist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdaftabalam.todolist.R;
import com.mdaftabalam.todolist.adapter.TaskAdapter;
import com.mdaftabalam.todolist.database.NoteDatabase;
import com.mdaftabalam.todolist.model.Note;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aftab Alam on 06-08-2020.
 **/
public class TaskListActivity extends AppCompatActivity implements TaskAdapter.OnNoteItemClick {

    private TextView textViewMsg;
    private NoteDatabase noteDatabase;
    private List<Note> notes;
    private TaskAdapter taskAdapter;
    private int pos;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(TaskListActivity.this, AddTaskActivity.class), 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        initializeVies();
        displayList();
    }

    private void displayList() {
        noteDatabase = NoteDatabase.getInstance(TaskListActivity.this);
        new RetrieveTask(this).execute();
    }

    private void initializeVies() {
        textViewMsg = (TextView) findViewById(R.id.tv_empty);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(TaskListActivity.this));
        notes = new ArrayList<>();
        taskAdapter = new TaskAdapter(notes, TaskListActivity.this);
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                notes.add((Note) data.getSerializableExtra("note"));
            } else if (resultCode == 2) {
                notes.set(pos, (Note) data.getSerializableExtra("note"));
            }
            listVisibility();
        }
    }

    @Override
    public void onNoteClick(final int pos) {
        new AlertDialog.Builder(TaskListActivity.this)
                .setItems(new String[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                TaskListActivity.this.pos = pos;
                                startActivityForResult(new Intent(TaskListActivity.this, AddTaskActivity.class).putExtra("note", notes.get(pos)), 100);
                                break;
                            case 1:
                                noteDatabase.getNoteDao().deleteNote(notes.get(pos));
                                notes.remove(pos);
                                listVisibility();
                                break;
                        }
                    }
                }).show();
    }

    private void listVisibility() {
        int emptyMsgVisibility = View.GONE;
        if (notes.size() == 0) {
            //No item to display
            if (textViewMsg.getVisibility() == View.GONE) {
                emptyMsgVisibility = View.VISIBLE;
            }
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        noteDatabase.cleanUp();
        super.onDestroy();
    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<Note>> {

        private WeakReference<TaskListActivity> activityReference;

        //Only retain a weak reference to the activity
        RetrieveTask(TaskListActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get() != null) {
                return activityReference.get().noteDatabase.getNoteDao().getNotes();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes != null && notes.size() > 0) {
                activityReference.get().notes.clear();
                activityReference.get().notes.addAll(notes);
                //Hides empty text view
                activityReference.get().textViewMsg.setVisibility(View.GONE);
                activityReference.get().taskAdapter.notifyDataSetChanged();
            }
        }
    }
}

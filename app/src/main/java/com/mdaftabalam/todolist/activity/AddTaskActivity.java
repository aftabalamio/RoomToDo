package com.mdaftabalam.todolist.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mdaftabalam.todolist.R;
import com.mdaftabalam.todolist.database.NoteDatabase;
import com.mdaftabalam.todolist.model.Note;

import java.lang.ref.WeakReference;

/**
 * Created by Aftab Alam on 06-08-2020.
 **/
public class AddTaskActivity extends AppCompatActivity {

    private TextInputEditText et_title, et_content;
    private NoteDatabase noteDatabase;
    private Note note;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        noteDatabase = NoteDatabase.getInstance(AddTaskActivity.this);
        Button button = findViewById(R.id.but_save);
        if ((note = (Note) getIntent().getSerializableExtra("note")) != null) {
            getSupportActionBar().setTitle("Update Task");
            update = true;
            button.setText("Update");
            et_title.setText(note.getTitle());
            et_content.setText(note.getContent());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (update) {
                        note.setContent(et_content.getText().toString());
                        note.setTitle(et_title.getText().toString());
                        noteDatabase.getNoteDao().updateNote(note);
                        setResult(note, 2);
                    } else {
                        if (et_title.getText().toString().isEmpty()) {
                            et_title.setError("enter your title");
                            et_title.requestFocus();
                        } else if (et_content.getText().toString().isEmpty()) {
                            et_content.setError("enter your description");
                            et_content.requestFocus();
                        } else {
                            note = new Note(et_content.getText().toString(), et_title.getText().toString());
                            new InsertTask(AddTaskActivity.this, note).execute();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddTaskActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setResult(Note note, int flag) {
        setResult(flag, new Intent().putExtra("note", note));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddTaskActivity> activityReference;
        private Note note;

        //only retain a weak reference to the activity
        InsertTask(AddTaskActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented note id
            long j = activityReference.get().noteDatabase.getNoteDao().insertNote(note);
            note.setNote_id(j);
            Log.e("ID ", "doInBackground: " + j);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(note, 1);
                activityReference.get().finish();
            }
        }
    }
}

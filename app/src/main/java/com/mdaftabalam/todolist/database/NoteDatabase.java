package com.mdaftabalam.todolist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mdaftabalam.todolist.dao.NoteDao;
import com.mdaftabalam.todolist.model.Note;
import com.mdaftabalam.todolist.util.Constants;
import com.mdaftabalam.todolist.util.DateRoomConverter;

/**
 * Created by Aftab Alam on 06-08-2020.
 **/
@Database(entities = {Note.class}, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase noteDB;

    // synchronized is use to avoid concurrent access in multi thread environment
    public static /*synchronized*/ NoteDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static NoteDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                NoteDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public abstract NoteDao getNoteDao();

    public void cleanUp() {
        noteDB = null;
    }
}
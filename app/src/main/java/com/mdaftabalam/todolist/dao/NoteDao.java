package com.mdaftabalam.todolist.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mdaftabalam.todolist.model.Note;
import com.mdaftabalam.todolist.util.Constants;

import java.util.List;

/**
 * Created by Aftab Alam on 06-08-2020.
 **/
@Dao
public interface NoteDao {

    @Query("SELECT * FROM " + Constants.TABLE_NAME_NOTE)
    List<Note> getNotes();

    /*
     * Insert the object in database
     * @param note, object to be inserted
     */
    @Insert
    long insertNote(Note note);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void updateNote(Note repos);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void deleteNote(Note note);
}

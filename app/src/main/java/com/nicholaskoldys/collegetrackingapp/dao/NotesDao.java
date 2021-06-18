package com.nicholaskoldys.collegetrackingapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nicholaskoldys.collegetrackingapp.model.Notes;

import java.util.List;

@Dao
public interface NotesDao extends DaoBase<Notes> {

    @Override
    @Query("SELECT * FROM notes WHERE id = :id")
    Notes getById(int id);

    @Query("SELECT * FROM notes WHERE course_id = :id")
    LiveData<List<Notes>> getAllLiveNotesFromCourse(int id);

    @Override
    @Query("SELECT * FROM notes")
    LiveData<List<Notes>> getAllLive();

    @Override
    @Query("SELECT * FROM notes")
    List<Notes> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notes item);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Notes> items);

    @Override
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Notes item);

    @Override
    @Delete
    void delete(Notes item);

    @Override
    @Query("DELETE FROM notes")
    void deleteAll();
}

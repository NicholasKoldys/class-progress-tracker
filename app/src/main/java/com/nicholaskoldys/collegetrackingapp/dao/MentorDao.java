package com.nicholaskoldys.collegetrackingapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import java.util.List;

@Dao
public interface MentorDao extends DaoBase<Mentor> {

    @Override
    @Query("SELECT * FROM mentor WHERE id = :id")
    Mentor getById(int id);

    @Query("SELECT * FROM mentor WHERE course_id = :id")
    LiveData<List<Mentor>> getMentorsByCourse(int id);

    @Override
    @Query("SELECT * FROM mentor")
    LiveData<List<Mentor>> getAllLive();

    @Override
    @Query("SELECT * FROM mentor")
    List<Mentor> getAll();

    @Override
    @Insert
    void insert(Mentor item);

    @Override
    @Insert
    void insertAll(List<Mentor> items);

    @Override
    @Update
    void update(Mentor item);

    @Override
    @Delete
    void delete(Mentor item);

    @Override
    @Query("DELETE FROM mentor")
    void deleteAll();
}

package com.nicholaskoldys.collegetrackingapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nicholaskoldys.collegetrackingapp.model.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao extends DaoBase<Assessment> {

    @Override
    @Query("SELECT * FROM assessment WHERE id = :id")
    Assessment getById(int id);

    @Query("SELECT * FROM assessment WHERE course_id = :id")
    LiveData<List<Assessment>> getAllLiveAssessmentsFromCourse(int id);

    @Override
    @Query("SELECT * FROM assessment")
    LiveData<List<Assessment>> getAllLive();

    @Override
    @Query("SELECT * FROM assessment")
    List<Assessment> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Assessment assessment);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Assessment> assessments);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Assessment assessment);

    @Override
    @Delete
    void delete(Assessment assessment);

    @Override
    @Query("DELETE FROM assessment")
    void deleteAll();
}

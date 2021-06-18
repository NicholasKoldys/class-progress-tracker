package com.nicholaskoldys.collegetrackingapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nicholaskoldys.collegetrackingapp.model.Goal;

import java.util.List;

@Dao
public interface GoalDao extends DaoBase<Goal> {

    @Override
    @Query("SELECT * FROM goal WHERE id = :id")
    Goal getById(int id);

    @Query("SELECT * FROM goal WHERE assessment_id = :assessmentId")
    LiveData<List<Goal>> getGoalsByAssessment(int assessmentId);

    @Override
    @Query("SELECT * FROM goal")
    LiveData<List<Goal>> getAllLive();

    @Override
    @Query("SELECT * FROM goal")
    List<Goal> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Goal item);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Goal> items);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Goal item);

    @Override
    @Delete
    void delete(Goal item);

    @Override
    @Query("DELETE FROM goal")
    void deleteAll();
}

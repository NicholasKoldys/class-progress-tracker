package com.nicholaskoldys.collegetrackingapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import java.util.List;

@Dao
public interface CourseDao extends DaoBase<Course> {

    @Override
    @Query("SELECT * FROM course WHERE id = :id")
    Course getById(int id);

    @Query("SELECT * FROM course WHERE term_id = :id")
    LiveData<List<Course>> getAllLiveCoursesFromTerm(int id);

    @Override
    @Query("SELECT * FROM course")
    LiveData<List<Course>> getAllLive();

    @Override
    @Query("SELECT * FROM course")
    List<Course> getAll();

    @Query("SELECT * FROM course WHERE term_id = :termId")
    List<Course> getAllTermsCourses(int termId);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Course> courses);

    @Override
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Course course);

    @Override
    @Delete
    void delete(Course course);

    @Override
    @Query("DELETE FROM course")
    void deleteAll();
}

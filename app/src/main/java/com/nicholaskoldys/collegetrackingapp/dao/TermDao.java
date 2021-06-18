package com.nicholaskoldys.collegetrackingapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nicholaskoldys.collegetrackingapp.model.Term;

import java.util.List;

@Dao
public interface TermDao extends DaoBase<Term> {

    @Override
    @Query("SELECT * FROM term WHERE id = :id")
    Term getById(int id);

    @Override
    @Query("SELECT * FROM term")
    LiveData<List<Term>> getAllLive();

    @Override
    @Query("SELECT * FROM term")
    List<Term> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Term term);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Term> terms);

    @Override
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Term term);

    @Override
    @Delete
    void delete(Term term);

    @Override
    @Query("DELETE FROM term")
    void deleteAll();
}

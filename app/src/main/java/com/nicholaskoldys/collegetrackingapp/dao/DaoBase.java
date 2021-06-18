package com.nicholaskoldys.collegetrackingapp.dao;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface DaoBase<T> {

//    @Query("SELECT * FROM term WHERE id = :id")
    T getById(int id);

//    @Query("SELECT * FROM term")
    LiveData<List<T>> getAllLive();

//    @Query("SELECT * FROM term")
    List<T> getAll();

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T item);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<T> items);

//    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(T item);

//    @Delete
    void delete(T item);

//    @Query("DELETE FROM term")
    void deleteAll();
}

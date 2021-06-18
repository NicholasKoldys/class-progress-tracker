package com.nicholaskoldys.collegetrackingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nicholaskoldys.collegetrackingapp.utility.Formatters;

import java.util.Calendar;

@Entity
public class Term implements CollegeBase {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Calendar startDate;
    private Calendar endDate;

    @Ignore
    public Term(String title, Calendar startDate, Calendar endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Term(int id, String title, Calendar startDate, Calendar endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public String getStartDateString() {
        return Formatters.calendarToMMDDYYYY(startDate);
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public String getEndDateString() {
        return Formatters.calendarToMMDDYYYY(endDate);
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    @NonNull
    @Override
    public String toString() {
        return getTitle();
    }
}

package com.nicholaskoldys.collegetrackingapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.Formatters;

import java.util.Calendar;

import static androidx.room.ForeignKey.CASCADE;

@Entity
@ForeignKey(onDelete = CASCADE, entity = Term.class, parentColumns = "id", childColumns = "term_id")
public class Course implements CollegeBase {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private @Constants.CourseStatus int status;
    private Calendar startDate;
    private Calendar endDate;
    @ColumnInfo(name = "term_id")
    private int termId;

    @Ignore
    public Course(String title, @Constants.CourseStatus int status, Calendar startDate, Calendar endDate, int termId) {
        this.title = title;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.termId = termId;
    }

    public Course(int id, String title, int status, Calendar startDate, Calendar endDate, int termId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.termId = termId;
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

    public @Constants.CourseStatus int getStatus() {
        return status;
    }

    public void setStatus(@Constants.CourseStatus int status) {
        this.status = status;
    }

    public String getStatusString() {
        return Constants.courseStatusToString(status);
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

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    @NonNull
    @Override
    public String toString() {
        return title + "\nStatus: " + Constants.courseStatusToString(status) ;
    }
}

package com.nicholaskoldys.collegetrackingapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nicholaskoldys.collegetrackingapp.utility.Formatters;

import java.util.Calendar;

import static androidx.room.ForeignKey.CASCADE;

@Entity
@ForeignKey(onDelete = CASCADE, entity = Course.class, parentColumns = "id", childColumns = "course_id")
public class Assessment implements CollegeBase {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Calendar dueDate;
    private String info;
    @ColumnInfo(name = "course_id")
    private int courseId;

    @Ignore
    public Assessment(String title, Calendar dueDate, String info, int courseId) {
        this.title = title;
        this.dueDate = dueDate;
        this.info = info;
        this.courseId = courseId;
    }

    public Assessment(int id, String title, Calendar dueDate, String info, int courseId) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.info = info;
        this.courseId = courseId;
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

    public Calendar getDueDate() {
        return dueDate;
    }

    public String getDueDateString() {
        return Formatters.calendarToMMDDYYYY(dueDate);
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @NonNull
    @Override
    public String toString() {
        return title + "\nDue: " + getDueDateString();
    }
}
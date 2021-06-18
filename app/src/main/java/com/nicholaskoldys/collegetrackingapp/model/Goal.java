package com.nicholaskoldys.collegetrackingapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Calendar;

import static androidx.room.ForeignKey.CASCADE;

@Entity
@ForeignKey(onDelete = CASCADE, entity = Assessment.class, parentColumns = "id", childColumns = "assessment_id")
public class Goal implements CollegeBase {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private int uniqueId;
    private Calendar dateAndTime;
    @ColumnInfo(name = "assessment_id")
    private int assessmentId;

//    @Ignore
//    public Goal(String title, int uniqueId, String notificationTag, Calendar dateAndTime, int assessmentId) {
//        this.title = title;
//        this.uniqueId = uniqueId;
//        this.notificationTag = notificationTag;
//        this.dateAndTime = dateAndTime;
//        this.assessmentId = assessmentId;
//    }

    public Goal(int id, String title, int uniqueId, Calendar dateAndTime, int assessmentId) {
        this.id = id;
        this.title = title;
        this.uniqueId = uniqueId;
        this.dateAndTime = dateAndTime;
        this.assessmentId = assessmentId;
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

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Calendar getDateAndTime() {
        return dateAndTime;
    }

    public String getDateString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(dateAndTime.getTime());
    }

    public String getTimeString() {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(dateAndTime.getTime());
    }

    public String getDateAndTimeString() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(dateAndTime.getTime());
    }

    public void setDateAndTime(Calendar dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    @NonNull
    @Override
    public String toString() {
        return title + "\n" + getDateAndTimeString();
    }
}

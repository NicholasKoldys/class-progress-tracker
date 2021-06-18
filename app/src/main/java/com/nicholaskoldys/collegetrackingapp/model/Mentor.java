package com.nicholaskoldys.collegetrackingapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
@ForeignKey(onDelete = CASCADE, entity = Course.class, parentColumns = "id", childColumns = "course_id")
public class Mentor implements CollegeBase {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String phone;
    private String email;
    @ColumnInfo(name = "course_id")
    private int courseId;

    @Ignore
    public Mentor(String title, String phone, String email, int courseId) {
        this.title = title;
        this.phone = phone;
        this.email = email;
        this.courseId = courseId;
    }

    public Mentor(int id, String title, String phone, String email, int courseId) {
        this.id = id;
        this.title = title;
        this.phone = phone;
        this.email = email;
        this.courseId = courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return getTitle();
    }
}

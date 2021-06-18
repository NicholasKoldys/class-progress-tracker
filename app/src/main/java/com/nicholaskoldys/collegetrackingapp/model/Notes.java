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
public class Notes implements CollegeBase {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @ColumnInfo(name = "course_id")
    private int courseId;
    private String content;

    @Ignore
    public Notes(String title, int courseId, String content) {
        this.title = title;
        this.courseId = courseId;
        this.content = content;
    }

    public Notes(int id, String title, int courseId, String content) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
        this.content = content;
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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return getTitle() + " | " + getContent();
    }
}

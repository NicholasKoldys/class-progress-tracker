package com.nicholaskoldys.collegetrackingapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nicholaskoldys.collegetrackingapp.database.AppRepository;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import com.nicholaskoldys.collegetrackingapp.model.Notes;
import com.nicholaskoldys.collegetrackingapp.utility.AlarmBuilder;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.UniqueIdentifierGenerator;

import java.util.Calendar;
import java.util.List;

import static com.nicholaskoldys.collegetrackingapp.utility.Constants.NotificationType.COURSE_END_NOTIFICATION_CODE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.NotificationType.COURSE_START_NOTIFICATION_CODE;

public class CourseViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Course>> coursesList;
    private MutableLiveData<Course> selectedCourse;
    private MutableLiveData<Notes> selectedNote;
    private LiveData<List<Assessment>> courseAssessmentsList;
    private LiveData<List<Notes>> courseNotesList;
    private LiveData<List<Mentor>> courseMentorsList;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        coursesList = repository.getAllLiveCourses();
    }

    public LiveData<List<Course>> getCoursesList() {
        return coursesList;
    }

    public MutableLiveData<Course> getCourse(int courseId) {

        loadSelectedItem(courseId);
        return selectedCourse;
    }

    public LiveData<List<Assessment>> getCourseAssessmentsList(int courseId) {

        loadSelectedItem(courseId);
        return courseAssessmentsList;
    }

    public LiveData<List<Notes>> getCourseNotesList(int courseId) {

        loadSelectedItem(courseId);
        return courseNotesList;
    }

    public LiveData<List<Mentor>> getCourseMentorsList(int courseId) {

        loadSelectedItem(courseId);
        return courseMentorsList;
    }

    private void loadSelectedItem(int courseId) {

        selectedCourse = new MutableLiveData<>();
        selectedCourse.postValue((Course) repository.getById(Constants.CollegeItemType.COURSE_TYPE, courseId));
        courseAssessmentsList = repository.getLiveAssessmentsFromCourse(courseId);
        courseNotesList = repository.getLiveNotesFromCourse(courseId);
        courseMentorsList = repository.getLiveMentorsFromCourse(courseId);
    }

    public MutableLiveData<Notes> getNote(int noteId) {
        loadSelectedNote(noteId);
        return selectedNote;
    }

    private void loadSelectedNote(int noteId) {
        selectedNote = new MutableLiveData<>();
        selectedNote.postValue((Notes) repository.getById(Constants.CollegeItemType.NOTES_TYPE, noteId));
    }




    public void submitCourse(String title, Calendar start, Calendar end, @Constants.CourseStatus int status, int termId, Boolean isStartAlertEnabled, Boolean isEndAlertEnabled) {

        Course course = new Course(title, status, start, end, termId);
        repository.insert(Constants.CollegeItemType.COURSE_TYPE, course);
        if(isStartAlertEnabled) {
            scheduleCourseStartDateNotifications(course);
        }
        if(isEndAlertEnabled) {
            scheduleCourseEndDateNotifications(course);
        }
    }

    public void updateCourse(int courseId, String title, Calendar start, Calendar end, @Constants.CourseStatus int status,  Boolean isStartAlertEnabled, Boolean isEndAlertEnabled) {

        Course course = (Course) repository.getById(Constants.CollegeItemType.COURSE_TYPE, courseId);
        course.setTitle(title);
        course.setStartDate(start);
        course.setEndDate(end);
        course.setStatus(status);
        if(isStartAlertEnabled) {
            scheduleCourseStartDateNotifications(course);
        } else {
            cancelCourseStartDateNotifications(course);
        }
        if(isEndAlertEnabled) {
            scheduleCourseEndDateNotifications(course);
        } else {
            cancelCourseEndDateNotifications(course);
        }
        repository.update(Constants.CollegeItemType.COURSE_TYPE, course);
    }

    public void deleteCourse(int courseId) {

        Course course = (Course) repository.getById(Constants.CollegeItemType.COURSE_TYPE, courseId);
        cancelCourseStartDateNotifications(course);
        cancelCourseEndDateNotifications(course);
        repository.delete(Constants.CollegeItemType.COURSE_TYPE, course);
    }

    private void scheduleCourseStartDateNotifications(Course course) {
        int uniqueIdStart = UniqueIdentifierGenerator.generateUniqueId(course.getId(), COURSE_START_NOTIFICATION_CODE, COURSE_START_NOTIFICATION_CODE);
        String courseNotificationTitleStart = course.getTitle() + " is Starting!";
        AlarmBuilder.setAlarm(getApplication(), Constants.CollegeItemType.COURSE_TYPE, course.getStartDate(), courseNotificationTitleStart, uniqueIdStart);
    }

    private void scheduleCourseEndDateNotifications(Course course) {
        int uniqueIdEnd = UniqueIdentifierGenerator.generateUniqueId(course.getId(), COURSE_END_NOTIFICATION_CODE, COURSE_END_NOTIFICATION_CODE);
        String courseNotificationTitleEnd = course.getTitle() + " is Ending!";
        AlarmBuilder.setAlarm(getApplication(), Constants.CollegeItemType.COURSE_TYPE, course.getEndDate(), courseNotificationTitleEnd, uniqueIdEnd);
    }

    private void cancelCourseStartDateNotifications(Course course) {
        int uniqueIdStart = UniqueIdentifierGenerator.generateUniqueId(course.getId(), COURSE_START_NOTIFICATION_CODE, COURSE_START_NOTIFICATION_CODE);
        String courseNotificationTitleStart = course.getTitle() + " is Starting!";
        AlarmBuilder.cancelAlarm(getApplication(), Constants.CollegeItemType.COURSE_TYPE, course.getStartDate(), courseNotificationTitleStart, uniqueIdStart);
    }

    private void cancelCourseEndDateNotifications(Course course) {
        int uniqueIdEnd = UniqueIdentifierGenerator.generateUniqueId(course.getId(), COURSE_END_NOTIFICATION_CODE, COURSE_END_NOTIFICATION_CODE);
        String courseNotificationTitleEnd = course.getTitle() + " is Ending!";
        AlarmBuilder.cancelAlarm(getApplication(), Constants.CollegeItemType.COURSE_TYPE, course.getEndDate(), courseNotificationTitleEnd, uniqueIdEnd);
    }




    public void submitNotes(String title, int courseId, String noteText) {

        repository.insert(Constants.CollegeItemType.NOTES_TYPE, new Notes(title, courseId, noteText));
    }

    public void updateNotes(int noteId, String noteText) {

        Notes note = (Notes) repository.getById(Constants.CollegeItemType.NOTES_TYPE, noteId);
        note.setContent(noteText);
        repository.update(Constants.CollegeItemType.NOTES_TYPE, note);
    }

    public void deleteNotes(int noteId) {

        Notes note = (Notes) repository.getById(Constants.CollegeItemType.NOTES_TYPE, noteId);
        repository.delete(Constants.CollegeItemType.NOTES_TYPE, note);
    }




    public void submitMentor(String name, String email, String phone, int courseId) {

        repository.insert(Constants.CollegeItemType.MENTOR_TYPE, new Mentor(name, phone, email, courseId));
    }

    public void updateMentor(int mentorId, String name, String email, String phone) {

        Mentor mentor = (Mentor) repository.getById(Constants.CollegeItemType.MENTOR_TYPE, mentorId);
        mentor.setTitle(name);
        mentor.setEmail(email);
        mentor.setPhone(phone);
        repository.update(Constants.CollegeItemType.MENTOR_TYPE, mentor);
    }

    public void deleteMentor(int mentorId) {

        Mentor mentor = (Mentor) repository.getById(Constants.CollegeItemType.MENTOR_TYPE, mentorId);
        repository.delete(Constants.CollegeItemType.MENTOR_TYPE, mentor);
    }
}
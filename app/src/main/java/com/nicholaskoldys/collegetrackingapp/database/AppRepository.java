package com.nicholaskoldys.collegetrackingapp.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.nicholaskoldys.collegetrackingapp.dao.AssessmentDao;
import com.nicholaskoldys.collegetrackingapp.dao.CourseDao;
import com.nicholaskoldys.collegetrackingapp.dao.DaoBase;
import com.nicholaskoldys.collegetrackingapp.dao.GoalDao;
import com.nicholaskoldys.collegetrackingapp.dao.MentorDao;
import com.nicholaskoldys.collegetrackingapp.dao.NotesDao;
import com.nicholaskoldys.collegetrackingapp.dao.TermDao;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Goal;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import com.nicholaskoldys.collegetrackingapp.model.Notes;
import com.nicholaskoldys.collegetrackingapp.model.Term;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppRepository {

    private TermDao termDao;
    private CourseDao courseDao;
    private AssessmentDao assessmentDao;
    private MentorDao mentorDao;
    private NotesDao notesDao;
    private GoalDao goalDao;

    private LiveData<List<Term>> allLiveTerms;
    private LiveData<List<Course>> allLiveCourses;
    private LiveData<List<Assessment>> allLiveAssessments;
    private LiveData<List<Mentor>> allLiveMentors;
    private LiveData<List<Notes>> allLiveNotes;
    private LiveData<List<Goal>> allLiveGoals;

    private static AppRepository INSTANCE;


    private AppRepository(@NonNull Application application) {

        AppDatabase db = AppDatabase.getInstance(application);

        termDao = db.TermDao();
        courseDao = db.CourseDao();
        assessmentDao = db.AssessmentDao();
        mentorDao = db.MentorDao();
        notesDao = db.NotesDao();
        goalDao = db.GoalDao();

        allLiveTerms =   termDao.getAllLive();
        allLiveCourses = courseDao.getAllLive();
        allLiveAssessments = assessmentDao.getAllLive();
        allLiveMentors = mentorDao.getAllLive();
        allLiveNotes = notesDao.getAllLive();
        allLiveGoals = goalDao.getAllLive();
    }

    public static AppRepository getInstance(@NonNull Application application) {

        if(INSTANCE == null) {
            synchronized (AppRepository.class) {
                if(INSTANCE == null) {
                    INSTANCE = new AppRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    private DaoBase checkDao(@Constants.CollegeItemType int itemType) {

        if(itemType == Constants.CollegeItemType.TERM_TYPE) {
            return termDao;
        } else if(itemType == Constants.CollegeItemType.COURSE_TYPE) {
            return courseDao;
        } else if(itemType == Constants.CollegeItemType.ASSESSMENT_TYPE) {
            return assessmentDao;
        } else if(itemType == Constants.CollegeItemType.MENTOR_TYPE) {
            return mentorDao;
        } else if(itemType == Constants.CollegeItemType.NOTES_TYPE) {
            return notesDao;
        } else if(itemType == Constants.CollegeItemType.GOAL_TYPE) {
            return goalDao;
        }

        return null;
    }

    public <T extends CollegeBase, D extends DaoBase<T>> CollegeBase getById(@Constants.CollegeItemType int itemType, int itemId) {

        T itemFromId;

        try {
            //Log.i("MAIN", "Attempting gotById from repo on itemId: " + itemId);
            itemFromId = (T) new RetrieveAsyncTask<T, D>(checkDao(itemType)).execute(itemId).get();
            //Log.i("MAIN", "Success gotById from Repo" + itemFromId.toString());
            return itemFromId;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public LiveData<List<Term>> getAllLiveTerms() {
        return allLiveTerms;
    }

    public LiveData<List<Course>> getAllLiveCourses() {
        return allLiveCourses;
    }

    public LiveData<List<Assessment>> getAllLiveAssessments() {
        return allLiveAssessments;
    }

    public LiveData<List<Mentor>> gettAllLiveMentors() {
        return allLiveMentors;
    }

    public LiveData<List<Notes>> getAllLiveNotes() {
        return allLiveNotes;
    }

    public LiveData<List<Course>> getLiveCoursesFromTerm(int termId) {
        return courseDao.getAllLiveCoursesFromTerm(termId);
    }

    public LiveData<List<Notes>> getLiveNotesFromCourse(int courseId) {
        return notesDao.getAllLiveNotesFromCourse(courseId);
    }

    public LiveData<List<Assessment>> getLiveAssessmentsFromCourse(int courseId) {
        return assessmentDao.getAllLiveAssessmentsFromCourse(courseId);
    }

    public LiveData<List<Mentor>> getLiveMentorsFromCourse(int courseId) {
        return mentorDao.getMentorsByCourse(courseId);
    }

    public LiveData<List<Goal>> getLiveGoalsFromAssessment(int assessmentId) {
        return goalDao.getGoalsByAssessment(assessmentId);
    }



    public <T extends CollegeBase, D extends DaoBase<T>> void insert(@Constants.CollegeItemType int itemType, T item) {

        new InsertAsyncTask<T, D>(item, checkDao(itemType)).execute();
    }

    public <T extends CollegeBase, D extends DaoBase<T>> void insertAll(@Constants.CollegeItemType int itemType, List<T> items) {

        new InsertAllAsyncTask<T, D>(items, checkDao(itemType)).execute();
    }

    public <T extends CollegeBase, D extends DaoBase<T>> void update(@Constants.CollegeItemType int itemType, T item) {

        new UpdateAsyncTask<T, D>(item, checkDao(itemType)).execute();
    }

    public <T extends CollegeBase, D extends DaoBase<T>> void delete(@Constants.CollegeItemType int itemType, T item) {

        new DeleteAsyncTask<T, D>(item, checkDao(itemType)).execute();
    }

    private static class RetrieveAsyncTask<T extends CollegeBase, D extends DaoBase<T>>
            extends AsyncTask<Integer, Void, Object> {

        private DaoBase<T> dao;

        private RetrieveAsyncTask(DaoBase<T> dao) {
            this.dao = dao;
        }

        @Override
        protected T doInBackground(Integer... ints) {

            return dao.getById(ints[0]);
        }
    }

    private static class InsertAsyncTask<T extends CollegeBase, D extends DaoBase<T>>
            extends AsyncTask<Void, Void, Void> {

        private T item;
        private DaoBase<T> dao;

        InsertAsyncTask(T item, DaoBase<T> dao) {
            this.item = item;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Log.i("MAIN", "Inserting... | " + item.toString());
            dao.insert(item);
            return null;
        }
    }

    private static class InsertAllAsyncTask<T extends CollegeBase, D extends DaoBase<T>>
            extends AsyncTask<Void, Void, Void> {

        private List<T> itemList;
        private DaoBase<T> dao;

        InsertAllAsyncTask(List<T> items, DaoBase<T> dao) {
            this.itemList = items;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.insertAll(itemList);
            return null;
        }
    }

    private static class UpdateAsyncTask<T extends CollegeBase, D extends DaoBase<T>>
            extends AsyncTask<Void, Void, Void> {

        private T item;
        private DaoBase<T> dao;

        UpdateAsyncTask(T item, DaoBase<T> dao) {
            this.item = item;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //Log.i("MAIN", "Updating... | " + item.toString());
            dao.update(item);
            return null;
        }
    }

    private static class DeleteAsyncTask<T extends CollegeBase, D extends DaoBase<T>>
            extends AsyncTask<Void, Void, Void> {

        private T item;
        private DaoBase<T> dao;

        DeleteAsyncTask(T item, DaoBase<T> dao) {
            this.item = item;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //Log.i("MAIN", "Deleting.. " + item.toString());
            dao.delete(item);
            return null;
        }
    }
}

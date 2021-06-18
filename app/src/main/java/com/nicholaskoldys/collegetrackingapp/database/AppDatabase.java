package com.nicholaskoldys.collegetrackingapp.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nicholaskoldys.collegetrackingapp.dao.AssessmentDao;
import com.nicholaskoldys.collegetrackingapp.dao.CourseDao;
import com.nicholaskoldys.collegetrackingapp.dao.GoalDao;
import com.nicholaskoldys.collegetrackingapp.dao.MentorDao;
import com.nicholaskoldys.collegetrackingapp.dao.NotesDao;
import com.nicholaskoldys.collegetrackingapp.dao.TermDao;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Goal;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import com.nicholaskoldys.collegetrackingapp.model.Notes;
import com.nicholaskoldys.collegetrackingapp.model.Term;
import com.nicholaskoldys.collegetrackingapp.sampledata.SampleData;
import com.nicholaskoldys.collegetrackingapp.utility.Converters;

@Database(entities = {Term.class, Course.class, Assessment.class, Mentor.class, Notes.class, Goal.class},
        version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TermDao TermDao();
    public abstract CourseDao CourseDao();
    public abstract AssessmentDao AssessmentDao();
    public abstract MentorDao MentorDao();
    public abstract NotesDao NotesDao();
    public abstract GoalDao GoalDao();

    private static final String DATABASE_NAME = "CollegeDatabase";
    private static AppDatabase INSTANCE;


    public static AppDatabase getInstance(@NonNull Application application) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = buildDatabase(application);
                }
            }
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(createDataCallback())
                .build();
    }

    private static RoomDatabase.Callback createDataCallback() {
        return new Callback() {

            /**
             * OnCreate writes the database when first created
             *
             */
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new CreateDataAsyncTask(INSTANCE).execute();
            }

            /**
             * OnOpen appends or overwrites to the database when app is opened
             *
             */
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                /**
                 * UnComment to deleteDatabase
                 * Must iterate version if change was made to models
                 */
//                new DeleteDataAsyncTask(INSTANCE).execute();
//                new CreateDataAsyncTask(INSTANCE).execute();
            }
        };
    }

    //TODO : 1 When adding new table make sure data is initialized here
    private static class CreateDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private TermDao termDao;
        private CourseDao courseDao;
        private AssessmentDao assessmentDao;
        private MentorDao mentorDao;
        private NotesDao notesDao;
        private GoalDao goalDao;

        private CreateDataAsyncTask(AppDatabase database) {

            this.termDao = database.TermDao();
            this.courseDao = database.CourseDao();
            this.assessmentDao = database.AssessmentDao();
            this.mentorDao = database.MentorDao();
            this.notesDao = database.NotesDao();
            this.goalDao = database.GoalDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            termDao.insertAll(SampleData.loadSampleTerms());
            courseDao.insertAll(SampleData.loadSampleCourses());
            assessmentDao.insertAll(SampleData.loadSampleAssessments());
            notesDao.insertAll(SampleData.loadSampleNotes());
            mentorDao.insertAll(SampleData.loadSampleMentors());
            //goalDao.insertAll(SampleData.loadSampleGoals());
            return null;
        }
    }

    //TODO : 1 and removed here
    private static class DeleteDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private TermDao termDao;
        private CourseDao courseDao;
        private AssessmentDao assessmentDao;
        private MentorDao mentorDao;
        private NotesDao notesDao;
        private GoalDao goalDao;

        private DeleteDataAsyncTask(AppDatabase database) {

            this.termDao = database.TermDao();
            this.courseDao = database.CourseDao();
            this.assessmentDao = database.AssessmentDao();
            this.mentorDao = database.MentorDao();
            this.notesDao = database.NotesDao();
            this.goalDao = database.GoalDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            termDao.deleteAll();
            courseDao.deleteAll();
            assessmentDao.deleteAll();
            mentorDao.deleteAll();
            notesDao.deleteAll();
            goalDao.deleteAll();
            return null;
        }
    }
}

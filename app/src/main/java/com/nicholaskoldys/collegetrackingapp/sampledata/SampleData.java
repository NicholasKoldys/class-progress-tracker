package com.nicholaskoldys.collegetrackingapp.sampledata;

import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import com.nicholaskoldys.collegetrackingapp.model.Notes;
import com.nicholaskoldys.collegetrackingapp.model.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SampleData {

    public static List<Term> loadSampleTerms() {

        List<Term> sampleTerms = new ArrayList<>();
        Calendar calendarDate = Calendar.getInstance();

        for(int i = 1; i <= 3; i++) {

            calendarDate.add(Calendar.MONTH, 1);
            sampleTerms.add(new Term(i, "TermExample : " + i, calendarDate, calendarDate));
        }

        return sampleTerms;
    }

    public static List<Course> loadSampleCourses() {

        List<Course> sampleCourses = new ArrayList<>();
        Calendar calendarDate = Calendar.getInstance();

        for(int i = 1; i <= 3; i++) {

            calendarDate.add(Calendar.MONTH, 1);
            sampleCourses.add(new Course(i, "CourseExample : " + i, i, calendarDate, calendarDate, i));
        }

        return sampleCourses;
    }

    public static List<Assessment> loadSampleAssessments() {

        List<Assessment> sampleAssessments = new ArrayList<>();
        Calendar calendarDate = Calendar.getInstance();

        for(int i = 1; i <= 2; i++) {
            sampleAssessments.add(new Assessment(i, "AssessmentExample : " + i, calendarDate, "Assessment Example Information", i));
        }

        return sampleAssessments;
    }

    public static List<Notes> loadSampleNotes() {

        List<Notes> sampleNotes = new ArrayList<>();

        for(int i = 1; i <= 3; i++) {
            sampleNotes.add(new Notes(i, "NoteExample : " + i, i, "Click Here to Change the Text of a Note"));
        }

        return sampleNotes;
    }

    public static List<Mentor> loadSampleMentors() {

        List<Mentor> sampleMentors = new ArrayList<>();

        for(int i = 1; i <= 2; i++) {
            sampleMentors.add(new Mentor(i, "Mentor " + i, "(615) 123 - 4567", "Mentor" + i + "@wgu.edu", i));
        }
        sampleMentors.add(new Mentor("Mentor", "(615) 123 - 4567", "Mentor@wgu.edu", 1));
        sampleMentors.add(new Mentor("Mentor", "(615) 123 - 4567", "Mentor@wgu.edu", 2));

        return sampleMentors;
    }
}

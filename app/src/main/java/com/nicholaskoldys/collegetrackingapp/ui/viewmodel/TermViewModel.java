package com.nicholaskoldys.collegetrackingapp.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nicholaskoldys.collegetrackingapp.database.AppRepository;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Term;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;

import java.util.Calendar;
import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Term>> termsList;
    private MutableLiveData<Term> selectedTerm;
    private LiveData<List<Course>> termsCoursesList;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        termsList = repository.getAllLiveTerms();
    }

    public LiveData<List<Term>> getTermsList() {
        return termsList;
    }

    public MutableLiveData<Term> getTerm(int termId) {
        loadSelectedItem(termId);
        return selectedTerm;
    }

    public LiveData<List<Course>> getTermsCoursesList(int termId) {
        loadSelectedItem(termId);
        return termsCoursesList;
    }

    private void loadSelectedItem(int termId) {
        //Log.i("MAIN", "Attempting to load selected Term " + termId);
        selectedTerm = new MutableLiveData<>();
        selectedTerm.postValue((Term) repository.getById(Constants.CollegeItemType.TERM_TYPE, termId));
        termsCoursesList = repository.getLiveCoursesFromTerm(termId);
    }

    public void submitTerm(String title, Calendar start, Calendar end) {

        repository.insert(Constants.CollegeItemType.TERM_TYPE, new Term(title, start, end));
    }

    public void updateTerm(int termId, String title, Calendar start, Calendar end) {

        Term term = (Term) repository.getById(Constants.CollegeItemType.TERM_TYPE, termId);
        term.setTitle(title);
        term.setStartDate(start);
        term.setEndDate(end);
        repository.update(Constants.CollegeItemType.TERM_TYPE, term);
    }

    public void deleteTerm(int termId) {

        Term term = (Term) repository.getById(Constants.CollegeItemType.TERM_TYPE, termId);
        repository.delete(Constants.CollegeItemType.TERM_TYPE, term);
    }
}

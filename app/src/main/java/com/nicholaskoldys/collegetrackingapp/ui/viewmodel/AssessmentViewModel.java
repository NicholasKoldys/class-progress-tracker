package com.nicholaskoldys.collegetrackingapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nicholaskoldys.collegetrackingapp.database.AppRepository;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.Goal;
import com.nicholaskoldys.collegetrackingapp.utility.AlarmBuilder;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.UniqueIdentifierGenerator;

import java.util.Calendar;
import java.util.List;

import static com.nicholaskoldys.collegetrackingapp.utility.Constants.NotificationType.ASSESSMENT_DUE_NOTIFICATION_CODE;

public class AssessmentViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Assessment>> assessmentsList;
    private MutableLiveData<Assessment> selectedAssessment;
    private LiveData<List<Goal>> assessmentGoalsList;
    private MutableLiveData<Goal> selectedGoal;


    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        assessmentsList = repository.getAllLiveAssessments();
    }

    private void loadSelectedAssessment(int assessmentId) {

        selectedAssessment = new MutableLiveData<>();
        selectedAssessment.postValue((Assessment) repository.getById(Constants.CollegeItemType.ASSESSMENT_TYPE, assessmentId));
        assessmentGoalsList = repository.getLiveGoalsFromAssessment(assessmentId);
    }

    public MutableLiveData<Assessment> getAssessment(int assessmentId) {
        loadSelectedAssessment(assessmentId);
        return selectedAssessment;
    }

    public LiveData<List<Assessment>> getAssessmentsList() {
        return assessmentsList;
    }

    public LiveData<List<Goal>> getAssessmentGoalsList(int assessmentId) {
        loadSelectedAssessment(assessmentId);
        return assessmentGoalsList;
    }

    public void submitAssessment(String title, Calendar dueDate, String info, int courseId, Boolean isAlertEnabled) {

        Assessment newAssessment = new Assessment(title, dueDate, info, courseId);
        repository.insert(Constants.CollegeItemType.ASSESSMENT_TYPE, newAssessment);
        if(isAlertEnabled) {
            scheduleAssessmentDueNotification(newAssessment);
        }
    }

    public void updateAssessment(int assessmentId, String title, Calendar dueDate, String info, Boolean isAlertEnabled) {

        Assessment assessment = (Assessment) repository.getById(Constants.CollegeItemType.ASSESSMENT_TYPE, assessmentId);
        assessment.setTitle(title);
        assessment.setDueDate(dueDate);
        assessment.setInfo(info);
        if(isAlertEnabled) {
            scheduleAssessmentDueNotification(assessment);
        } else {
            cancelAssessmentDueNotification(assessment);
        }
        repository.update(Constants.CollegeItemType.ASSESSMENT_TYPE, assessment);
    }

    public void deleteAssessment(int assessmentId) {

        Assessment assessment = (Assessment) repository.getById(Constants.CollegeItemType.ASSESSMENT_TYPE, assessmentId);
        cancelAssessmentDueNotification(assessment);
        repository.delete(Constants.CollegeItemType.ASSESSMENT_TYPE, assessment);
    }

    private void scheduleAssessmentDueNotification(Assessment assessment) {

        int uniqueId = UniqueIdentifierGenerator.generateUniqueId(assessment.getId(), ASSESSMENT_DUE_NOTIFICATION_CODE, ASSESSMENT_DUE_NOTIFICATION_CODE);

        AlarmBuilder.setAlarm(getApplication(), Constants.CollegeItemType.ASSESSMENT_TYPE, assessment.getDueDate(), assessment.getTitle(), uniqueId);
    }

    private void cancelAssessmentDueNotification(Assessment assessment) {

        int uniqueId = UniqueIdentifierGenerator.generateUniqueId(assessment.getId(), ASSESSMENT_DUE_NOTIFICATION_CODE, ASSESSMENT_DUE_NOTIFICATION_CODE);

        AlarmBuilder.cancelAlarm(getApplication(), Constants.CollegeItemType.ASSESSMENT_TYPE, assessment.getDueDate(), assessment.getTitle(), uniqueId);
    }




    private void loadSelectedGoal(int goalId) {

        selectedGoal = new MutableLiveData<>();
        selectedGoal.postValue((Goal) repository.getById(Constants.CollegeItemType.GOAL_TYPE, goalId));
    }

    public MutableLiveData<Goal> getSelectedGoal(int goalId) {
        loadSelectedGoal(goalId);
        return selectedGoal;
    }

    private Goal getGoal(int goalId) {
        return (Goal)repository.getById(Constants.CollegeItemType.GOAL_TYPE, goalId);
    }

    public void submitGoal(final int id, final String title, final Calendar dateAndTime, final int assessmentId, @Constants.NotificationType final int notificationId) {

        final int uniqueId = UniqueIdentifierGenerator.generateUniqueId(assessmentId, id, notificationId);

        repository.insert(Constants.CollegeItemType.GOAL_TYPE, new Goal(id, title, uniqueId, dateAndTime, assessmentId));
        scheduleGoalNotification(getGoal(id));
    }

    public void updateGoal(int goalId, String title, Calendar dateAndTime) {

        Goal goal = getGoal(goalId);
        goal.setTitle(title);
        goal.setDateAndTime(dateAndTime);
        repository.update(Constants.CollegeItemType.GOAL_TYPE, goal);
        scheduleGoalNotification(goal);
    }

    public void deleteGoal(int goalId) {

        Goal goal = getGoal(goalId);
        cancelGoalNotification(goal);
        repository.delete(Constants.CollegeItemType.GOAL_TYPE, goal);
    }

    private void scheduleGoalNotification(Goal goal) {

        AlarmBuilder.setAlarm(getApplication(), Constants.CollegeItemType.GOAL_TYPE, goal.getDateAndTime(), goal.getTitle(), goal.getUniqueId());
    }

    private void cancelGoalNotification(Goal goal) {

        AlarmBuilder.cancelAlarm(getApplication(), Constants.CollegeItemType.GOAL_TYPE, goal.getDateAndTime(), goal.getTitle(), goal.getUniqueId());
    }
}

package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Goal;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.AssessmentViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.Formatters;

import java.text.DateFormat;
import java.util.Calendar;

public class GoalSetDialogueFragment extends DialogFragment {

    private static final String PARENT_OR_CHILD_ID = "parentOrChildId";
    private static final String IS_NEW_GOAL = "isNewGoal";
    private static final String GOAL_COUNT = "goalCount";
    private static final String NOTIFICATION_CODE = "notificationCode";
    private int parentAssessmentId;
    private int goalId = 0;
    private int goalCount;
    private @Constants.NotificationType int notificationCode;
    private Boolean isNewGoal;

    private Calendar adjustedDate;
    private Calendar adjustedTime;

    private AssessmentViewModel viewModel;

    private TextInputLayout goalTitleLayout;
    private TextInputLayout goalDateLayout;
    private TextInputLayout goalTimeLayout;
    private Button goalCancelButton;
    private Button goalDeleteButton;
    private Button goalConfirmButton;
    private Button goalCreateButton;


    public static GoalSetDialogueFragment newInstance(int parentOrChildId, int goalCount, Boolean isNewGoal, @Constants.NotificationType int notificationCode) {
         Bundle args = new Bundle();
         args.putInt(PARENT_OR_CHILD_ID, parentOrChildId);
         args.putInt(GOAL_COUNT, goalCount);
         args.putBoolean(IS_NEW_GOAL, isNewGoal);
         args.putInt(NOTIFICATION_CODE, notificationCode);
         GoalSetDialogueFragment fragment = new GoalSetDialogueFragment();
         fragment.setArguments(args);
         return fragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        isNewGoal = args.getBoolean(IS_NEW_GOAL);
        goalCount = args.getInt(GOAL_COUNT);
        notificationCode = args.getInt(NOTIFICATION_CODE);
        if(isNewGoal) {
            parentAssessmentId = args.getInt(PARENT_OR_CHILD_ID);
        } else {
            goalId = args.getInt(PARENT_OR_CHILD_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_dialogue_goaledit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View goalEditDialogueFragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(goalEditDialogueFragment, savedInstanceState);

        initViewObjects(goalEditDialogueFragment);
        setupOnClickListeners();
        initViewModel();
        applyDisplayDataViews();
    }

    private void initViewObjects(View goalEditDialogueFragment) {

        goalTitleLayout = goalEditDialogueFragment.findViewById(R.id.goalEditTitleLayout);
        goalDateLayout = goalEditDialogueFragment.findViewById(R.id.goalEditDateLayout);
        goalTimeLayout = goalEditDialogueFragment.findViewById(R.id.goalEditTimeLayout);
        goalCancelButton = goalEditDialogueFragment.findViewById(R.id.goalEditCancelButton);
        goalDeleteButton = goalEditDialogueFragment.findViewById(R.id.goalEditDeleteButton);
        goalConfirmButton = goalEditDialogueFragment.findViewById(R.id.goalEditConfirmButton);
        goalCreateButton = goalEditDialogueFragment.findViewById(R.id.goalEditCreateButton);
    }

    private void setupOnClickListeners() {

        goalDateLayout.getEditText().setInputType(InputType.TYPE_NULL);
        goalDateLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                calendarPickerFragment.setTargetFragment(GoalSetDialogueFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                calendarPickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });
        goalDateLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Bundle args = new Bundle();
                    args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                    calendarPickerFragment.setTargetFragment(GoalSetDialogueFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    calendarPickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
                }
            }
        });

        goalTimeLayout.getEditText().setInputType(InputType.TYPE_NULL);
        goalTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putInt(TimeDialogueFragment.TIME_REQUEST_CODE, TimeDialogueFragment.TIME_PICKER_TTTT);
                DialogFragment timePickerFragment = TimeDialogueFragment.newInstance(args);

                timePickerFragment.setTargetFragment(GoalSetDialogueFragment.this, TimeDialogueFragment.TIME_PICKER_TTTT);
                timePickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });
        goalTimeLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Bundle args = new Bundle();
                    args.putInt(TimeDialogueFragment.TIME_REQUEST_CODE, TimeDialogueFragment.TIME_PICKER_TTTT);
                    DialogFragment timePickerFragment = TimeDialogueFragment.newInstance(args);

                    timePickerFragment.setTargetFragment(GoalSetDialogueFragment.this, TimeDialogueFragment.TIME_PICKER_TTTT);
                    timePickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
                }
            }
        });

        goalConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!goalTitleLayout.getEditText().getText().toString().equals("")
                        && !goalTimeLayout.getEditText().getText().toString().equals("")
                        && !goalDateLayout.getEditText().getText().toString().equals("")) {

                    Calendar newDateAndTime = Calendar.getInstance();
                    newDateAndTime.set(adjustedDate.get(Calendar.YEAR), adjustedDate.get(Calendar.MONTH), adjustedDate.get(Calendar.DAY_OF_MONTH), adjustedTime.get(Calendar.HOUR_OF_DAY), adjustedTime.get(Calendar.MINUTE));

                    String goalTitle = goalTitleLayout.getEditText().getText().toString();

                    ((OnSelectGoalListener) getTargetFragment()).updateGoal(goalId, goalTitle, newDateAndTime);

                    getDialog().dismiss();

                } else {
                    Toast.makeText(getContext(), "Please input Data before submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goalDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(goalId != 0) {
                    ((OnSelectGoalListener) getTargetFragment()).deleteSelectedGoal(goalId);

                    getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), "Please Select a Goal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goalCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!goalTitleLayout.getEditText().getText().toString().equals("")
                    && !goalTimeLayout.getEditText().getText().toString().equals("")
                    && !goalDateLayout.getEditText().getText().toString().equals("")) {

                    Calendar newDateAndTime = Calendar.getInstance();
                    newDateAndTime.set(adjustedDate.get(Calendar.YEAR), adjustedDate.get(Calendar.MONTH), adjustedDate.get(Calendar.DAY_OF_MONTH), adjustedTime.get(Calendar.HOUR_OF_DAY), adjustedTime.get(Calendar.MINUTE));

                    int goalId = goalCount + 1;
                    String goalTitle = goalTitleLayout.getEditText().getText().toString();

                    ((OnSelectGoalListener) getTargetFragment()).createNewGoal(goalId, goalTitle, newDateAndTime);

                    getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), "Please input Data before submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goalCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private void initViewModel() {

        viewModel = ViewModelProviders.of(GoalSetDialogueFragment.this).get(AssessmentViewModel.class);
    }

    private void applyDisplayDataViews() {

        if(!isNewGoal) {

            viewModel.getSelectedGoal(goalId).observe(getViewLifecycleOwner(), new Observer<Goal>() {
                @Override
                public void onChanged(Goal goal) {
                    goalTitleLayout.getEditText().setText(goal.getTitle());
                    goalDateLayout.getEditText().setText(goal.getDateString());
                    goalTimeLayout.getEditText().setText(goal.getTimeString());

                    Calendar calendar = goal.getDateAndTime();
                    Calendar date = Calendar.getInstance();
                    date.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    Calendar time = Calendar.getInstance();
                    time.setTime(calendar.getTime());

                    adjustedDate = date;
                    adjustedTime = time;
                }
            });
        } else {

            goalConfirmButton.setVisibility(View.INVISIBLE);
            goalDeleteButton.setVisibility(View.INVISIBLE);
            goalCreateButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY) {

                adjustedDate = Calendar.getInstance();
                Bundle calendarBreakdown = data.getBundleExtra(DateDialogueFragment.SELECTED_STARTDATE);
                if(calendarBreakdown != null) {
                    adjustedDate.set(calendarBreakdown.getInt(DateDialogueFragment.DATE_PICKER_YEAR), calendarBreakdown.getInt(DateDialogueFragment.DATE_PICKER_MONTH), calendarBreakdown.getInt(DateDialogueFragment.DATE_PICKER_DAY_OF_MONTH));
                    goalDateLayout.getEditText().setText(Formatters.calendarToMMDDYYYY(adjustedDate));
                }
            }

            if (requestCode == TimeDialogueFragment.TIME_PICKER_TTTT) {

                adjustedTime = Calendar.getInstance();
                Bundle calendarBreakdown = data.getBundleExtra(TimeDialogueFragment.SELECTED_TIME);
                if(calendarBreakdown != null) {
                    adjustedTime.set(Calendar.HOUR_OF_DAY, calendarBreakdown.getInt(TimeDialogueFragment.TIME_PICKER_HOUROFDAY));
                    adjustedTime.set(Calendar.MINUTE, calendarBreakdown.getInt(TimeDialogueFragment.TIME_PICKER_MINUTE));
                    adjustedTime.set(Calendar.SECOND, 0);
                    goalTimeLayout.getEditText().setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(adjustedTime.getTime()));
                }
            }
        }
    }

    interface OnSelectGoalListener {
        void deleteSelectedGoal(int goalId);
        void createNewGoal(int goalId, String title, Calendar alarmDateTime);
        void updateGoal(int goalId, String title, Calendar alarmDateTime);
    }
}
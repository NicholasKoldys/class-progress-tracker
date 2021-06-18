package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.nicholaskoldys.collegetrackingapp.MainActivity;
import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Goal;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.AssessmentViewModel;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.CourseViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.Formatters;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CourseEditFragment extends Fragment implements ListDialogueFragment.OnSelectDLTListen {

    private static final String PARENT_OR_CHILD_ID = "parentORChildId";
    private static final String IS_NEW_COURSE = "isNewCourse";

    private int courseId;
    private int parentTermId;
    private Boolean isNewCourse;
    private int assessmentsPerCourseCount = 0;
    private Calendar adjustedStartDate;
    private Calendar adjustedEndDate;

    private TextInputLayout courseTitleTextInputLayout;
    private TextInputLayout courseStartTextInputLayout;
    private TextInputLayout courseEndTextInputLayout;
    private Spinner courseStatusEditSpinner;

    private Switch courseStartSwitch;
    private Switch courseEndSwitch;

    private TextView courseAssessmentEditLabel;
    private TextView courseMentorEditLabel;

    private Button courseAddAssessmentButton;
    private Button courseDeleteAssessmentButton;
    private Button courseAddMentorButton;
    private Button courseEditMentorButton;
    private Button courseDeleteMentorButton;
    private FloatingActionButton courseConfirmFAB;
    private FloatingActionButton courseDeleteFAB;

    private CourseViewModel courseViewModel;


    public static CourseEditFragment newInstance(int parentOrChildId, Boolean isNewCourse) {
        //Log.i("MAIN", "Creating a Course Edit Fragment with parentOrChildId " + parentOrChildId + " | isNewCourse " + isNewCourse);
        Bundle args = new Bundle();
        args.putInt(PARENT_OR_CHILD_ID, parentOrChildId);
        args.putBoolean(IS_NEW_COURSE, isNewCourse);
        CourseEditFragment courseEditFragment = new CourseEditFragment();
        courseEditFragment.setArguments(args);
        return courseEditFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
         isNewCourse = args.getBoolean(IS_NEW_COURSE);

         if(isNewCourse) {
             parentTermId = args.getInt(PARENT_OR_CHILD_ID);
         } else {
             courseId = args.getInt(PARENT_OR_CHILD_ID);
         }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_content_edit_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View courseEditFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(courseEditFragmentView, savedInstanceState);

        initViewObjects(courseEditFragmentView);
        setupOnClickListeners();
        initViewModel();
        applyDisplayDataViews();
    }

    private void initViewObjects(View courseEditFragmentView) {

        courseTitleTextInputLayout = courseEditFragmentView.findViewById(R.id.courseTitleTextInputLayout);
        courseStartTextInputLayout = courseEditFragmentView.findViewById(R.id.courseStartTextInputLayout);
        courseEndTextInputLayout = courseEditFragmentView.findViewById(R.id.courseEndTextInputLayout);
        courseStatusEditSpinner = courseEditFragmentView.findViewById(R.id.courseStatusEditSpinner);

        courseStartSwitch = courseEditFragmentView.findViewById(R.id.courseEditStartSwitch);
        courseEndSwitch = courseEditFragmentView.findViewById(R.id.courseEditEndSwitch);

        courseAssessmentEditLabel = courseEditFragmentView.findViewById(R.id.courseAssessmentEditLabel);
        courseAddAssessmentButton = courseEditFragmentView.findViewById(R.id.courseAddAssessmentButton);
        courseDeleteAssessmentButton = courseEditFragmentView.findViewById(R.id.courseDeleteAssessmentButton);

        courseMentorEditLabel = courseEditFragmentView.findViewById(R.id.courseMentorEditLabel);
        courseAddMentorButton = courseEditFragmentView.findViewById(R.id.courseAddMentorButton);
        courseEditMentorButton = courseEditFragmentView.findViewById(R.id.courseEditMentorButton);
        courseDeleteMentorButton = courseEditFragmentView.findViewById(R.id.courseDeleteMentorButton);

        courseConfirmFAB = courseEditFragmentView.findViewById(R.id.courseConfirmFAB);
        courseDeleteFAB = courseEditFragmentView.findViewById(R.id.courseEditDeleteFAB);
    }

    private void setupOnClickListeners() {

        courseStartTextInputLayout.getEditText().setInputType(InputType.TYPE_NULL);
        courseStartTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                Bundle args = new Bundle();
                args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                calendarPickerFragment.setTargetFragment(CourseEditFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                calendarPickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });
        courseStartTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                    Bundle args = new Bundle();
                    args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                    calendarPickerFragment.setTargetFragment(CourseEditFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    calendarPickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
                }
            }
        });

        courseEndTextInputLayout.getEditText().setInputType(InputType.TYPE_NULL);
        courseEndTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                Bundle args = new Bundle();
                args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                calendarPickerFragment.setTargetFragment(CourseEditFragment.this, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                calendarPickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });
        courseEndTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    FragmentTransaction transaction =
                            MainActivity.getTransactionForDialog(getActivity());
                    Bundle args = new Bundle();
                    args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                    DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);
                    calendarPickerFragment.setTargetFragment(CourseEditFragment.this, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                    calendarPickerFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
                }
            }
        });

        courseAddAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(assessmentsPerCourseCount < 5) {
                    FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                    AssessmentEditFragment assessmentEditFragment = AssessmentEditFragment.newInstance(courseId, true);
                    transaction.replace(R.id.fragmentViewHolder, assessmentEditFragment).addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "A Course can have MAX 5 Assessments", Toast.LENGTH_SHORT).show();
                }
            }
        });

        courseDeleteAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                ListDialogueFragment listDialogueFragment = ListDialogueFragment.newInstance(Constants.CollegeItemType.ASSESSMENT_TYPE, courseId);
                listDialogueFragment.setTargetFragment(CourseEditFragment.this, 0);
                listDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        courseAddMentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                MentorDialogueFragment mentorDialogueFragment = MentorDialogueFragment.newInstance(courseId, true);
                mentorDialogueFragment.setTargetFragment(CourseEditFragment.this, 0);
                mentorDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        courseEditMentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                MentorDialogueFragment mentorDialogueFragment = MentorDialogueFragment.newInstance(courseId, false);
                mentorDialogueFragment.setTargetFragment(CourseEditFragment.this, 0);
                mentorDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        courseDeleteMentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                ListDialogueFragment listDialogueFragment = ListDialogueFragment.newInstance(Constants.CollegeItemType.MENTOR_TYPE, courseId);
                listDialogueFragment.setTargetFragment(CourseEditFragment.this, 0);
                listDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        courseConfirmFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!courseTitleTextInputLayout.getEditText().getText().toString().equals("")
                    && !courseStartTextInputLayout.getEditText().getText().toString().equals("")
                    && !courseEndTextInputLayout.getEditText().getText().toString().equals("")) {

                    String title = courseTitleTextInputLayout.getEditText().getText().toString();
                    @Constants.CourseStatus int status =  Constants.courseStatusStringToInt(courseStatusEditSpinner.getSelectedItem().toString());

                    Boolean isStartAlertEnabled = courseStartSwitch.isChecked();
                    Boolean isEndAlertEnabled = courseEndSwitch.isChecked();

                    if (isNewCourse) {
                        courseViewModel.submitCourse(title, adjustedStartDate, adjustedEndDate, status, parentTermId, isStartAlertEnabled, isEndAlertEnabled);
                    } else {
                        courseViewModel.updateCourse(courseId, title, adjustedStartDate, adjustedEndDate, status, isStartAlertEnabled, isEndAlertEnabled);
                    }

                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Please input Data before submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //NOT IN USE ATM
//        courseDeleteFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                courseViewModel.deleteCourse(courseId);
//                getActivity().getSupportFragmentManager().popBackStack("DETAILED_COURSE", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            }
//        });
    }

    private void initViewModel() {

        courseViewModel = ViewModelProviders.of(CourseEditFragment.this).get(CourseViewModel.class);
    }

    private void applyDisplayDataViews() {

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Constants.getCourseStatusArray());
        courseStatusEditSpinner.setAdapter(spinnerAdapter);

        if(!isNewCourse) {
            courseViewModel.getCourse(courseId).observe(getViewLifecycleOwner(), new Observer<Course>() {
                @Override
                public void onChanged(Course course) {

                    courseTitleTextInputLayout.getEditText().setText(course.getTitle());
                    courseStartTextInputLayout.getEditText().setText(course.getStartDateString());
                    courseEndTextInputLayout.getEditText().setText(course.getEndDateString());
                    courseStatusEditSpinner.setSelection(course.getStatus());

                    adjustedStartDate = course.getStartDate();
                    adjustedEndDate = course.getEndDate();

                    courseViewModel.getCourseAssessmentsList(courseId).observe(getViewLifecycleOwner(), new Observer<List<Assessment>>() {
                        @Override
                        public void onChanged(List<Assessment> assessments) {
                            assessmentsPerCourseCount = assessments.size();
                        }
                    });
                }
            });
        } else {
            courseAssessmentEditLabel.setVisibility(View.INVISIBLE);
            courseMentorEditLabel.setVisibility(View.INVISIBLE);
            courseAddAssessmentButton.setVisibility(View.INVISIBLE);
            courseDeleteAssessmentButton.setVisibility(View.INVISIBLE);
            courseAddMentorButton.setVisibility(View.INVISIBLE);
            courseEditMentorButton.setVisibility(View.INVISIBLE);
            courseDeleteMentorButton.setVisibility(View.INVISIBLE);
            courseDeleteFAB.hide();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY) {

                adjustedStartDate = Calendar.getInstance();
                Bundle calendarBreakdown = data.getBundleExtra(DateDialogueFragment.SELECTED_STARTDATE);
                if(calendarBreakdown != null) {
                    adjustedStartDate.set(calendarBreakdown.getInt("YEAR", 0), calendarBreakdown.getInt("MONTH", 0), calendarBreakdown.getInt("DAY", 0));
                    adjustedStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    adjustedStartDate.set(Calendar.MINUTE, 0);
                    adjustedStartDate.set(Calendar.SECOND, 0);
                    courseStartTextInputLayout.getEditText().setText(Formatters.calendarToMMDDYYYY(adjustedStartDate));
                }
            }

            if (requestCode == DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY) {

                adjustedEndDate = Calendar.getInstance();
                Bundle calendarBreakdown = data.getBundleExtra(DateDialogueFragment.SELECTED_ENDDATE);
                if(calendarBreakdown != null) {
                    adjustedEndDate.set(calendarBreakdown.getInt("YEAR", 0), calendarBreakdown.getInt("MONTH", 0), calendarBreakdown.getInt("DAY", 0));
                    adjustedEndDate.set(Calendar.HOUR_OF_DAY, 0);
                    adjustedEndDate.set(Calendar.MINUTE, 0);
                    adjustedEndDate.set(Calendar.SECOND, 0);
                    courseEndTextInputLayout.getEditText().setText(Formatters.calendarToMMDDYYYY(adjustedEndDate));
                }
            }
        }
    }

    @Override
    public void onDeleteCallback(@Constants.CollegeItemType int itemType, final int id) {

        if(itemType == Constants.CollegeItemType.ASSESSMENT_TYPE) {
            final AssessmentViewModel assessmentViewModel = ViewModelProviders.of(CourseEditFragment.this).get(AssessmentViewModel.class);

            assessmentViewModel.getAssessmentGoalsList(id).observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
                @Override
                public void onChanged(List<Goal> goals) {

                    Iterator<Goal> itr1 = goals.iterator();

                    while (itr1.hasNext()) {
                        assessmentViewModel.deleteGoal(itr1.next().getId());
                    }
                }
            });
            assessmentViewModel.deleteAssessment(id);
        }

        if(itemType == Constants.CollegeItemType.MENTOR_TYPE) {
            courseViewModel.deleteMentor(id);
        }
    }
}
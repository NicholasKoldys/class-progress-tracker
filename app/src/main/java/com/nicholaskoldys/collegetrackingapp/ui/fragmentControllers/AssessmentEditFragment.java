package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.nicholaskoldys.collegetrackingapp.MainActivity;
import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.AssessmentViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.Formatters;

import java.util.Calendar;

public class AssessmentEditFragment extends Fragment {

    private static final String PARENT_OR_CHILD_ID = "parentORChildId";
    private static final String IS_NEW_ASSESSMENT = "isNewAssessment";

    private int assessmentId;
    private int parentCourseId;
    private Boolean isNewAssessment;
    private Calendar adjustedDueDate;

    private TextInputLayout assessmentTitleTextInputLayout;
    private TextInputLayout assessmentDueDateTextInputLayout;

    private EditText assessmentInformationEditText;
    private Switch dueDateAlertSwitch;
    private FloatingActionButton assessmentSubmitFAB;

    private AssessmentViewModel assessmentViewModel;


    public static AssessmentEditFragment newInstance(int parentOrChildId, Boolean isNewAssessment) {
        //Log.i("MAIN", "Creating a Assessment Edit Fragment with parentOrChildId " + parentOrChildId + " | isNewAssessment " + isNewAssessment);
        Bundle args = new Bundle();
        args.putInt(PARENT_OR_CHILD_ID, parentOrChildId);
        args.putBoolean(IS_NEW_ASSESSMENT, isNewAssessment);
        AssessmentEditFragment assessmentEditFragment = new AssessmentEditFragment();
        assessmentEditFragment.setArguments(args);
        return assessmentEditFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

        isNewAssessment = args.getBoolean(IS_NEW_ASSESSMENT);

        if(isNewAssessment) {
            parentCourseId = args.getInt(PARENT_OR_CHILD_ID);
        } else {
            assessmentId = args.getInt(PARENT_OR_CHILD_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_content_edit_assessment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View assessmentEditFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(assessmentEditFragmentView, savedInstanceState);

        initViewObjects(assessmentEditFragmentView);
        setupOnClickListeners();
        initViewModel();
        applyDisplayDataViews();
    }

    private void initViewObjects(View assessmentEditFragmentView) {

        assessmentTitleTextInputLayout = assessmentEditFragmentView.findViewById(R.id.assessmentTitleTextInputLayout);
        assessmentDueDateTextInputLayout = assessmentEditFragmentView.findViewById(R.id.assessmentDueDateTextInputLayout);

        assessmentInformationEditText = assessmentEditFragmentView.findViewById(R.id.assessmentInformationEditText);
        dueDateAlertSwitch = assessmentEditFragmentView.findViewById(R.id.assessmentEditDueAlertSwitch);

        assessmentSubmitFAB = assessmentEditFragmentView.findViewById(R.id.assessmentSubmitFAB);
    }

    private void setupOnClickListeners() {

        assessmentDueDateTextInputLayout.getEditText().setInputType(InputType.TYPE_NULL);
        assessmentDueDateTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                calendarPickerFragment.setTargetFragment(AssessmentEditFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                calendarPickerFragment.show(MainActivity.getTransactionForDialog(getActivity()), Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        assessmentDueDateTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Bundle args = new Bundle();
                    args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                    calendarPickerFragment.setTargetFragment(AssessmentEditFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    calendarPickerFragment.show(MainActivity.getTransactionForDialog(getActivity()), Constants.FRAGMENT_DIALOG_TAG);
                }
            }
        });

        assessmentSubmitFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!assessmentTitleTextInputLayout.getEditText().getText().toString().equals("")
                    && !assessmentDueDateTextInputLayout.getEditText().getText().toString().equals("")
                    && !assessmentTitleTextInputLayout.getEditText().getText().toString().equals("")) {

                    String title = assessmentTitleTextInputLayout.getEditText().getText().toString();
                    String info = assessmentInformationEditText.getText().toString();

                    Boolean isAlertEnabled = dueDateAlertSwitch.isChecked();

                    if(isNewAssessment) {
                        assessmentViewModel.submitAssessment(
                                title,
                                adjustedDueDate,
                                info,
                                parentCourseId,
                                isAlertEnabled
                        );
                    } else {
                        assessmentViewModel.updateAssessment(
                                assessmentId,
                                title,
                                adjustedDueDate,
                                info,
                                isAlertEnabled
                        );
                    }

                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Please input data to submit.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViewModel() {

        assessmentViewModel = ViewModelProviders.of(AssessmentEditFragment.this).get(AssessmentViewModel.class);
    }

    private void applyDisplayDataViews() {

        if(!isNewAssessment) {
            assessmentViewModel.getAssessment(assessmentId).observe(getViewLifecycleOwner(), new Observer<Assessment>() {
                @Override
                public void onChanged(Assessment assessment) {
                    assessmentTitleTextInputLayout.getEditText().setText(assessment.getTitle());
                    assessmentDueDateTextInputLayout.getEditText().setText(assessment.getDueDateString());
                    assessmentInformationEditText.setText(assessment.getInfo());
                    adjustedDueDate = assessment.getDueDate();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY) {

                adjustedDueDate = Calendar.getInstance();
                Bundle calendarBreakdown = data.getBundleExtra(DateDialogueFragment.SELECTED_STARTDATE);
                adjustedDueDate.set(
                        calendarBreakdown.getInt("YEAR", 0),
                        calendarBreakdown.getInt("MONTH", 0),
                        calendarBreakdown.getInt("DAY", 0),
                        0,
                        0
                );
                assessmentDueDateTextInputLayout.getEditText()
                        .setText(Formatters.calendarToMMDDYYYY(adjustedDueDate));
            }
        }
    }
}

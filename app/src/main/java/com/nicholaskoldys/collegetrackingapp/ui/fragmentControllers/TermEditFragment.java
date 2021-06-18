package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.nicholaskoldys.collegetrackingapp.model.Term;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.AssessmentViewModel;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.CourseViewModel;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.TermViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.Formatters;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class TermEditFragment extends Fragment implements ListDialogueFragment.OnSelectDLTListen {

    private static final String TERM_ID = "termId";
    private static final String IS_NEW_TERM = "isNewTerm";

    private int termId = 0;
    private Boolean isNewTerm;
    private Boolean isTermEmpty = false;
    private Calendar adjustedStartDate;
    private Calendar adjustedEndDate;

    private TextInputLayout termTitleTextInputLayout;
    private TextInputLayout termStartTextInputLayout;
    private TextInputLayout termEndTextInputLayout;
    private TextView termCourseLabel;

    private Button termAddCourseButton;
    private Button termDeleteCourseButton;
    private FloatingActionButton termConfirmFAB;
    private FloatingActionButton termDeleteFAB;

    private TermViewModel termViewModel;


    public static TermEditFragment newInstance(int termId, Boolean isNewTerm) {
        //Log.i("MAIN", "Creating a Term Edit Fragment with termId " + termId + " | isNewTerm " + isNewTerm);
        Bundle args = new Bundle();
        args.putInt(TERM_ID, termId);
        args.putBoolean(IS_NEW_TERM, isNewTerm);
        TermEditFragment termEditFragment = new TermEditFragment();
        termEditFragment.setArguments(args);
        return termEditFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        isNewTerm = args.getBoolean(IS_NEW_TERM);
        if(!isNewTerm) {
            termId = args.getInt(TERM_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_content_edit_term, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View termEditFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(termEditFragmentView, savedInstanceState);

        initViewObjects(termEditFragmentView);
        setupOnClickListeners();
        initViewModel();
        applyDisplayDataViews();
    }

    private void initViewObjects(View termEditFragmentView) {

        termTitleTextInputLayout = termEditFragmentView.findViewById(R.id.termTitleTextInputLayout);
        termStartTextInputLayout = termEditFragmentView.findViewById(R.id.termStartTextInputLayout);
        termEndTextInputLayout = termEditFragmentView.findViewById(R.id.termEndTextInputLayout);

        termCourseLabel = termEditFragmentView.findViewById(R.id.termsCourseAddLabel);
        termAddCourseButton = termEditFragmentView.findViewById(R.id.termAddCourseButton);
        termDeleteCourseButton = termEditFragmentView.findViewById(R.id.termDeleteCourseButton);

        termConfirmFAB = termEditFragmentView.findViewById(R.id.termConfirmFAB);
        termDeleteFAB = termEditFragmentView.findViewById(R.id.termEditDeleteFAB);
    }

    private void setupOnClickListeners() {

        termStartTextInputLayout.getEditText().setInputType(InputType.TYPE_NULL);
        termStartTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                calendarPickerFragment.setTargetFragment(TermEditFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                calendarPickerFragment.show(MainActivity.getTransactionForDialog(getActivity()), Constants.FRAGMENT_DIALOG_TAG);
            }
        });
        termStartTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Bundle args = new Bundle();
                    args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                    calendarPickerFragment.setTargetFragment(TermEditFragment.this, DateDialogueFragment.STARTDATE_PICKER_MMDDYYYY);
                    calendarPickerFragment.show(MainActivity.getTransactionForDialog(getActivity()), Constants.FRAGMENT_DIALOG_TAG);
                }
            }
        });

        termEndTextInputLayout.getEditText().setInputType(InputType.TYPE_NULL);
        termEndTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                calendarPickerFragment.setTargetFragment(TermEditFragment.this, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                calendarPickerFragment.show(MainActivity.getTransactionForDialog(getActivity()), Constants.FRAGMENT_DIALOG_TAG);
            }
        });
        termEndTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Bundle args = new Bundle();
                    args.putInt(DateDialogueFragment.DATE_REQUEST_CODE, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                    DialogFragment calendarPickerFragment = DateDialogueFragment.newInstance(args);

                    calendarPickerFragment.setTargetFragment(TermEditFragment.this, DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY);
                    calendarPickerFragment.show(MainActivity.getTransactionForDialog(getActivity()), Constants.FRAGMENT_DIALOG_TAG);
                }
            }
        });

        termAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                CourseEditFragment courseEditFragment = CourseEditFragment.newInstance(termId, true);
                transaction.replace(R.id.fragmentViewHolder, courseEditFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        termDeleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogueFragment listDialogueFragment = ListDialogueFragment.newInstance(Constants.CollegeItemType.COURSE_TYPE, termId);
                listDialogueFragment.setTargetFragment(TermEditFragment.this, 0);
                listDialogueFragment.show(MainActivity.getTransactionForDialog(getActivity()), Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        termConfirmFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!termTitleTextInputLayout.getEditText().getText().toString().equals("")
                        && !termStartTextInputLayout.getEditText().getText().toString().equals("")
                        && !termEndTextInputLayout.getEditText().getText().toString().equals("")
                        && adjustedStartDate != null
                        && adjustedEndDate != null) {

                    if (isNewTerm) {

                        termViewModel.submitTerm(termTitleTextInputLayout.getEditText().getText().toString(), adjustedStartDate, adjustedEndDate);
                    } else {
                        termViewModel.updateTerm(termId, termTitleTextInputLayout.getEditText().getText().toString(), adjustedStartDate, adjustedEndDate);
                    }

                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Please input Data before submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });

        termDeleteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTermEmpty) {
                    termViewModel.deleteTerm(termId);
                    getActivity().getSupportFragmentManager().popBackStack("DETAILED_TERM", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    Toast.makeText(getContext(), "Term has course, please remove before deleting", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViewModel() {

        termViewModel = ViewModelProviders.of(TermEditFragment.this).get(TermViewModel.class);
    }

    private void applyDisplayDataViews() {

        if(!isNewTerm) {

            //Log.i("MAIN", "Applying TermViewModel to view. Termid = " + termId);
            termViewModel.getTerm(termId).observe(TermEditFragment.this, new Observer<Term>() {
                @Override
                public void onChanged(Term term) {
                    termTitleTextInputLayout.getEditText().setText(term.getTitle());
                    termStartTextInputLayout.getEditText().setText(term.getStartDateString());
                    termEndTextInputLayout.getEditText().setText(term.getEndDateString());

                    adjustedStartDate = term.getStartDate();
                    adjustedEndDate = term.getEndDate();

                    termViewModel.getTermsCoursesList(termId).observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
                        @Override
                        public void onChanged(List<Course> courses) {
                            if(courses != null) {
                                isTermEmpty = courses.size() == 0;
                            } else {
                                isTermEmpty = true;
                            }
                        }
                    });
                }
            });
        } else {

            termCourseLabel.setVisibility(View.INVISIBLE);
            termAddCourseButton.setVisibility(View.INVISIBLE);
            termDeleteCourseButton.setVisibility(View.INVISIBLE);
            termDeleteFAB.hide();
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
                    termStartTextInputLayout.getEditText().setText(Formatters.calendarToMMDDYYYY(adjustedStartDate));
                }
            }

            if (requestCode == DateDialogueFragment.ENDDATE_PICKER_MMDDYYYY) {

                adjustedEndDate = Calendar.getInstance();
                Bundle calendarBreakdown = data.getBundleExtra(DateDialogueFragment.SELECTED_ENDDATE);
                if(calendarBreakdown != null) {
                    adjustedEndDate.set(calendarBreakdown.getInt("YEAR", 0), calendarBreakdown.getInt("MONTH", 0), calendarBreakdown.getInt("DAY", 0));
                    termEndTextInputLayout.getEditText().setText(Formatters.calendarToMMDDYYYY(adjustedEndDate));
                }
            }
        }
    }

    @Override
    public void onDeleteCallback(@Constants.CollegeItemType int itemType, int id) {

        if (itemType == Constants.CollegeItemType.COURSE_TYPE) {
            CourseViewModel courseViewModel = ViewModelProviders.of(TermEditFragment.this).get(CourseViewModel.class);

            final AssessmentViewModel assessmentViewModel = ViewModelProviders.of(TermEditFragment.this).get(AssessmentViewModel.class);

            courseViewModel.getCourseAssessmentsList(id).observe(getViewLifecycleOwner(), new Observer<List<Assessment>>() {
                @Override
                public void onChanged(List<Assessment> assessments) {

                    Iterator<Assessment> itr1 = assessments.iterator();

                    while (itr1.hasNext()) {

                        int assessmentId = itr1.next().getId();

                        assessmentViewModel.getAssessmentGoalsList(assessmentId).observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
                            @Override
                            public void onChanged(List<Goal> goals) {

                                Iterator<Goal> itr2 = goals.iterator();

                                while (itr2.hasNext()) {
                                    assessmentViewModel.deleteGoal(itr2.next().getId());
                                }
                            }
                        });
                        assessmentViewModel.deleteAssessment(assessmentId);
                    }
                }
            });
            courseViewModel.deleteCourse(id);
        }
    }
}

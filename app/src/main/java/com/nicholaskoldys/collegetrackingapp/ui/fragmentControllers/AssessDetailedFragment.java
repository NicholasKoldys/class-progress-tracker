package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nicholaskoldys.collegetrackingapp.MainActivity;
import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;
import com.nicholaskoldys.collegetrackingapp.model.Goal;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.BaseItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.ItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.AssessmentViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;

import java.util.Calendar;
import java.util.List;

public class AssessDetailedFragment extends Fragment implements BaseItemRecyclerViewAdapter.ListItemClickListener, GoalSetDialogueFragment.OnSelectGoalListener {

    private static final String ASSESSMENT_ID = "assessmentId";
    private final @Constants.NotificationType int NOTIFICATION_CODE = Constants.NotificationType.ASSESSMENT_GOAL_NOTIFICATION_CODE;

    private int assessmentId;
    private int goalCount = 0;

    private TextView assessmentTitleTextView;
    private TextView assessmentDueDateTextView;
    private TextView assessmentInfoTextView;

    private ItemRecyclerViewAdapter adapter;
    private RecyclerView assessmentGoalRecyclerView;

    private FloatingActionButton assessmentEditFAB;
    private FloatingActionButton assessmentGoalFAB;

    private AssessmentViewModel assessmentViewModel;


    public static AssessDetailedFragment newInstance(int assessmentId) {
        //Log.i("MAIN", "Creating a Assessment Detailed Fragment with assessmentId " + assessmentId);
        Bundle args = new Bundle();
        args.putInt(ASSESSMENT_ID, assessmentId);
        AssessDetailedFragment assessDetailedFragment = new AssessDetailedFragment();
        assessDetailedFragment.setArguments(args);
        return assessDetailedFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        assessmentId = args.getInt(ASSESSMENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_content_detailed_assessment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View assessmentDetailedFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(assessmentDetailedFragmentView, savedInstanceState);

        initViewObjects(assessmentDetailedFragmentView);
        setupButtonListeners();
        initViewModel();
        initRecyclerView();
        applyDisplayDataViews();
    }

    private void initViewObjects(View assessmentDetailedFragmentView) {

        assessmentTitleTextView = assessmentDetailedFragmentView.findViewById(R.id.assessmentTitleTextView);
        assessmentDueDateTextView = assessmentDetailedFragmentView.findViewById(R.id.assessmentDueDateTextView);
        assessmentInfoTextView = assessmentDetailedFragmentView.findViewById(R.id.assessmentInfoTextView);

        assessmentGoalRecyclerView = assessmentDetailedFragmentView.findViewById(R.id.assessmentGoalDatesRecyclerView);

        assessmentGoalFAB = assessmentDetailedFragmentView.findViewById(R.id.assessmentGoalFAB);
        assessmentEditFAB = assessmentDetailedFragmentView.findViewById(R.id.assessmentEditFAB);
    }

    private void setupButtonListeners() {

        assessmentGoalFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                GoalSetDialogueFragment goalSetDialogueFragment = GoalSetDialogueFragment.newInstance(assessmentId, goalCount, true, NOTIFICATION_CODE);
                goalSetDialogueFragment.setTargetFragment(AssessDetailedFragment.this, 0);
                goalSetDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        assessmentEditFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                AssessmentEditFragment assessmentEditFragment = AssessmentEditFragment.newInstance(assessmentId, false);
                transaction.replace(R.id.fragmentViewHolder, assessmentEditFragment).addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initViewModel() {

        assessmentViewModel = ViewModelProviders.of(AssessDetailedFragment.this).get(AssessmentViewModel.class);
    }

    private void initRecyclerView() {

        adapter = new ItemRecyclerViewAdapter(getContext(), this);
        assessmentGoalRecyclerView.setAdapter(adapter);
        assessmentGoalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void applyDisplayDataViews() {

        assessmentViewModel.getAssessment(assessmentId).observe(getViewLifecycleOwner(), new Observer<Assessment>() {
            @Override
            public void onChanged(Assessment assessment) {
                assessmentTitleTextView.setText(assessment.getTitle());
                assessmentDueDateTextView.setText(assessment.getDueDateString());
                assessmentInfoTextView.setText(assessment.getInfo());
                adapter.notifyDataSetChanged();
            }
        });

        assessmentViewModel.getAssessmentGoalsList(assessmentId).observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                adapter.setDisplayData(goals);
                goalCount = adapter.getItemCount();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onListItemClick(CollegeBase item) {
        if(item != null) {
            FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
            GoalSetDialogueFragment goalSetDialogueFragment = GoalSetDialogueFragment.newInstance(item.getId(), goalCount, false, NOTIFICATION_CODE);
            goalSetDialogueFragment.setTargetFragment(this, 0);
            goalSetDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
        }
    }

    @Override
    public void deleteSelectedGoal(int goalId) {
        assessmentViewModel.deleteGoal(goalId);
    }

    @Override
    public void createNewGoal(int goalId, String title, Calendar alarmDateTime) {
        assessmentViewModel.submitGoal(goalId, title, alarmDateTime, assessmentId, NOTIFICATION_CODE);
    }

    @Override
    public void updateGoal(int goalId, String title, Calendar alarmDateTime) {
        assessmentViewModel.updateGoal(goalId, title, alarmDateTime);
    }
}

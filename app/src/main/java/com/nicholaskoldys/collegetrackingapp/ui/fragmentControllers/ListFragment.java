package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nicholaskoldys.collegetrackingapp.MainActivity;
import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Term;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.BaseItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.ItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.AssessmentViewModel;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.CourseViewModel;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.TermViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.LoadingProgressController;

import java.util.List;

public class ListFragment extends Fragment implements BaseItemRecyclerViewAdapter.ListItemClickListener {

    private static final String ITEM_TYPE = "itemType";

    private @Constants.CollegeItemType
    int itemType;
    private TextView typeListTitleTextView;
    private RecyclerView typeListRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView emptyListViewMessageTextView;

    private FloatingActionButton listTermAddFAB;

    private ItemRecyclerViewAdapter recyclerViewAdapter;
    private ViewModel viewModel;


    public static ListFragment newInstance(@Constants.CollegeItemType int itemType) {
        //Log.i("MAIN", "Creating a Select List Dialogue Fragment with itemType " + Constants.getItemTypeString(itemType));
        Bundle args = new Bundle();
        args.putInt(ITEM_TYPE, itemType);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        itemType = args.getInt(ITEM_TYPE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View listFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(listFragmentView, savedInstanceState);

        initViewObjects(listFragmentView);
        setupButtonListeners();
        initViewModel();
        initRecyclerView();

        switch (itemType) {

            case Constants.CollegeItemType.TERM_TYPE:
                typeListTitleTextView.setText("Terms");
                break;

            case Constants.CollegeItemType.COURSE_TYPE:
                typeListTitleTextView.setText("Courses");
                break;

            case Constants.CollegeItemType.ASSESSMENT_TYPE:
                typeListTitleTextView.setText("Assessments");
                break;
        }
        applyDisplayDataToRecyclerView();
    }

    private void initViewObjects(View listFragmentView) {

        typeListTitleTextView = listFragmentView.findViewById(R.id.typeListTitleTextView);
        typeListRecyclerView = listFragmentView.findViewById(R.id.typeListRecyclerView);
        loadingProgressBar = listFragmentView.findViewById(R.id.typeListLoadingProgressBar);
        emptyListViewMessageTextView  = listFragmentView.findViewById(R.id.emptyListViewMessageTextView);


        listTermAddFAB = listFragmentView.findViewById(R.id.listTermAddFAB);
        if(itemType == Constants.CollegeItemType.TERM_TYPE) {
            listTermAddFAB.show();
        }
    }

    private void setupButtonListeners() {

        listTermAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("MAIN", "Starting a Term Edit Fragment for new Term");
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                TermEditFragment termFragment = TermEditFragment.newInstance(0, true);
                transaction.replace(R.id.fragmentViewHolder, termFragment).addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initViewModel() {

        switch (itemType) {

            case Constants.CollegeItemType.TERM_TYPE:
                viewModel = ViewModelProviders.of(ListFragment.this).get(TermViewModel.class);
                break;

            case Constants.CollegeItemType.COURSE_TYPE:
                viewModel = ViewModelProviders.of(ListFragment.this).get(CourseViewModel.class);
                break;

            case Constants.CollegeItemType.ASSESSMENT_TYPE:
                viewModel = ViewModelProviders.of(ListFragment.this).get(AssessmentViewModel.class);
                break;
        }
    }

    private void initRecyclerView() {

        recyclerViewAdapter = new ItemRecyclerViewAdapter(getContext(), ListFragment.this);
        typeListRecyclerView.setAdapter(recyclerViewAdapter);
        typeListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void applyDisplayDataToRecyclerView() {

        switch (itemType) {

            case Constants.CollegeItemType.TERM_TYPE:
                ((TermViewModel) viewModel).getTermsList().observe(ListFragment.this, new Observer<List<Term>>() {
                    @Override
                    public void onChanged(List<Term> terms) {
                        recyclerViewAdapter.setDisplayData(terms);
                    }
                });
                break;

            case Constants.CollegeItemType.COURSE_TYPE:
                ((CourseViewModel) viewModel).getCoursesList().observe(ListFragment.this, new Observer<List<Course>>() {
                    @Override
                    public void onChanged(List<Course> courses) {
                        recyclerViewAdapter.setDisplayData(courses);
                    }
                });
                break;

            case Constants.CollegeItemType.ASSESSMENT_TYPE:
                ((AssessmentViewModel) viewModel).getAssessmentsList().observe(ListFragment.this, new Observer<List<Assessment>>() {
                    @Override
                    public void onChanged(List<Assessment> assessments) {
                        recyclerViewAdapter.setDisplayData(assessments);
                    }
                });
                break;
        }
        new LoadingProgressController(loadingProgressBar, emptyListViewMessageTextView, recyclerViewAdapter).runLoader();
    }

    @Override
    public void onListItemClick(CollegeBase item) {

        if(item != null) {

            FragmentTransaction transaction =
                    getActivity().getSupportFragmentManager().beginTransaction();

            switch (itemType) {

                case Constants.CollegeItemType.TERM_TYPE:
                    //Log.i("MAIN", "Starting a Detailed Term");
                    TermDetailedFragment termFragment = TermDetailedFragment.newInstance(item.getId());
                    transaction.replace(R.id.fragmentViewHolder, termFragment, "DETAILED_TERM").addToBackStack("DETAILED_TERM");
                    transaction.commit();
                    break;

                case Constants.CollegeItemType.COURSE_TYPE:
                    //Log.i("MAIN", "Starting a Detailed Course");
                    CourseDetailedFragment courseFragment = CourseDetailedFragment.newInstance(item.getId());
                    transaction.replace(R.id.fragmentViewHolder, courseFragment, "DETAILED_COURSE").addToBackStack("DETAILED_COURSE");
                    transaction.commit();
                    break;

                case Constants.CollegeItemType.ASSESSMENT_TYPE:
                    //Log.i("MAIN", "Starting a Detailed Assessment");
                    AssessDetailedFragment assessmentFragment = AssessDetailedFragment.newInstance(item.getId());
                    transaction.replace(R.id.fragmentViewHolder, assessmentFragment, "DETAILED_ASSESSMENT").addToBackStack("DETAILED_ASSESSMENT");
                    transaction.commit();
                    break;
            }
        }
    }
}

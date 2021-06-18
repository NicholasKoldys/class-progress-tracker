package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Term;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.BaseItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.ItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.TermViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.LoadingProgressController;

import java.util.List;

public class TermDetailedFragment extends Fragment implements BaseItemRecyclerViewAdapter.ListItemClickListener {

    private static final String TERM_ID = "termId";

    private int termId = 0;

    private TextView termTitleTextView;
    private TextView termStartTextView;
    private TextView termEndTextView;
    private RecyclerView termCoursesRecyclerView;

    private ProgressBar loadingProgressBar;
    private TextView termEmptySublistMessageTextView;

    private FloatingActionButton termEditFAB;

    private TermViewModel termViewModel;
    private ItemRecyclerViewAdapter recyclerViewAdapter;


    public static TermDetailedFragment newInstance(int termId) {
        //Log.i("MAIN", "Creating a Term Detailed Fragment with termId " + termId);
        Bundle args = new Bundle();
        args.putInt(TERM_ID, termId);
        TermDetailedFragment termDetailedFragment = new TermDetailedFragment();
        termDetailedFragment.setArguments(args);
        return termDetailedFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        termId = args.getInt(TERM_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_content_detailed_term, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View termDetailedFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(termDetailedFragmentView, savedInstanceState);

        initViewObjects(termDetailedFragmentView);
        setupOnClickListeners();
        initViewModel();
        initRecyclerView();
        applyDisplayDataViews();
    }

    private void initViewObjects(View termDetailedFragmentView) {

        termTitleTextView = termDetailedFragmentView.findViewById(R.id.termTitleTextView);
        termStartTextView = termDetailedFragmentView.findViewById(R.id.termStartTextView);
        termEndTextView = termDetailedFragmentView.findViewById(R.id.termEndTextView);
        termCoursesRecyclerView = termDetailedFragmentView.findViewById(R.id.termCoursesRecyclerView);

        loadingProgressBar = termDetailedFragmentView.findViewById(R.id.termLoadingProgressBar);
        termEmptySublistMessageTextView = termDetailedFragmentView.findViewById(R.id.termEmptySublistMessageTextView);

        termEditFAB = termDetailedFragmentView.findViewById(R.id.termEditFAB);
    }

    private void setupOnClickListeners() {

        termEditFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                TermEditFragment termEditFragment = TermEditFragment.newInstance(termId, false);
                transaction.replace(R.id.fragmentViewHolder, termEditFragment).addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initViewModel() {

        termViewModel = ViewModelProviders.of(TermDetailedFragment.this).get(TermViewModel.class);
    }

    private void initRecyclerView() {

        recyclerViewAdapter = new ItemRecyclerViewAdapter(getContext(), TermDetailedFragment.this);
        termCoursesRecyclerView.setAdapter(recyclerViewAdapter);
        termCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void applyDisplayDataViews() {

        termViewModel.getTerm(termId).observe(TermDetailedFragment.this, new Observer<Term>() {
            @Override
            public void onChanged(Term term) {
                termTitleTextView.setText(term.getTitle());
                termStartTextView.setText(term.getStartDateString());
                termEndTextView.setText(term.getEndDateString());
            }
        });

        termViewModel.getTermsCoursesList(termId).observe(TermDetailedFragment.this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                recyclerViewAdapter.setDisplayData(courses);
            }
        });

        new LoadingProgressController(loadingProgressBar, termEmptySublistMessageTextView, recyclerViewAdapter).runLoader();
    }

    @Override
    public void onListItemClick(CollegeBase item) {

        if(item != null) {
            FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
            CourseDetailedFragment courseFragment = CourseDetailedFragment.newInstance(item.getId());
            transaction.replace(R.id.fragmentViewHolder, courseFragment).addToBackStack(null);
            transaction.commit();
        }
    }
}

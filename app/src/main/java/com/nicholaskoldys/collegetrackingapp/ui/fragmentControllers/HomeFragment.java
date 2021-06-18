package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nicholaskoldys.collegetrackingapp.MainActivity;
import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;

public class HomeFragment extends Fragment {

    private TextView termTextView;
    private TextView courseTextView;
    private TextView assessmentTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Log.i("MAIN", "Starting HomeFragment from Main Activity");
        return inflater.inflate(R.layout.fragment_content_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View homeFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(homeFragmentView, savedInstanceState);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        initViewObjects(homeFragmentView);
        setupOnClickMethods();
    }

    private void initViewObjects(View homeFragmentView) {
        termTextView = homeFragmentView.findViewById(R.id.termTypeSelectionTextView);
        courseTextView = homeFragmentView.findViewById(R.id.courseTypeSelectionTextView);
        assessmentTextView = homeFragmentView.findViewById(R.id.assessmentTypeSelectionTextView);
    }

    private void setupOnClickMethods() {

        final FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());

        termTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragment termListFragment = ListFragment.newInstance(Constants.CollegeItemType.TERM_TYPE);
                transaction.replace(R.id.fragmentViewHolder, termListFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        courseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragment courseListFragment = ListFragment.newInstance(Constants.CollegeItemType.COURSE_TYPE);
                transaction.replace(R.id.fragmentViewHolder, courseListFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        assessmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragment assessmentListFragment = ListFragment.newInstance(Constants.CollegeItemType.ASSESSMENT_TYPE);
                transaction.replace(R.id.fragmentViewHolder, assessmentListFragment).addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.mainHomeMenuButton).setVisible(false);
    }
}

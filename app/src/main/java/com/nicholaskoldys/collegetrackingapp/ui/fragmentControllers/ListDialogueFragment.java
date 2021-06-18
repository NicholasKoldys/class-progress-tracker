package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Assessment;
import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import com.nicholaskoldys.collegetrackingapp.model.Term;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.BaseItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.ItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.CourseViewModel;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.TermViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.LoadingProgressController;

import java.util.List;

public class ListDialogueFragment<T extends CollegeBase> extends DialogFragment implements BaseItemRecyclerViewAdapter.ListItemClickListener {

    private static final String ITEM_TYPE = "itemType";
    private static final String PARENT_ID = "parentId";

    private @Constants.CollegeItemType
    int itemType;
    private int parentId;
    private int selectedItemId = -1;

    private ItemRecyclerViewAdapter adapter;
    private ViewModel itemViewModel;

    private RecyclerView listRecyclerView;
    private Button selectButton;
    private Button cancelButton;
    private TextView emptyMessage;


    static ListDialogueFragment newInstance(@Constants.CollegeItemType int itemType, int parentId) {
        //Log.i("MAIN", "Creating a List Dialogue Fragment with itemType " + Constants.getItemTypeString(itemType) + " | parentId " + parentId);
        Bundle args = new Bundle();
        args.putInt(ITEM_TYPE, itemType);
        args.putInt(PARENT_ID, parentId);
        ListDialogueFragment listDialogueFragment = new ListDialogueFragment();
        listDialogueFragment.setArguments(args);
        return listDialogueFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        itemType = args.getInt(ITEM_TYPE);
        parentId = args.getInt(PARENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_dialogue_listselect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View listDialogueFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(listDialogueFragmentView, savedInstanceState);

        initViewObjects(listDialogueFragmentView);
        setupOnClickListeners();
        initRecyclerView();
        initViewModelANDApplyDisplayDataViews();
    }

    private void initViewObjects(View listDialogueFragmentView) {

        listRecyclerView = listDialogueFragmentView.findViewById(R.id.dialogueListRecyclerView);
        selectButton = listDialogueFragmentView.findViewById(R.id.dialogueListSelectButton);
        cancelButton = listDialogueFragmentView.findViewById(R.id.dialogueListCancelButton);
        emptyMessage = listDialogueFragmentView.findViewById(R.id.dialogueListEmptyMessageTextView);
    }

    private void setupOnClickListeners() {

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedItemId != -1) {
                    ((OnSelectDLTListen)getTargetFragment()).onDeleteCallback(itemType, selectedItemId);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), "Please make a selection to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private void initRecyclerView() {

        adapter = new ItemRecyclerViewAdapter(getContext(), ListDialogueFragment.this);
        listRecyclerView.setAdapter(adapter);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initViewModelANDApplyDisplayDataViews() {

        switch (itemType) {

            case Constants.CollegeItemType.TERM_TYPE:
                itemViewModel = ViewModelProviders.of(ListDialogueFragment.this).get(TermViewModel.class);
                ((TermViewModel) itemViewModel).getTermsList().observe(this, new Observer<List<Term>>() {
                    @Override
                    public void onChanged(List<Term> terms) {
                        adapter.setDisplayData(terms);
                    }
                });
                break;

            case Constants.CollegeItemType.COURSE_TYPE:
                itemViewModel = ViewModelProviders.of(ListDialogueFragment.this).get(TermViewModel.class);
                ((TermViewModel) itemViewModel).getTermsCoursesList(parentId).observe(this, new Observer<List<Course>>() {
                    @Override
                    public void onChanged(List<Course> courses) {
                        adapter.setDisplayData(courses);
                    }
                });
                break;

            case Constants.CollegeItemType.ASSESSMENT_TYPE:
                itemViewModel = ViewModelProviders.of(ListDialogueFragment.this).get(CourseViewModel.class);
                ((CourseViewModel) itemViewModel).getCourseAssessmentsList(parentId).observe(this, new Observer<List<Assessment>>() {
                    @Override
                    public void onChanged(List<Assessment> assessments) {
                        adapter.setDisplayData(assessments);
                    }
                });
                break;

            case Constants.CollegeItemType.MENTOR_TYPE:
                itemViewModel = ViewModelProviders.of(ListDialogueFragment.this).get(CourseViewModel.class);
                ((CourseViewModel) itemViewModel).getCourseMentorsList(parentId).observe(this, new Observer<List<Mentor>>() {
                    @Override
                    public void onChanged(List<Mentor> mentors) {
                        adapter.setDisplayData(mentors);
                    }
                });
                break;

            case Constants.CollegeItemType.GOAL_TYPE:
                break;
        }
        new LoadingProgressController(null, emptyMessage, adapter).runLoader();
    }

    @Override
    public void onListItemClick(CollegeBase item) {
        if(item != null) {
            selectedItemId = item.getId();
            adapter.notifyDataSetChanged();
        }
    }

    public interface OnSelectDLTListen {
        void onDeleteCallback(@Constants.CollegeItemType int itemType, int id);
    }
}

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.BaseItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.ItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.CourseViewModel;

import java.util.List;

public class MentorDialogueFragment extends DialogFragment implements BaseItemRecyclerViewAdapter.ListItemClickListener {

    private static final String PARENT_OR_CHILD_ID = "parentORChildId";
    private static final String IS_NEW_MENTOR = "isNewMentor";

    private Boolean isNewMentor;
    private int parentCourseId;
    private int selectedMentorId = 0;

    private RecyclerView mentorEditRecyclerView;
    private TextView mentorEditEmptyMessage;

    private TextInputLayout mentorNameEditLayout;
    private TextInputLayout mentorEmailEditLayout;
    private TextInputLayout mentorPhoneEditLayout;

    private Button mentorEditCancelButton;
    private Button mentorEditDeleteButton;
    private Button mentorEditConfirmButton;
    private Button mentorEditCreateButton;

    private CourseViewModel viewModel;
    private ItemRecyclerViewAdapter adapter;


    public static MentorDialogueFragment newInstance(int parentORChildId, Boolean isNewMentor) {
        //Log.i("MAIN", "Creating a Edit Mentor Dialogue Fragment with parentOrChildId " + parentORChildId + " | isNewMentor " + isNewMentor);
        Bundle args = new Bundle();
        args.putInt(PARENT_OR_CHILD_ID, parentORChildId);
        args.putBoolean(IS_NEW_MENTOR, isNewMentor);
        MentorDialogueFragment mentorDialogueFragment = new MentorDialogueFragment();
        mentorDialogueFragment.setArguments(args);
        return mentorDialogueFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        isNewMentor = args.getBoolean(IS_NEW_MENTOR);
        parentCourseId = args.getInt(PARENT_OR_CHILD_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_dialogue_mentoredit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View mentorEditDialogueFragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mentorEditDialogueFragment, savedInstanceState);

        initViewObjects(mentorEditDialogueFragment);
        setupOnClickListeners();
        initRecyclerView();
        initViewModel();
        applyDisplayDataViews();
    }

    private void initViewObjects(View mentorEditDialogueFragment) {

        mentorEditRecyclerView = mentorEditDialogueFragment.findViewById(R.id.mentorEditRecyclerView);
        mentorEditEmptyMessage = mentorEditDialogueFragment.findViewById(R.id.mentorEditEmptyMessage);

        mentorNameEditLayout = mentorEditDialogueFragment.findViewById(R.id.mentorEditNameLayout);
        mentorPhoneEditLayout = mentorEditDialogueFragment.findViewById(R.id.mentorEmailEditLayout);
        mentorEmailEditLayout = mentorEditDialogueFragment.findViewById(R.id.mentorPhoneEditLayout);

        mentorEditCancelButton = mentorEditDialogueFragment.findViewById(R.id.mentorEditCancelButton);
        mentorEditDeleteButton = mentorEditDialogueFragment.findViewById(R.id.mentorEditDeleteButton);
        mentorEditConfirmButton = mentorEditDialogueFragment.findViewById(R.id.mentorEditConfirmButton);
        mentorEditCreateButton = mentorEditDialogueFragment.findViewById(R.id.mentorEditCreateButton);
    }

    private void setupOnClickListeners() {

        mentorEditConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMentorId != 0) {
                    if(!mentorNameEditLayout.getEditText().getText().toString().equals("")
                            && !mentorEmailEditLayout.getEditText().getText().toString().equals("")
                            && !mentorPhoneEditLayout.getEditText().getText().toString().equals("")) {

                        viewModel.updateMentor(
                                selectedMentorId,
                                mentorNameEditLayout.getEditText().getText().toString(),
                                mentorEmailEditLayout.getEditText().getText().toString(),
                                mentorPhoneEditLayout.getEditText().getText().toString()
                        );
                        getDialog().dismiss();
                    } else {
                        Toast.makeText(getContext(), "Please input Data before submitting", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Select a Mentor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mentorEditDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMentorId != 0) {
                    viewModel.deleteMentor(selectedMentorId);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), "Please Select a Mentor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mentorEditCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mentorEditCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mentorNameEditLayout.getEditText().getText().toString().equals("")
                    && !mentorEmailEditLayout.getEditText().getText().toString().equals("")
                    && !mentorPhoneEditLayout.getEditText().getText().toString().equals("")) {

                    viewModel.submitMentor(
                            mentorNameEditLayout.getEditText().getText().toString(),
                            mentorEmailEditLayout.getEditText().getText().toString(),
                            mentorPhoneEditLayout.getEditText().getText().toString(),
                            parentCourseId
                    );
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), "Please input Data before submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initRecyclerView() {

        adapter = new ItemRecyclerViewAdapter(getContext(), MentorDialogueFragment.this);
        mentorEditRecyclerView.setAdapter(adapter);
        mentorEditRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initViewModel() {

        viewModel = ViewModelProviders.of(MentorDialogueFragment.this).get(CourseViewModel.class);
    }

    private void applyDisplayDataViews() {

        if(!isNewMentor) {
            viewModel.getCourseMentorsList(parentCourseId).observe(MentorDialogueFragment.this, new Observer<List<Mentor>>() {
                @Override
                public void onChanged(List<Mentor> mentors) {
                    adapter.setDisplayData(mentors);
                }
            });
        } else {
            mentorEditRecyclerView.setVisibility(View.GONE);
            mentorEditDeleteButton.setVisibility(View.INVISIBLE);
            mentorEditCreateButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListItemClick(CollegeBase item) {

        if(item != null) {
            selectedMentorId = item.getId();
            mentorNameEditLayout.getEditText().setText(item.getTitle());
            mentorEmailEditLayout.getEditText().setText(((Mentor) item).getEmail());
            mentorPhoneEditLayout.getEditText().setText(((Mentor) item).getPhone());
        }
    }
}

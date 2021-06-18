package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
import com.nicholaskoldys.collegetrackingapp.model.Course;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;
import com.nicholaskoldys.collegetrackingapp.model.Notes;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.BaseItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.ItemRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.MentorRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.adapter.NotesRecyclerViewAdapter;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.CourseViewModel;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;
import com.nicholaskoldys.collegetrackingapp.utility.LoadingProgressController;

import java.util.List;

public class CourseDetailedFragment extends Fragment implements BaseItemRecyclerViewAdapter.ListItemClickListener, MentorRecyclerViewAdapter.MentorItemClickListener, NotesRecyclerViewAdapter.NotesListClickListener, NoteDialogueFragment.updateNoteViewListener {

    private static final String COURSE_ID = "courseId";

    int courseId;

    private TextView courseTitleTextView;
    private TextView courseStartTextView;
    private TextView courseEndTextView;
    private TextView courseStatusTextView;

    private RecyclerView courseAssessmentsRecyclerView;
    private RecyclerView courseNotesRecyclerView;
    private RecyclerView courseMentorsRecyclerView;

    private FloatingActionButton courseAssessmentsListFAB;
    private FloatingActionButton courseNotesListFAB;
    private FloatingActionButton courseAddNoteFAB;
    private FloatingActionButton courseEditFAB;
    private FloatingActionButton courseOptionsNoteFAB;
    private FloatingActionButton courseEditNoteFAB;
    private FloatingActionButton courseShareNoteFAB;

    private ProgressBar loadingProgressBar;
    private TextView courseEmptySublistMessageTextView;
    private TextView courseEmptyMentorMessageTextView;

    private CourseViewModel courseViewModel;
    private ItemRecyclerViewAdapter sublistAdapter;
    private NotesRecyclerViewAdapter notesAdapter;
    private MentorRecyclerViewAdapter mentorAdapter;
    private int selectedNoteId = 0;


    public static CourseDetailedFragment newInstance(int courseId) {
        //Log.i("MAIN", "Creating a Course Detailed Fragment with courseId " + courseId);
        Bundle args = new Bundle();
        args.putInt(COURSE_ID, courseId);
        CourseDetailedFragment courseDetailedFragment = new CourseDetailedFragment();
        courseDetailedFragment.setArguments(args);
        return courseDetailedFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        courseId = args.getInt(COURSE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_content_detailed_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View courseDetailedFragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(courseDetailedFragmentView, savedInstanceState);

        initViewObjects(courseDetailedFragmentView);
        setupButtonListeners();
        initViewModel();
        initRecyclerView();
        applyDisplayDataViews();
    }

    private void initViewObjects(View courseDetailedFragmentView) {

        courseTitleTextView = courseDetailedFragmentView.findViewById(R.id.courseTitleTextView);
        courseStartTextView = courseDetailedFragmentView.findViewById(R.id.courseStartTextView);
        courseEndTextView = courseDetailedFragmentView.findViewById(R.id.courseEndTextView);
        courseStatusTextView = courseDetailedFragmentView.findViewById(R.id.courseStatusTextView);

        courseAssessmentsRecyclerView = courseDetailedFragmentView.findViewById(R.id.courseAssessmentsRecyclerView);
        courseNotesRecyclerView = courseDetailedFragmentView.findViewById(R.id.courseNotesRecyclerView);
        courseMentorsRecyclerView = courseDetailedFragmentView.findViewById(R.id.courseMentorsRecyclerView);

        courseAssessmentsListFAB = courseDetailedFragmentView.findViewById(R.id.courseAssessmentsListFAB);
        courseNotesListFAB = courseDetailedFragmentView.findViewById(R.id.courseNotesListFAB);
        courseAddNoteFAB = courseDetailedFragmentView.findViewById(R.id.courseAddNoteFAB);
        courseEditFAB = courseDetailedFragmentView.findViewById(R.id.courseEditFAB);
        courseOptionsNoteFAB = courseDetailedFragmentView.findViewById(R.id.courseOptionsNoteFAB);
        courseEditNoteFAB = courseDetailedFragmentView.findViewById(R.id.courseEditNoteFAB);
        courseShareNoteFAB = courseDetailedFragmentView.findViewById(R.id.courseShareNoteFAB);

        loadingProgressBar = courseDetailedFragmentView.findViewById(R.id.courseLoadingProgressBar);
        courseEmptySublistMessageTextView = courseDetailedFragmentView.findViewById(R.id.courseEmptySubListMessageTextView);
        courseEmptyMentorMessageTextView = courseDetailedFragmentView.findViewById(R.id.courseEmptyMentorMessageTextView);
    }

    private void setupButtonListeners() {

        courseAssessmentsListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseNotesRecyclerView.setVisibility(View.INVISIBLE);
                courseAssessmentsRecyclerView.setVisibility(View.VISIBLE);
                courseNotesListFAB.show();
                courseAddNoteFAB.hide();
                courseOptionsNoteFAB.hide();
                courseEditNoteFAB.hide();
                courseShareNoteFAB.hide();
                changeAddNoteFABConstraint(courseNotesListFAB.isShown());

                if(sublistAdapter.getItemCount() == 0) {
                    courseEmptySublistMessageTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        courseNotesListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseNotesRecyclerView.setVisibility(View.VISIBLE);
                courseAssessmentsRecyclerView.setVisibility(View.INVISIBLE);
                courseAddNoteFAB.show();
                changeAddNoteFABConstraint(courseNotesListFAB.isShown());

                if(courseEmptySublistMessageTextView.getVisibility() == View.VISIBLE) {
                    courseEmptySublistMessageTextView.setVisibility(View.INVISIBLE);
                }
            }
        });

        courseAddNoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                NoteDialogueFragment noteDialogueFragment = NoteDialogueFragment.newInstance(courseId, true);
                noteDialogueFragment.setTargetFragment(CourseDetailedFragment.this, 1);
                noteDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        courseEditFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                CourseEditFragment courseEditFragment = CourseEditFragment.newInstance(courseId, false);
                transaction.replace(R.id.fragmentViewHolder, courseEditFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        courseOptionsNoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!courseEditNoteFAB.isShown()) {
                    courseEditNoteFAB.show();
                    courseShareNoteFAB.show();
                } else {
                    courseEditNoteFAB.hide();
                    courseShareNoteFAB.hide();
                }
            }
        });

        courseEditNoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
                NoteDialogueFragment noteDialogueFragment = NoteDialogueFragment.newInstance(selectedNoteId, false);
                noteDialogueFragment.setTargetFragment(CourseDetailedFragment.this, 1);
                noteDialogueFragment.show(transaction, Constants.FRAGMENT_DIALOG_TAG);
            }
        });

        courseShareNoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedNoteId != 0) {

                    final String courseTitle = courseTitleTextView.getText().toString();

                    courseViewModel.getNote(selectedNoteId).observe(getViewLifecycleOwner(), new Observer<Notes>() {
                        @Override
                        public void onChanged(Notes notes) {

                            if(notes != null) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setType("text/plain");
                                //email.setType("text/html");
                                //email.setType("message/rfc822");
//                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"nkoldys@wgu.edu"});
                                email.putExtra(Intent.EXTRA_SUBJECT, "Note From " + courseTitle + " : " + notes.getTitle());
                                email.putExtra(Intent.EXTRA_TEXT, notes.getContent());
                                try {
                                    startActivity(Intent.createChooser(email, "Share with Email or SMS..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void initViewModel() {

        courseViewModel = ViewModelProviders.of(CourseDetailedFragment.this).get(CourseViewModel.class);
    }

    private void initRecyclerView() {

        sublistAdapter = new ItemRecyclerViewAdapter(getContext(), CourseDetailedFragment.this);
        courseAssessmentsRecyclerView.setAdapter(sublistAdapter);
        courseAssessmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notesAdapter = new NotesRecyclerViewAdapter(getContext(), CourseDetailedFragment.this);
        courseNotesRecyclerView.setAdapter(notesAdapter);
        courseNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mentorAdapter = new MentorRecyclerViewAdapter(getContext(), CourseDetailedFragment.this);
        courseMentorsRecyclerView.setAdapter(mentorAdapter);
        courseMentorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void applyDisplayDataViews() {

        courseViewModel.getCourse(courseId).observe(CourseDetailedFragment.this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                courseTitleTextView.setText(course.getTitle());
                courseStartTextView.setText(course.getStartDateString());
                courseEndTextView.setText(course.getEndDateString());
                courseStatusTextView.setText(course.getStatusString());

                courseViewModel.getCourseAssessmentsList(courseId).observe(CourseDetailedFragment.this, new Observer<List<Assessment>>() {
                    @Override
                    public void onChanged(List<Assessment> assessments) {
                        sublistAdapter.setDisplayData(assessments);
                        sublistAdapter.notifyDataSetChanged();
                    }
                });

                courseViewModel.getCourseMentorsList(courseId).observe(CourseDetailedFragment.this, new Observer<List<Mentor>>() {
                    @Override
                    public void onChanged(List<Mentor> mentors) {
                        mentorAdapter.setMentorDisplayData(mentors);
                        mentorAdapter.notifyDataSetChanged();
                    }
                });

                courseViewModel.getCourseNotesList(courseId).observe(CourseDetailedFragment.this, new Observer<List<Notes>>() {
                    @Override
                    public void onChanged(List<Notes> notes) {
                        notesAdapter.setNotesDisplayData(notes);
                        notesAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        new LoadingProgressController(loadingProgressBar, courseEmptySublistMessageTextView, sublistAdapter).runLoader();
        new LoadingProgressController(null, courseEmptyMentorMessageTextView, mentorAdapter).runLoader();
    }

    @Override
    public void onListItemClick(CollegeBase item) {

        if(item != null) {
            FragmentTransaction transaction = MainActivity.getTransactionForDialog(getActivity());
            AssessDetailedFragment assessmentFragment = AssessDetailedFragment.newInstance(item.getId());
            transaction.replace(R.id.fragmentViewHolder, assessmentFragment).addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onMentorItemClick(Mentor mentor) {
        //No Action
    }

    @Override
    public void onNotesListClick(Notes note) {

        if(note != null) {
            courseNotesListFAB.hide();
            courseOptionsNoteFAB.show();
            changeAddNoteFABConstraint(courseNotesListFAB.isShown());

            selectedNoteId = note.getId();
            notesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateNoteViewRequest() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notesAdapter.notifyDataSetChanged();
    }

    void changeAddNoteFABConstraint(Boolean isNoteListFABShown) {

        ConstraintLayout constraintLayout = getActivity().findViewById(R.id.courseDetailedConstraintLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        if(isNoteListFABShown) {
            constraintSet.connect(R.id.courseAddNoteFAB, ConstraintSet.START, R.id.courseNotesListFAB, ConstraintSet.END, 16);
        }

        if(courseOptionsNoteFAB.isShown()) {
            constraintSet.connect(R.id.courseAddNoteFAB, ConstraintSet.START, R.id.courseOptionsNoteFAB, ConstraintSet.END, 16);
        }
        constraintSet.applyTo(constraintLayout);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.notesShareMenuButton).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.notesShareMenuButton:
                shareAllCourseNotes();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareAllCourseNotes() {

        final String courseTitle = courseTitleTextView.getText().toString();

        courseViewModel.getCourseNotesList(courseId).observe(getViewLifecycleOwner(), new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notesList) {

                if(notesList != null) {

                    StringBuilder notesMegaString = new StringBuilder();

                    for(Notes note : notesList) {
                        notesMegaString.append(note.getTitle());
                        notesMegaString.append(" : ");
                        notesMegaString.append(note.getContent());
                        notesMegaString.append("\n\n");
                    }

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("text/plain");
                    //email.setType("text/html");
                    //email.setType("message/rfc822");
                    //email.putExtra(Intent.EXTRA_EMAIL, new String[]{"nkoldys@wgu.edu"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "All Notes From " + courseTitle);
                    email.putExtra(Intent.EXTRA_TEXT, notesMegaString.toString());
                    try {
                        startActivity(Intent.createChooser(email, "Share with Email or SMS..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

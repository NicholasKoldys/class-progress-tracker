package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Notes;
import com.nicholaskoldys.collegetrackingapp.ui.viewmodel.CourseViewModel;

public class NoteDialogueFragment extends DialogFragment {

    private static final String PARENT_OR_CHILD_ID = "parentORChildId";
    private static final String IS_NEW_NOTE = "isNewNote";

    private int parentCourseId;
    private Boolean isNewNote;
    private int noteId;
    private CourseViewModel courseViewModel;


    public static NoteDialogueFragment newInstance(int parentORChildId, Boolean isNewNote) {
        //Log.i("MAIN", "Creating a Edit Note Dialogue Fragment with parentOrChildId " + parentORChildId + " | isNewNote " + isNewNote);
        Bundle args = new Bundle();
        args.putInt(PARENT_OR_CHILD_ID, parentORChildId);
        args.putBoolean(IS_NEW_NOTE, isNewNote);
        NoteDialogueFragment noteDialogueFragment = new NoteDialogueFragment();
        noteDialogueFragment.setArguments(args);
        return noteDialogueFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        isNewNote = args.getBoolean(IS_NEW_NOTE);

        if(isNewNote) {
            parentCourseId = args.getInt(PARENT_OR_CHILD_ID);
        } else {
            noteId = args.getInt(PARENT_OR_CHILD_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        courseViewModel = ViewModelProviders.of(NoteDialogueFragment.this).get(CourseViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(isNewNote) {

            builder.setTitle("Create a new note:");

            final EditText noteText = (EditText) LayoutInflater.from(getContext()).inflate(R.layout.fragment_content_dialogue_noteeditor, null, false);

            builder.setView(noteText);

            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newNoteContent = noteText.getText().toString();
                    if(!newNoteContent.equals("")) {
                        courseViewModel.submitNotes("Note" + parentCourseId, parentCourseId, newNoteContent);
                        ((CourseDetailedFragment) getTargetFragment()).updateNoteViewRequest();
                    } else {
                        Toast.makeText(getContext(), "Please input Note Text before Creating", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ((CourseDetailedFragment)getTargetFragment()).updateNoteViewRequest();
                }
            });
        } else {

            builder.setTitle("Edit note:");

            final EditText noteText = (EditText) LayoutInflater.from(getContext()).inflate(R.layout.fragment_content_dialogue_noteeditor, null, false);

            courseViewModel.getNote(noteId).observe(NoteDialogueFragment.this, new Observer<Notes>() {
                @Override
                public void onChanged(Notes notes) {
                    noteText.setText(notes.getContent());
                }
            });

            builder.setView(noteText);

            builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String noteContent = noteText.getText().toString();
                    if(!noteContent.equals("")) {
                        courseViewModel.updateNotes(noteId, noteContent);
                        ((CourseDetailedFragment) getTargetFragment()).updateNoteViewRequest();
                    } else {
                        Toast.makeText(getContext(), "Please don't leave text blank", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    courseViewModel.deleteNotes(noteId);
                    ((CourseDetailedFragment)getTargetFragment()).updateNoteViewRequest();
                }
            });

            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ((CourseDetailedFragment)getTargetFragment()).updateNoteViewRequest();
                }
            });
        }
        return builder.create();
    }

    interface updateNoteViewListener {
        void updateNoteViewRequest();
    }
}

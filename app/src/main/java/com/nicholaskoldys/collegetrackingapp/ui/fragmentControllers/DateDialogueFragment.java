package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class DateDialogueFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    static final String DATE_REQUEST_CODE = "DATE_REQUEST";
    static final int STARTDATE_PICKER_MMDDYYYY = 21;
    static final int ENDDATE_PICKER_MMDDYYYY = 22;
    static final String SELECTED_STARTDATE = "SELECTED_STARTDATE";
    static final String SELECTED_ENDDATE = "SELECTED_ENDDATE";
    static final String DATE_PICKER_YEAR = "YEAR";
    static final String DATE_PICKER_MONTH = "MONTH";
    static final String DATE_PICKER_DAY_OF_MONTH = "DAY";


    static DateDialogueFragment newInstance(Bundle args) {
        //Log.i("MAIN", "Creating a Date Dialogue Fragment");
        DateDialogueFragment fragment = new DateDialogueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Calendar pickDate = Calendar.getInstance();
        int year = pickDate.get(Calendar.YEAR);
        int month = pickDate.get(Calendar.MONTH);
        int day = pickDate.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        int requestedDate = getArguments().getInt(DATE_REQUEST_CODE, 0);

        Fragment callerFragment = getTargetFragment();

        if (callerFragment != null) {

            Intent data = new Intent();

            Bundle calendarBreakdown = new Bundle();
            calendarBreakdown.putInt(DATE_PICKER_YEAR, selectedDate.get(Calendar.YEAR));
            calendarBreakdown.putInt(DATE_PICKER_MONTH, selectedDate.get(Calendar.MONTH));
            calendarBreakdown.putInt(DATE_PICKER_DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH));

            if (requestedDate == STARTDATE_PICKER_MMDDYYYY) {

                data.putExtra(SELECTED_STARTDATE, calendarBreakdown);
                getTargetFragment().onActivityResult(STARTDATE_PICKER_MMDDYYYY, Activity.RESULT_OK, data);

            } else if (requestedDate == ENDDATE_PICKER_MMDDYYYY) {

                data.putExtra(SELECTED_ENDDATE, calendarBreakdown);
                getTargetFragment().onActivityResult(ENDDATE_PICKER_MMDDYYYY, Activity.RESULT_OK, data);
            }
        }
    }
}

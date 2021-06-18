package com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class TimeDialogueFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    static final String TIME_REQUEST_CODE = "TIME_REQUEST";
    static final String SELECTED_TIME = "SELECTED_TIME";
    static final int TIME_PICKER_TTTT = 11;
    static final String TIME_PICKER_HOUROFDAY = "hourOfDay";
    static final String TIME_PICKER_MINUTE = "minute";


    static TimeDialogueFragment newInstance(Bundle args) {
        //Log.i("MAIN", "Creating a Date Dialogue Fragment");
        TimeDialogueFragment fragment = new TimeDialogueFragment();
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
        int hour = pickDate.get(Calendar.HOUR_OF_DAY);
        int minute = pickDate.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getContext()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar selectedTime = Calendar.getInstance();
        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedTime.set(Calendar.MINUTE, minute);

        int requestedDate = getArguments().getInt(TIME_REQUEST_CODE, 0);

        Fragment callerFragment = getTargetFragment();

        if (callerFragment != null) {

            Intent data = new Intent();

            Bundle calendarBreakdown = new Bundle();
            calendarBreakdown.putInt(TIME_PICKER_HOUROFDAY, selectedTime.get(Calendar.HOUR_OF_DAY));
            calendarBreakdown.putInt(TIME_PICKER_MINUTE, selectedTime.get(Calendar.MINUTE));

            data.putExtra(SELECTED_TIME, calendarBreakdown);
            getTargetFragment().onActivityResult(TIME_PICKER_TTTT, Activity.RESULT_OK, data);
        }
    }
}

package com.nicholaskoldys.collegetrackingapp.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Formatters {

    public static String calendarToMMDDYYYY(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        return month + 1 + "/" + day + "/" + year;
    }

    public static String calendarToTTTT(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        return  hour + ":" + min;
    }

    public static String calendarToMMDDYYYYTTTT(Calendar calendar) {
        return  calendarToMMDDYYYY(calendar) + " at " + calendarToTTTT(calendar);
    }

    public static SimpleDateFormat StringToCalendar() {
        return new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    }
}
package com.nicholaskoldys.collegetrackingapp.utility;

import androidx.room.TypeConverter;
import java.sql.Timestamp;
import java.util.Calendar;

public class Converters {

    @TypeConverter
    public static Calendar fromTimestamp(Long value) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(value);
        return cal;
    }

    @TypeConverter
    public static Long calendarToTimestamp(Calendar calendar) {
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return timestamp.getTime();
    }
}
package com.nicholaskoldys.collegetrackingapp.utility;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CollegeItemType.ASSESSMENT_TYPE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CollegeItemType.COURSE_TYPE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CollegeItemType.GOAL_TYPE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CollegeItemType.MENTOR_TYPE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CollegeItemType.NOTES_TYPE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CollegeItemType.NULL_TYPE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CollegeItemType.TERM_TYPE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CourseStatus.COMPLETED;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CourseStatus.DROPPED;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CourseStatus.IN_PROGRESS;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.CourseStatus.PLAN_TO_TAKE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.NotificationType.ASSESSMENT_DUE_NOTIFICATION_CODE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.NotificationType.ASSESSMENT_GOAL_NOTIFICATION_CODE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.NotificationType.COURSE_END_NOTIFICATION_CODE;
import static com.nicholaskoldys.collegetrackingapp.utility.Constants.NotificationType.COURSE_START_NOTIFICATION_CODE;

public class Constants {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({COURSE_START_NOTIFICATION_CODE, COURSE_END_NOTIFICATION_CODE, ASSESSMENT_DUE_NOTIFICATION_CODE, ASSESSMENT_GOAL_NOTIFICATION_CODE})
    public @interface NotificationType {
        int COURSE_START_NOTIFICATION_CODE = 1;
        int COURSE_END_NOTIFICATION_CODE = 2;
        int ASSESSMENT_DUE_NOTIFICATION_CODE = 3;
        int ASSESSMENT_GOAL_NOTIFICATION_CODE = 4;
    }

    public static final String FRAGMENT_DIALOG_TAG = "DIALOG";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TERM_TYPE, COURSE_TYPE, ASSESSMENT_TYPE, MENTOR_TYPE, NOTES_TYPE, NULL_TYPE, GOAL_TYPE})
    public @interface CollegeItemType {
        int NULL_TYPE = 0;
        int TERM_TYPE = 1;
        int COURSE_TYPE = 2;
        int ASSESSMENT_TYPE = 3;
        int MENTOR_TYPE = 4;
        int NOTES_TYPE = 5;
        int GOAL_TYPE = 6;
    }

    public static String getItemTypeString(@CollegeItemType int itemType) {

        @CollegeItemType int subItemType;

        switch (itemType) {

            case TERM_TYPE:
                return "TERM_TYPE";

            case COURSE_TYPE:
                return "COURSE_TYPE";

            case ASSESSMENT_TYPE:
                return "ASSESSMENT_TYPE";

            case MENTOR_TYPE:
                return "MENTOR_TYPE";

            case NOTES_TYPE:
                return "NOTES_TYPE";

            case NULL_TYPE:
                return "NULL_TYPE";

            case GOAL_TYPE:
                return "GOAL_TYPE";
        }

        return "NO_TYPE";
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PLAN_TO_TAKE, IN_PROGRESS, COMPLETED, DROPPED})
    public @interface CourseStatus {
        int PLAN_TO_TAKE = 0;
        int IN_PROGRESS = 1;
        int COMPLETED = 2;
        int DROPPED = 3;
    }

    public static String courseStatusToString(@CourseStatus int status) {

        String statusString = "";

        switch (status) {

            case PLAN_TO_TAKE:
                statusString = "Planned to take";
                return statusString;

            case IN_PROGRESS:
                statusString = "In Progress";
                return statusString;

            case COMPLETED:
                statusString = "Completed";
                return statusString;

            case DROPPED:
                statusString = "Dropped";
                return statusString;
        }

        return statusString;
    }

    public static @CourseStatus int courseStatusStringToInt(String status) {

        switch (status){

            case "Planned to take":
                return PLAN_TO_TAKE;

            case "In Progress":
                return IN_PROGRESS;

            case "Completed":
                return COMPLETED;

            case "Dropped":
                return DROPPED;
        }

        return DROPPED;
    }

    public static String[] getCourseStatusArray() {

        return new String[]{courseStatusToString(PLAN_TO_TAKE), courseStatusToString(IN_PROGRESS), courseStatusToString(COMPLETED), courseStatusToString(DROPPED)};
    }
}

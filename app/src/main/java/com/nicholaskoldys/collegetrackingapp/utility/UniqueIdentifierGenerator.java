package com.nicholaskoldys.collegetrackingapp.utility;

import androidx.annotation.NonNull;

public class UniqueIdentifierGenerator {

    public static int generateUniqueId(@NonNull int baseId, @NonNull int subId, @Constants.NotificationType int notificationCode) {

        StringBuilder stringIDSB = new StringBuilder();
        stringIDSB.append(notificationCode);
        stringIDSB.append(baseId);
        stringIDSB.append(subId);

        return Integer.parseInt(stringIDSB.toString());
    }
}
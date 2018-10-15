package com.example.android.gianlu33.automanualsync;

import android.content.Context;

public class AutoManualSyncUtils {

    public static String getTimeFormatted(Context context, int seconds, int index) {
        int timeConverted = seconds;

        for(int i=0; i<index; i++) {
            if (i == 2)
                timeConverted /= 24;
            else
                timeConverted /= 60;
        }

        return context.getString(R.string.text_view_frequency_time_sleep,
                timeConverted,
                context.getResources().getStringArray(R.array.array_time_units)[index]);



    }
}

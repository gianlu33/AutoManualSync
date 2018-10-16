package com.example.android.gianlu33.automanualsync;

public class AutoManualSyncUtils {
    public static final int jobID = 0;

    public static int getSeconds(int seconds, int index) {
        //todo rivedi un attimo, seconds non è più secodi ma tempo con unità in base a index, e devo restituire i secondi
        //todo ma forse è giusta uguale

        int timeConverted = seconds;

        for(int i=0; i<index; i++) {
            if (i == 2)
                timeConverted /= 24;
            else
                timeConverted /= 60;
        }

        return timeConverted;
    }
}

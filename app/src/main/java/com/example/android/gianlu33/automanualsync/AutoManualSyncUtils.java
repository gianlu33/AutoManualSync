package com.example.android.gianlu33.automanualsync;

public class AutoManualSyncUtils {

    public static int getSeconds(int time, int index) {
        int timeConverted;

        switch(index){
            case 0:
                timeConverted = time;
                break;
            case 1:
                timeConverted = time * 60;
                break;
            case 2:
                timeConverted = time * 60 * 60;
                break;
            default:
                timeConverted = time * 24 * 60 * 60;
        }

        return timeConverted;
    }

}

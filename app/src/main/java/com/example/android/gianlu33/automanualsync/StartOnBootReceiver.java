package com.example.android.gianlu33.automanualsync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartOnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent myintent = new Intent(context, MainActivity.class);

            context.startActivity(myintent);
        }
    }
}

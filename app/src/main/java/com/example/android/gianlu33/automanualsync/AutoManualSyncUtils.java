package com.example.android.gianlu33.automanualsync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

//todo se scegli firebasejobdispatcher devi cancellare tutta la parte inutile (commentata)

public class AutoManualSyncUtils {

    /*public static void scheduleJob(Context context){
        Resources res = context.getResources();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (res.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

        boolean status = sharedPreferences.getBoolean(res.getString(R.string.shared_preferences_status),
                res.getBoolean(R.bool.status_default_value));
        int frequencyTimeIndex = sharedPreferences.getInt(res.getString(R.string.shared_preferences_frequency_time_index),
                res.getInteger(R.integer.frequency_time_default_index));

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        int jobID = res.getInteger(R.integer.auto_manual_sync_job_id);

        if(jobScheduler == null){
            return;
        }

        jobScheduler.cancel(jobID);

        if(status){
            //schedulo job
            ComponentName serviceComponent = new ComponentName(context, AutoManualSyncJob.class);
            JobInfo.Builder builder = new JobInfo.Builder(jobID, serviceComponent);

            int seconds = res.getIntArray(R.array.array_frequency_values)[frequencyTimeIndex];

            builder.setPersisted(true)
                    .setPeriodic(seconds * 1000)
            //.setImportantWhileForeground(true) //segno il job come importante se l'app è in foreground
            ;
            jobScheduler.schedule(builder.build());
        }
    }*/

    public static void scheduleJob(Context context){
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Resources res = context.getResources();

        SharedPreferences sharedPreferences = context.getSharedPreferences
                (res.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

        boolean status = sharedPreferences.getBoolean(res.getString(R.string.shared_preferences_status),
                res.getBoolean(R.bool.status_default_value));
        int frequencyTimeIndex = sharedPreferences.getInt(res.getString(R.string.shared_preferences_frequency_time_index),
                res.getInteger(R.integer.frequency_time_default_index));

        firebaseJobDispatcher.cancelAll();

        if(status) {
            int seconds = res.getIntArray(R.array.array_frequency_values)[frequencyTimeIndex];

            Job job = firebaseJobDispatcher.newJobBuilder()
                    .setLifetime(Lifetime.FOREVER)
                    .setService(AutoManualSyncJob.class)
                    .setTag(res.getString(R.string.job_id))
                    .setReplaceCurrent(true)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(seconds, seconds+30))
                    .build();

            firebaseJobDispatcher.schedule(job);

        }
    }
}

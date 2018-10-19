package com.example.android.gianlu33.automanualsync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class AutoManualSyncJob extends JobService {
    public static final String TAG = AutoManualSyncJob.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new JobAsyncTask(this, jobParameters).execute();
        return false;
    }

    private class JobAsyncTask extends AsyncTask<Void, Void, Void>{
        Context context;
        JobParameters jpar;

        private JobAsyncTask(Context context, JobParameters jpar){
            this.context = context;
            this.jpar = jpar;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //se autosync è già attivo, non fai niente
            boolean isAutoSyncEnabled = ContentResolver.getMasterSyncAutomatically();
            if(isAutoSyncEnabled) return null;

            final Resources res = context.getResources();
            final SharedPreferences sharedPreferences = context.getSharedPreferences
                    (res.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
            boolean notification = sharedPreferences.getBoolean
                    (res.getString(R.string.shared_preferences_enable_notifications),
                            res.getBoolean(R.bool.enable_notifications_default_value));
            int timeSleepTimeIndex = sharedPreferences.getInt(res.getString(R.string.shared_preferences_time_sleep_time_index),
                    res.getInteger(R.integer.time_sleep_time_default_index));

            int seconds = res.getIntArray(R.array.array_time_sleep_values)[timeSleepTimeIndex];

            //inizializzo parametri per notifica
            String channelID = res.getString(R.string.default_notification_channel_id);
            String textTitleEnabled = res.getString(R.string.notification_text_title_enabled);
            String textTitleDisabled = res.getString(R.string.notification_text_title_disabled);
            int notificationID = res.getInteger(R.integer.notification_id);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            //enable autosync
            ContentResolver.setMasterSyncAutomatically(true);

            if(notification){
                //todo notifica
                mBuilder.setContentTitle(textTitleEnabled);
                notificationManager.notify(notificationID, mBuilder.build());
            }

            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //disable autosync
            ContentResolver.setMasterSyncAutomatically(false);

            if(notification){
                //todo notifica
                mBuilder.setContentTitle(textTitleDisabled);
                notificationManager.notify(notificationID, mBuilder.build());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            jobFinished(jpar, false);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}

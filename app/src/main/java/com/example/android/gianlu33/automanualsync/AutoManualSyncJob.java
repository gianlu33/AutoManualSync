package com.example.android.gianlu33.automanualsync;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class AutoManualSyncJob extends JobService {
    //public static final String TAG = AutoManualSyncJob.class.getSimpleName();

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
            //printLogToFile(context, System.currentTimeMillis(), true);

            if(notification){
                mBuilder.setContentTitle(textTitleEnabled);
                notificationManager.cancel(notificationID);
                notificationManager.notify(notificationID, mBuilder.build());
            }

            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //disable autosync
            ContentResolver.setMasterSyncAutomatically(false);
            //printLogToFile(context, System.currentTimeMillis(), false);

            if(notification){
                mBuilder.setContentTitle(textTitleDisabled);
                notificationManager.cancel(notificationID);
                notificationManager.notify(notificationID, mBuilder.build());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            jobFinished(jpar, false);
            this.context = null;
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    /*
    private void printLogToFile(Context context, long timestamp, boolean hasActivated){
        StringBuffer data;

        if(hasActivated)
            data = new StringBuffer("AutoSync enabled: ");
        else
            data = new StringBuffer("AutoSync disabled: ");

        data.append(getDateAndTime(timestamp));
        //Log.v(TAG, data.toString());
        data.append("\n");

        if(!hasActivated)
            data.append("\n");

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput("logAutoManual.txt",
                            Context.MODE_PRIVATE | Context.MODE_APPEND));
            outputStreamWriter.append(data.toString());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String getDateAndTime(long timestamp){
        Date date = new Date(timestamp);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(date);
    }
    */
}

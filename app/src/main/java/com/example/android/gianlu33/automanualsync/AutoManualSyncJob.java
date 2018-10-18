package com.example.android.gianlu33.automanualsync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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
            final SharedPreferences sharedPreferences = context.getSharedPreferences
                    (context.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
            boolean notification = sharedPreferences.getBoolean
                    (context.getString(R.string.shared_preferences_enable_notifications),
                            context.getResources().getBoolean(R.bool.enable_notifications_default_value));
            int timeSleepTimeIndex = sharedPreferences.getInt(context.getString(R.string.shared_preferences_time_sleep_time_index),
                    context.getResources().getInteger(R.integer.time_sleep_time_default_index));

            int seconds = getResources().getIntArray(R.array.array_time_sleep_values)[timeSleepTimeIndex];
            Log.v(TAG, "seconds: "  +seconds);

            ContentResolver.setMasterSyncAutomatically(true);

            if(notification){
                //todo notifica
            }

            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ContentResolver.setMasterSyncAutomatically(false);

            if(notification){
                //todo notifica

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

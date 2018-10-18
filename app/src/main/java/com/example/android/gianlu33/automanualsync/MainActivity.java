package com.example.android.gianlu33.automanualsync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

//todo togli poi tutto, logs, cose inutilizzate etc da tutti i files
//todo rivedi note

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private boolean mStatus, mNotifications;
    private int mFrequencyTimeIndex, mTimeSleepTimeIndex;
    private boolean activateSpinnerFrequency = false, activateSpinnerTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TextView textViewStatus;
        final Spinner spinnerFrequency, spinnerTimeSleep;
        final CheckBox checkBoxNotifications;
        final Button buttonEnabler;
        final Resources res = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get views
        textViewStatus = findViewById(R.id.text_view_status);
        spinnerFrequency = findViewById(R.id.spinner_frequency);
        spinnerTimeSleep = findViewById(R.id.spinner_time_sleep);
        checkBoxNotifications = findViewById(R.id.checkbox_notifications);
        buttonEnabler = findViewById(R.id.button_enabler);

        //populate views
        final SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

        mStatus = sharedPreferences.getBoolean(getString(R.string.shared_preferences_status),
                res.getBoolean(R.bool.status_default_value));
        mNotifications = sharedPreferences.getBoolean(getString(R.string.shared_preferences_enable_notifications),
                res.getBoolean(R.bool.enable_notifications_default_value));
        mFrequencyTimeIndex = sharedPreferences.getInt(getString(R.string.shared_preferences_frequency_time_index),
                res.getInteger(R.integer.frequency_time_default_index));
        mTimeSleepTimeIndex = sharedPreferences.getInt(getString(R.string.shared_preferences_time_sleep_time_index),
                res.getInteger(R.integer.time_sleep_time_default_index));

        spinnerFrequency.setSelection(mFrequencyTimeIndex);
        spinnerTimeSleep.setSelection(mTimeSleepTimeIndex);
        checkBoxNotifications.setChecked(mNotifications);

        changeUI(textViewStatus, buttonEnabler, res);

        //listeners
        buttonEnabler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatus = !mStatus;
                sharedPreferences.edit()
                        .putBoolean(res.getString(R.string.shared_preferences_status), mStatus)
                        .apply();

                changeUI(textViewStatus, buttonEnabler, res);
                scheduleJob();
            }
        });

        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!activateSpinnerFrequency) {
                    activateSpinnerFrequency = true;
                    return;
                }

                mFrequencyTimeIndex = i;
                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_frequency_time_index), mFrequencyTimeIndex)
                        .apply();

                Log.v(TAG, "scheduleJob spinnerfrequency");
                scheduleJob();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTimeSleep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!activateSpinnerTime) {
                    activateSpinnerTime = true;
                    return;
                }
                mTimeSleepTimeIndex = i;
                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_time_sleep_time_index), mTimeSleepTimeIndex)
                        .apply();

                Log.v(TAG, "scheduleJob spinner timesleep");
                scheduleJob();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        checkBoxNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mNotifications = b;
                sharedPreferences.edit()
                        .putBoolean(res.getString(R.string.shared_preferences_enable_notifications), mNotifications)
                        .apply();
            }
        });
    }

    private void changeUI(TextView textViewStatus, Button buttonEnabler, Resources res){
        if(mStatus){
            textViewStatus.setText(R.string.text_view_status_enabled);
            textViewStatus.setTextColor(res.getColor(R.color.color_text_view_status_enabled));
            buttonEnabler.setText(R.string.button_text_disable);
        }
        else {
            textViewStatus.setText(R.string.text_view_status_disabled);
            textViewStatus.setTextColor(res.getColor(R.color.color_text_view_status_disabled));
            buttonEnabler.setText(R.string.button_text_enable);
        }
    }

    private void scheduleJob(){
        JobScheduler jobScheduler = getSystemService(JobScheduler.class);
        int jobID = getResources().getInteger(R.integer.auto_manual_sync_job_id);

        if(jobScheduler == null){
            Log.e(TAG, "error, jobScheduler is null");
            return;
        }

        jobScheduler.cancelAll();

        if(mStatus){
            //schedulo job
            ComponentName serviceComponent = new ComponentName(this, AutoManualSyncJob.class);
            JobInfo.Builder builder = new JobInfo.Builder(jobID, serviceComponent);

            int seconds = getResources().getIntArray(R.array.array_frequency_values)[mFrequencyTimeIndex];
            Log.v(TAG, "seconds: " + seconds);

            //todo rivedi un attimo
            builder.setPersisted(true)
                    .setPeriodic(seconds * 1000);

            jobScheduler.schedule(builder.build());

        }
    }

}

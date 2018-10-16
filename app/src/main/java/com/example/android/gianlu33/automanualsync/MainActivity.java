package com.example.android.gianlu33.automanualsync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private boolean mStatus, mNotifications;
    private int mFrequencyTime, mTimeSleepTime, mFrequencyTimeUnit, mTimeSleepTimeUnit;

    //todo quando premo bottone poi devo modificare anche le views oltre a salvare impostazioni, modificare il service e show toast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TextView textViewStatus;
        final EditText editTextFrequency, editTextTimeSleep;
        final Spinner spinnerFrequency, spinnerTimeSleep;
        final CheckBox checkBoxNotifications;
        final Button buttonEnabler;
        final Resources res = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get views
        textViewStatus = findViewById(R.id.text_view_status);
        editTextFrequency = findViewById(R.id.edit_text_frequency);
        editTextTimeSleep = findViewById(R.id.edit_text_time_sleep);
        spinnerFrequency = findViewById(R.id.spinner_frequency);
        spinnerTimeSleep = findViewById(R.id.spinner_time_sleep);
        checkBoxNotifications = findViewById(R.id.checkbox_notifications);
        buttonEnabler = findViewById(R.id.button_enabler);

        //populate views
        final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

        mStatus = sharedPreferences.getBoolean(getString(R.string.shared_preferences_status),
                res.getBoolean(R.bool.status_default_value));
        mNotifications = sharedPreferences.getBoolean(getString(R.string.shared_preferences_enable_notifications),
                res.getBoolean(R.bool.enable_notifications_default_value));
        mFrequencyTime = sharedPreferences.getInt(getString(R.string.shared_preferences_frequency_time),
                res.getInteger(R.integer.frequency_time_default_value));
        mFrequencyTimeUnit = sharedPreferences.getInt(getString(R.string.shared_preferences_frequency_time_unit),
                res.getInteger(R.integer.frequency_time_units_index_default_value));
        mTimeSleepTime = sharedPreferences.getInt(getString(R.string.shared_preferences_time_sleep_time),
                res.getInteger(R.integer.time_sleep_time_default_value));
        mTimeSleepTimeUnit = sharedPreferences.getInt(getString(R.string.shared_preferences_time_sleep_time_unit),
                res.getInteger(R.integer.time_sleep_time_units_index_default_value));

        editTextFrequency.setText(mFrequencyTime + "");
        editTextTimeSleep.setText(mTimeSleepTime + "");
        spinnerFrequency.setSelection(mFrequencyTimeUnit);
        spinnerTimeSleep.setSelection(mTimeSleepTimeUnit);
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
                //todo chiama metodo schedula job
            }
        });

        //todo listener per frequency e timesleep
        //todo aggiorno var globali, salvo sharedpref e chiamo schedulatore
        editTextFrequency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //todo vedere nel caso cancelli il testo cosa fare
                if(charSequence.length() == 0) {
                    editTextFrequency.setText("0");
                    mFrequencyTime = 0;
                }
                else
                    mFrequencyTime = Integer.parseInt(charSequence.toString());

                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_frequency_time), mFrequencyTime)
                        .apply();
                //todo chiama metodo schedula job

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextTimeSleep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //todo vedere nel caso cancelli il testo cosa fare
                if(charSequence.length() == 0) {
                    editTextTimeSleep.setText("0");
                    mTimeSleepTime = 0;
                }
                else
                    mTimeSleepTime = Integer.parseInt(charSequence.toString());

                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_time_sleep_time), mTimeSleepTime)
                        .apply();
                //todo chiama metodo schedula job

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mFrequencyTimeUnit = i;
                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_frequency_time_unit), mFrequencyTimeUnit)
                        .apply();
                //todo chiama metodo schedula job

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTimeSleep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTimeSleepTimeUnit = i;
                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_time_sleep_time_unit), mTimeSleepTimeUnit)
                        .apply();
                //todo chiama metodo schedula job

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
                //todo chiama metodo schedula job

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
        int jobID = AutoManualSyncUtils.jobID;

        if(jobScheduler == null){
            Log.e(TAG, "error, jobScheduler is null");
            return;
        }

        if(mStatus){
            //schedulo job
            ComponentName serviceComponent = new ComponentName(this, AutoManualSyncJob.class);
            JobInfo.Builder builder = new JobInfo.Builder(AutoManualSyncUtils.jobID, serviceComponent);
            //todo setto parametri
            //todo devo ricavare i secondi dalle variabili globali del tempo

            jobScheduler.schedule(builder.build());

        } else {
            //cancello job
            jobScheduler.cancel(jobID);
        }


    }


}

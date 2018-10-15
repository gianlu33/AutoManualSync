package com.example.android.gianlu33.automanualsync;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean mEnabled, mNotifications;
    private int mFrequencyMinutes, mTimeSleepMinutes;

    //todo quando premo bottone poi devo modificare anche le views oltre a salvare impostazioni, modificare il service e show toast

    //todo rivedi variabili globali / locali qui sopra

    //todo vedi qual è l'api deprecata che sto usando
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView textViewStatus, textViewFrequency, textViewTimeSleep, textViewInfos;
        CheckBox checkBoxNotifications;
        Button buttonEnabler;
        Resources res = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get views
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewFrequency = findViewById(R.id.textViewFrequency);
        textViewTimeSleep = findViewById(R.id.textViewTimeSleep);
        textViewInfos = findViewById(R.id.textViewInfos);
        checkBoxNotifications = findViewById(R.id.checkBoxNotifications);
        buttonEnabler = findViewById(R.id.buttonEnabler);

        //populate views
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

        boolean status = sharedPreferences.getBoolean(getString(R.string.shared_preferences_status),
                res.getBoolean(R.bool.status_default_value));
        boolean notifications = sharedPreferences.getBoolean(getString(R.string.shared_preferences_enable_notifications),
                res.getBoolean(R.bool.enable_notifications_default_value));
        int frequencySeconds = sharedPreferences.getInt(getString(R.string.shared_preferences_frequency_seconds),
                res.getInteger(R.integer.frequency_seconds_default_value));
        int frequencyTimeUnit = sharedPreferences.getInt(getString(R.string.shared_preferences_frequency_time_unit),
                res.getInteger(R.integer.frequency_time_units_index_default_value));
        int timeSleepSeconds = sharedPreferences.getInt(getString(R.string.shared_preferences_time_sleep_seconds),
                res.getInteger(R.integer.time_sleep_seconds_default_value));
        int timeSleepTimeUnit = sharedPreferences.getInt(getString(R.string.shared_preferences_time_sleep_time_unit),
                res.getInteger(R.integer.time_sleep_time_units_index_default_value));

        String frequencyFormatted = AutoManualSyncUtils.getTimeFormatted(this, frequencySeconds, frequencyTimeUnit);
        String timeSleepFormatted = AutoManualSyncUtils.getTimeFormatted(this, timeSleepSeconds, timeSleepTimeUnit);

        textViewFrequency.setText(frequencyFormatted);
        textViewTimeSleep.setText(timeSleepFormatted);
        checkBoxNotifications.setChecked(notifications);

        if(status){
            textViewStatus.setText(R.string.text_view_status_enabled);
            textViewStatus.setTextColor(res.getColor(R.color.color_text_view_status_enabled));
            textViewInfos.setText(getString(R.string.text_view_infos_enabled,
                    frequencyFormatted, timeSleepFormatted));
            buttonEnabler.setText(R.string.button_text_disable);
        }
        else {
            textViewStatus.setText(R.string.text_view_status_disabled);
            textViewStatus.setTextColor(res.getColor(R.color.color_text_view_status_disabled));
            textViewInfos.setText(R.string.text_view_infos_disabled);
            buttonEnabler.setText(R.string.button_text_enable);
        }



    }
}

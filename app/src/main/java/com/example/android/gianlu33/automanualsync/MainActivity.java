package com.example.android.gianlu33.automanualsync;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private boolean mStatus, mNotifications;
    private int mFrequencyTimeIndex, mTimeSleepTimeIndex;
    private boolean mActivateSpinnerFrequency = false, mActivateSpinnerTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TextView textViewStatus;
        final Spinner spinnerFrequency, spinnerTimeSleep;
        final CheckBox checkBoxNotifications;
        final Button buttonEnabler;
        final Resources res = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creo notification channel per l'eventuale notifica (creazione ripetuta non ha effetto)
        createNotificationChannel();

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

        changeUI(textViewStatus, buttonEnabler);

        //listeners
        buttonEnabler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatus = !mStatus;
                sharedPreferences.edit()
                        .putBoolean(res.getString(R.string.shared_preferences_status), mStatus)
                        .apply();

                changeUI(textViewStatus, buttonEnabler);
                AutoManualSyncUtils.scheduleJob(MainActivity.this);
            }
        });

        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!mActivateSpinnerFrequency) {
                    mActivateSpinnerFrequency = true;
                    return;
                }

                mFrequencyTimeIndex = i;
                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_frequency_time_index), mFrequencyTimeIndex)
                        .apply();

                AutoManualSyncUtils.scheduleJob(MainActivity.this);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTimeSleep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!mActivateSpinnerTime) {
                    mActivateSpinnerTime = true;
                    return;
                }
                mTimeSleepTimeIndex = i;
                sharedPreferences.edit()
                        .putInt(res.getString(R.string.shared_preferences_time_sleep_time_index), mTimeSleepTimeIndex)
                        .apply();
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

    private void changeUI(TextView textViewStatus, Button buttonEnabler){
        if(mStatus){
            textViewStatus.setText(R.string.text_view_status_enabled);
            textViewStatus.setTextColor(ContextCompat.getColor(this, R.color.color_text_view_status_enabled));
            buttonEnabler.setText(R.string.button_text_disable);
        }
        else {
            textViewStatus.setText(R.string.text_view_status_disabled);
            textViewStatus.setTextColor(ContextCompat.getColor(this, R.color.color_text_view_status_disabled));
            buttonEnabler.setText(R.string.button_text_enable);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getResources().getString(R.string.default_notification_channel_id);

            CharSequence name = getString(R.string.default_notification_channel_name);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if(notificationManager != null)
                notificationManager.createNotificationChannel(channel);
            else
                Log.e(TAG, "notificationManager is null");
        }
    }

    //quando premo il tasto back, non chiudo l'app ma rimane aperta in background (sempre attiva quindi)
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}

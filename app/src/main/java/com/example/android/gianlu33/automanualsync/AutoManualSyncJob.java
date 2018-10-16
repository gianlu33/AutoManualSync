package com.example.android.gianlu33.automanualsync;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class AutoManualSyncJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}

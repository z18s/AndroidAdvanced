package com.example.weatherapp.model.weather;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static com.example.weatherapp.model.DataKey.*;

public class UpdateWorker extends Worker {

    public static final String WORKER_ONETIME_TAG = "OneTimeWorker";
    public static final String WORKER_PERIODIC_TAG = "PeriodicWorker";

    public static final long WORKER_ONETIME_DELAY_SEC = 1;
    public static final long WORKER_PERIODIC_INTERVAL_MIN = 15;

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WeatherRequest weatherRequest = new WeatherRequest();

        String tempString = weatherRequest.getWeatherValues().getTempString();
        Data data = new Data.Builder().putString(TEMPERATURE.getType(), tempString).build();
        return Result.success(data);
    }
}
package com.example.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

public class SensorService {
    private SensorManager sensorManager;
    private List<Sensor> deviceSensors;

    public SensorService(Activity activity) {
        initSensorManager(activity);
        initSensorsList();
    }

    private void initSensorManager(Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
    }

    private void initSensorsList() {
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    public Sensor getTemperatureSensor() {
        return sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    public Sensor getHumiditySensor() {
        return sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }
}
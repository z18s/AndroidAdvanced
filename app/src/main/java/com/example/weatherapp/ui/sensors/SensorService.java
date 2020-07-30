package com.example.weatherapp.ui.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
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

    public void registerListener(SensorEventListener sensorEventListener, Sensor sensor) {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener(SensorEventListener sensorEventListener) {
        sensorManager.unregisterListener(sensorEventListener);
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
package com.example.weatherapp.ui.sensors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Observable;

public class SensorValues extends Observable {

    private SensorService sensorService;
    private Sensor tempSensor;
    private Sensor humSensor;

    private float tempVal = 0.0f;
    private float humVal = 0.0f;

    public SensorValues(Activity activity) {
        init(activity);
    }

    private void init(Activity activity) {
        sensorService = new SensorService(activity);

        tempSensor = sensorService.getTemperatureSensor();
        humSensor = sensorService.getHumiditySensor();
    }

    public void registerListeners(SensorEventListener sensorEventListener) {
        if (tempSensor != null) {
            sensorService.registerListener(sensorEventListener, tempSensor);
        }
        if (humSensor != null) {
            sensorService.registerListener(sensorEventListener, humSensor);
        }
    }

    public void unregisterListeners(SensorEventListener sensorEventListener) {
        sensorService.unregisterListener(sensorEventListener);
    }

    public void update(SensorEvent sensorEvent) {
        if (tempSensor.equals(sensorEvent.sensor)) {
            tempVal = sensorEvent.values[0];
        } else if (humSensor.equals(sensorEvent.sensor)) {
            humVal = sensorEvent.values[0];
        }

        setChanged();
        notifyObservers();
    }

    @SuppressLint("DefaultLocale")
    public String getTempText() {
        return String.format("Temperature: %.1f °C", tempVal);
    }

    @SuppressLint("DefaultLocale")
    public String getHumText() {
        return String.format("Humidity: %.1f %%", humVal);
    }
}
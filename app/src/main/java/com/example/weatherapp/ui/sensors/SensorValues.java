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

    private float tempValue = 0.0f;
    private float humValue = 0.0f;

    private float tempValueAngle;
    private float humValueAngle;

    private final float HUM_VALUE_MIN = 0.0f;
    private final float HUM_VALUE_MAX = 100.0f;
    private final float TEMP_VALUE_MIN = -273.15f;
    private final float TEMP_VALUE_MAX = 100.0f;

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
            tempValue = sensorEvent.values[0];
        } else if (humSensor.equals(sensorEvent.sensor)) {
            humValue = sensorEvent.values[0];
        }

        if (tempValue >= 0) {
            tempValueAngle = -tempValue * 360 / (TEMP_VALUE_MAX - 0);
        } else {
            tempValueAngle = -tempValue * 360 / (0 - TEMP_VALUE_MIN);
        }
        humValueAngle = -humValue * 360 / (HUM_VALUE_MAX - HUM_VALUE_MIN);

        setChanged();
        notifyObservers();
    }

    @SuppressLint("DefaultLocale")
    public String getTempText() {
        return String.format("Temperature: %.1f °C", tempValue);
    }

    @SuppressLint("DefaultLocale")
    public String getHumText() {
        return String.format("Humidity: %.1f %%", humValue);
    }

    public float getTempValue() {
        return tempValue;
    }

    public float getHumValue() {
        return humValue;
    }

    public float getTempValueAngle() {
        return tempValueAngle;
    }

    public float getHumValueAngle() {
        return humValueAngle;
    }
}
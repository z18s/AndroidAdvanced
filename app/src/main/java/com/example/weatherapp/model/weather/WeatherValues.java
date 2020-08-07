package com.example.weatherapp.model.weather;

import android.annotation.SuppressLint;

public class WeatherValues {
    private static final WeatherValues instance = new WeatherValues();

    public static final float ABSOLUTE_ZERO_TEMP = -273.15f;

    private volatile String cityName;
    private volatile float tempValue;
    private volatile String iconName;

    private WeatherValues() {
    }

    public static WeatherValues getInstance() {
        return instance;
    }

    public float getTempValue() {
        return tempValue;
    }

    public void setTempValue(float tempValue) {
        this.tempValue = tempValue;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    @SuppressLint("DefaultLocale")
    public String getTempString() {
        return String.format("Temperature: %.1f", tempValue);
    }

    public String getIconUrl() {
        return String.format("https://openweathermap.org/img/wn/%s@4x.png", iconName);
    }
}
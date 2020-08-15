package com.example.weatherapp.model.weather;

import android.annotation.SuppressLint;

public class WeatherValues {
    private static final WeatherValues instance = new WeatherValues();

    private final float ABSOLUTE_ZERO_TEMP = -273.15f;

    private volatile String cityName;
    private volatile float tempValue = -1.0f;
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
        if (tempValue == -1.0f) {
            return "N/A";
        } else {
            return String.format("Temperature: %.1f", tempValue + ABSOLUTE_ZERO_TEMP);
        }
    }

    public float getTempCelsius() {
        return tempValue + ABSOLUTE_ZERO_TEMP;
    }

    public String getIconUrl() {
        return String.format("https://openweathermap.org/img/wn/%s@4x.png", iconName);
    }
}
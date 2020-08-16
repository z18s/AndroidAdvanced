package com.example.weatherapp.model.constants;

public enum PreferencesNames {
    WEATHER_PREFERENCES("LastWeatherRequest");

    private String name;

    PreferencesNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
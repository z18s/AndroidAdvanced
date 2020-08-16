package com.example.weatherapp.model.constants;

public enum WeatherKeys {
    CITY_NAME ("cityName"),
    COUNTRY_NAME ("countryName"),
    TEMP_VALUE ("tempValue"),
    ICON_NAME ("iconName");

    private String key;

    WeatherKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
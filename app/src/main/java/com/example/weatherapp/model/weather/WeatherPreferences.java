package com.example.weatherapp.model.weather;

import android.content.SharedPreferences;

import static com.example.weatherapp.model.constants.WeatherKeys.*;

public class WeatherPreferences {

    private SharedPreferences lastRequest;

    public WeatherPreferences(SharedPreferences lastRequest) {
        this.lastRequest = lastRequest;
    }

    public void save(String cityName, String countryName, float tempValue, String iconName) {
        SharedPreferences.Editor editor = lastRequest.edit();
        editor.putString(CITY_NAME.getKey(), cityName);
        editor.putString(COUNTRY_NAME.getKey(), countryName);
        editor.putFloat(TEMP_VALUE.getKey(), tempValue);
        editor.putString(ICON_NAME.getKey(), iconName);
        editor.apply();
    }

    public void load() {
        String cityName = lastRequest.getString(CITY_NAME.getKey(), "");
        String countryName = lastRequest.getString(COUNTRY_NAME.getKey(), "");
        float tempValue = lastRequest.getFloat(TEMP_VALUE.getKey(), 0);
        String iconName = lastRequest.getString(ICON_NAME.getKey(), "");

        WeatherValues.getInstance().setWeatherValues(cityName, countryName, tempValue, iconName);
    }
}
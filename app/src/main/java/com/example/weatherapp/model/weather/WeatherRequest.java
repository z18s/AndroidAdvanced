package com.example.weatherapp.model.weather;

import java.util.Random;

// CLASS FOR TEST
public class WeatherRequest {
    private WeatherValues weatherValues;

    public WeatherRequest() {
        int temp = new Random().nextInt(30);
        weatherValues = new WeatherValues(temp);
    }

    public WeatherValues getWeatherValues() {
        return weatherValues;
    }
}
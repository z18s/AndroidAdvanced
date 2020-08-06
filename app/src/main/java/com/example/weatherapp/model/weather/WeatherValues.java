package com.example.weatherapp.model.weather;

public class WeatherValues {
    public static String lastSavedTemp;

    private String tempString = "Temperature: ";

    private int temp;

    public WeatherValues(int temp) {
        this.temp = temp;
        lastSavedTemp = tempString + temp;
    }

    public int getTemp() {
        return temp;
    }

    public String getTempString() {
        return tempString + temp;
    }
}
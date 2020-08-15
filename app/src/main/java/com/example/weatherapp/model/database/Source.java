package com.example.weatherapp.model.database;

import java.util.List;

public class Source {

    private final WeatherDao weatherDao;

    private List<Weather> weather;

    public Source(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public void addWeather(Weather weather) {
        weatherDao.insertWeather(weather);
        LoadWeather();
    }

    public void updateWeather(Weather weather) {
        weatherDao.updateWeather(weather);
        LoadWeather();
    }

    public void removeWeather(long id) {
        weatherDao.deleteWeatherById(id);
        LoadWeather();
    }

    public List<Weather> getWeather() {
        if (weather == null) {
            LoadWeather();
        }
        return weather;
    }

    public void LoadWeather() {
        weather = weatherDao.getAllWeather();
    }

    public List<Weather> getWeatherByCity(String cityName) {
        return weatherDao.getWeatherByCity(cityName);
    }

    public long getCountWeather() {
        return weatherDao.getCountWeather();
    }

    public long getCountWeatherByCity(String cityName) {
        return weatherDao.getCountWeatherByCity(cityName);
    }

    public String getLastDate() {
        return weatherDao.getLastDate();
    }

    public String[] getCitiesByDate(String date) {
        return weatherDao.getCitiesByDate(date);
    }
}
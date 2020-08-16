package com.example.weatherapp.model.database;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    private static App instance;

    private AppDatabase db;
    private final String DATABASE_NAME = "weather_database";

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }
}
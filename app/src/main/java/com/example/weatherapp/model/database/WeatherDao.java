package com.example.weatherapp.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWeather(Weather weather);

    @Update
    void updateWeather(Weather weather);

    @Delete
    void deleteWeather(Weather weather);

    @Query("SELECT id, date, city_name, temperature FROM weather")
    List<Weather> getAllWeather();

    @Query("SELECT COUNT() FROM weather")
    long getCountWeather();

    @Query("SELECT COUNT() FROM weather WHERE city_name = :cityName")
    long getCountWeatherByCity(String cityName);

    @Query("SELECT id, date, city_name, temperature FROM weather WHERE city_name = :cityName")
    List<Weather> getWeatherByCity(String cityName);

    @Query("SELECT id, city_name, temperature FROM weather WHERE date = :date")
    List<Weather> getWeatherByDate(String date);

    @Query("DELETE FROM weather WHERE id = :id")
    void deleteWeatherById(long id);

    @Query("SELECT date FROM weather ORDER BY id DESC LIMIT 1")
    String getLastDate();

    @Query("SELECT city_name FROM weather WHERE date = :date")
    String[] getCitiesByDate(String date);
}
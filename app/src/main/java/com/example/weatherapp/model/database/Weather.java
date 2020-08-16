package com.example.weatherapp.model.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = Weather.DATE)})
public class Weather {

    public final static String ID = "id";
    public final static String DATE = "date";
    public final static String CITY_NAME = "city_name";
    public final static String TEMPERATURE = "temperature";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long id;

    @ColumnInfo(name = DATE)
    public String date;

    @ColumnInfo(name = CITY_NAME)
    public String cityName;

    @ColumnInfo(name = TEMPERATURE)
    public String temperature;
}
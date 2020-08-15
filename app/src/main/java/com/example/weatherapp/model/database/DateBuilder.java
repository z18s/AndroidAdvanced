package com.example.weatherapp.model.database;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBuilder {

    private Date date;

    public DateBuilder() {
        date = new Date();
    }

    public Date getDate() {
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDateString() {
        return new SimpleDateFormat("dd/MM/yyyy HH:00").format(date);
    }
}
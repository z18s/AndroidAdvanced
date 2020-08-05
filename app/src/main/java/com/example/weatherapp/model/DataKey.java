package com.example.weatherapp.model;

public enum DataKey {
    TEMPERATURE ("temperature");

    private String type;

    DataKey(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
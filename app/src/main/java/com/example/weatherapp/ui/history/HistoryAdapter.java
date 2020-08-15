package com.example.weatherapp.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.database.Source;
import com.example.weatherapp.model.database.Weather;
import com.example.weatherapp.model.weather.WeatherValues;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Source dataSource;
    private boolean isFullData;

    public HistoryAdapter(Source dataSource) {
        this(dataSource, true);
    }

    public HistoryAdapter(Source dataSource, boolean isFullData) {
        this.dataSource = dataSource;
        this.isFullData = isFullData;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder viewHolder, int position) {
        List<Weather> weatherList;
        if (isFullData) {
            weatherList = dataSource.getWeather();
        } else {
            weatherList = dataSource.getWeatherByCity(WeatherValues.getInstance().getCityName());
        }
        Weather weather = weatherList.get(position);
        viewHolder.textWeatherDate.setText(weather.date);
        viewHolder.textWeatherCityName.setText(weather.cityName);
        viewHolder.textWeatherTemperature.setText(weather.temperature);
    }

    @Override
    public int getItemCount() {
        if (isFullData) {
            return (int) dataSource.getCountWeather();
        } else {
            return (int) dataSource.getCountWeatherByCity(WeatherValues.getInstance().getCityName());
        }
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textWeatherDate;
        TextView textWeatherCityName;
        TextView textWeatherTemperature;
        View cardView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            cardView = itemView;
            textWeatherDate = cardView.findViewById(R.id.text_weather_date);
            textWeatherCityName = cardView.findViewById(R.id.text_weather_city_name);
            textWeatherTemperature = cardView.findViewById(R.id.text_weather_temperature);
        }
    }
}
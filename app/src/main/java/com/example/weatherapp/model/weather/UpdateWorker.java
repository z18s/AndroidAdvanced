package com.example.weatherapp.model.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.weatherapp.model.database.App;
import com.example.weatherapp.model.database.DateBuilder;
import com.example.weatherapp.model.database.Source;
import com.example.weatherapp.model.database.Weather;
import com.example.weatherapp.model.database.WeatherDao;
import com.example.weatherapp.model.interfaces.OpenWeather;
import com.example.weatherapp.ui.history.HistoryAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.weatherapp.model.constants.PreferencesNames.WEATHER_PREFERENCES;

public class UpdateWorker extends Worker {
    public static final String WORKER_ONETIME_TAG = "OneTimeWorker";
    public static final String WORKER_PERIODIC_TAG = "PeriodicWorker";

    public static final long WORKER_ONETIME_DELAY_SEC = 1;
    public static final long WORKER_PERIODIC_INTERVAL_MIN = 15;

    private final String KEY_API = "8a4f2513a83690c3e3741076c4029169";
    private String cityName = "London,uk";

    private OpenWeather openWeather = null;
    private volatile boolean isUpdated = false;
    private volatile WeatherPreferences weatherPreferences;
    private volatile SharedPreferences lastRequest;

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        lastRequest = context.getSharedPreferences(WEATHER_PREFERENCES.getName(), Context.MODE_PRIVATE);
        weatherPreferences = new WeatherPreferences(lastRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("DEBUG_UpdateWorker", "doWork");

        isUpdated = false;

        if (openWeather == null) {
            initRetrofit();
        }

        requestRetrofit(cityName);

        while (!isUpdated) {
            Thread.yield();
        }

        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("DEBUG_UpdateWorker", "onStopped");
    }

    private void initRetrofit() {
        Log.d("DEBUG_UpdateWorker", "initRetrofit");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    private void requestRetrofit(String city) {

        Log.d("DEBUG_UpdateWorker", "requestRetrofit");

        openWeather.loadWeather(city, KEY_API)
                .enqueue(new Callback<WeatherRequest>() {

                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {

                        Log.d("DEBUG_UpdateWorker", "requestRetrofit.onResponse");

                        WeatherRequest weatherRequest = response.body();
                        if (weatherRequest != null) {
                            initWeatherValues(weatherRequest);

                            Log.d("DEBUG_UpdateWorker", "requestRetrofit.onResponse - OK!");
                        }
                        isUpdated = true;
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        Log.d("DEBUG_UpdateWorker", "requestRetrofit.onFailure! " + call.toString());
                    }
                });
    }

    private void initWeatherValues(WeatherRequest weatherRequest) {
        String cityName = weatherRequest.getName();
        String countryName = weatherRequest.getSys().getCountry();
        float tempValue = weatherRequest.getMain().getTemp();
        String iconName = weatherRequest.getWeather().get(0).getIcon();

        WeatherValues.getInstance().setWeatherValues(cityName, countryName, tempValue, iconName);

        weatherPreferences.save(cityName, countryName, tempValue, iconName);

        sendWeatherToDatabase();
    }

    @SuppressLint("DefaultLocale")
    private void sendWeatherToDatabase() {

        String currentDate = new DateBuilder().getDateString();
        String currentCity = WeatherValues.getInstance().getCityName();

        Weather weather = new Weather();
        weather.date = currentDate;
        weather.cityName = currentCity;
        weather.temperature = String.format("%.1f", WeatherValues.getInstance().getTempCelsius());

        WeatherDao weatherDao = App
                .getInstance()
                .getWeatherDao();
        Source dataSource = new Source(weatherDao);

        String lastDate = dataSource.getLastDate();

        // Проверка на наличие в БД записи с текущей датой
        if (currentDate.equals(lastDate)) {
            String[] cities = dataSource.getCitiesByDate(lastDate);
            int i = 0;
            for (String city : cities) {
                if (currentCity.equals(city)) {
                    i++;
                    break;
                }
            }
            if (i == 0) {
                // Запись в БД, если текущая дата есть, но нет текущего города
                dataSource.addWeather(weather);

                Log.d("DEBUG_UpdateWorker", "sendWeatherToDatabase (city) - " +
                        weather.id + " - " + weather.date + " - " + weather.cityName + " - " + weather.temperature);
            }
        } else {
            // Запись в БД, если текущей даты нет
            dataSource.addWeather(weather);

            Log.d("DEBUG_UpdateWorker", "sendWeatherToDatabase (date) - " +
                    weather.id + " - " + weather.date + " - " + weather.cityName + " - " + weather.temperature);
        }

        new HistoryAdapter(dataSource).notifyDataSetChanged();
    }
}
package com.example.weatherapp.model.weather;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.weatherapp.model.interfaces.OpenWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateWorker extends Worker {
    public static final String WORKER_ONETIME_TAG = "OneTimeWorker";
    public static final String WORKER_PERIODIC_TAG = "PeriodicWorker";

    public static final long WORKER_ONETIME_DELAY_SEC = 1;
    public static final long WORKER_PERIODIC_INTERVAL_MIN = 15;

    private static final String KEY_API = "8a4f2513a83690c3e3741076c4029169";
    private static String cityName = "London,uk";

    private static OpenWeather openWeather = null;
    private static volatile boolean isUpdated = false;

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("DEBUG_UpdateWorker", "doWork");

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
        // HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //
        // OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // httpClient.addInterceptor(logging);

        Log.d("DEBUG_UpdateWorker", "initRetrofit");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                // .client(httpClient.build())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    private void requestRetrofit(String city) {

        Log.d("DEBUG_UpdateWorker", "requestRetrofit");
        isUpdated = false;

        openWeather.loadWeather(city, KEY_API)
                .enqueue(new Callback<WeatherRequest>() {

                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {

                        Log.d("DEBUG_UpdateWorker", "requestRetrofit.onResponse");

                        WeatherRequest weatherRequest = response.body();
                        if (weatherRequest != null) {

                            String cityName = weatherRequest.getName();
                            String countryName = weatherRequest.getSys().getCountry();
                            WeatherValues.getInstance().setCityName(cityName + ", " + countryName);

                            float temp = weatherRequest.getMain().getTemp() + WeatherValues.ABSOLUTE_ZERO_TEMP;
                            WeatherValues.getInstance().setTempValue(temp);

                            String iconName = weatherRequest.getWeather().get(0).getIcon();
                            WeatherValues.getInstance().setIconName(iconName);

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
}
package com.example.weatherapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.model.weather.WeatherValues;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mTemp;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(WeatherValues.getInstance().getCityName());
        mTemp = new MutableLiveData<>();
        mTemp.setValue(WeatherValues.getInstance().getTempString());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getTemp() {
        return mTemp;
    }
}
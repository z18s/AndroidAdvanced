package com.example.weatherapp.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.database.App;
import com.example.weatherapp.model.database.Source;
import com.example.weatherapp.model.database.WeatherDao;

public class HistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView(view, true);
        initListeners(view);
    }

    private void initRecyclerView(View view, boolean isFullData) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_history);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        WeatherDao weatherDao = App
                .getInstance()
                .getWeatherDao();
        Source dataSource = new Source(weatherDao);
        RecyclerView.Adapter adapter = new HistoryAdapter(dataSource, isFullData);
        recyclerView.setAdapter(adapter);
    }

    private void initListeners(View view) {
        Button buttonCurrentCityData = view.findViewById(R.id.history_current_city_button);
        Button buttonAllData = view.findViewById(R.id.history_all_button);

        buttonCurrentCityData.setOnClickListener((v1) -> {
            initRecyclerView(view, false);
        });

        buttonAllData.setOnClickListener((v2) -> {
            initRecyclerView(view, true);
        });
    }
}
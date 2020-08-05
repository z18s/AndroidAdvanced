package com.example.weatherapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.weatherapp.R;
import com.example.weatherapp.model.weather.IWorker;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        final TextView homeText = view.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), homeText::setText);

        final TextView homeTemp = view.findViewById(R.id.temp_value);
        homeViewModel.getTemp().observe(getViewLifecycleOwner(), homeTemp::setText);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        IWorker worker = (IWorker) context;
        worker.startOneTimeWorker();
    }
}
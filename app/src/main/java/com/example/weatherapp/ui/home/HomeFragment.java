package com.example.weatherapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

    IWorker worker;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        TextView homeText = view.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), homeText::setText);

        TextView homeTemp = view.findViewById(R.id.temp_value);
        homeViewModel.getTemp().observe(getViewLifecycleOwner(), homeTemp::setText);

        Log.d("DEBUG_HomeFragment", "onViewCreated");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        worker = (IWorker) context;
        worker.startOneTimeWorker();
        Log.d("DEBUG_HomeFragment", "startOneTimeWorker");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (worker != null) {
            worker.cancelOneTimeWorker();
            Log.d("DEBUG_HomeFragment", "cancelOneTimeWorker");
        }
    }
}
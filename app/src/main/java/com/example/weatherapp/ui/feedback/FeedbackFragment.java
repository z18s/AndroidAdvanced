package com.example.weatherapp.ui.feedback;

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

public class FeedbackFragment extends Fragment {

    private FeedbackViewModel feedbackViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedbackViewModel = ViewModelProviders.of(this).get(FeedbackViewModel.class);
        final TextView textView = view.findViewById(R.id.text_feedback);
        feedbackViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }
}
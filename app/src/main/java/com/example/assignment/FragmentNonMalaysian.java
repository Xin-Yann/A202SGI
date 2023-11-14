package com.example.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class FragmentNonMalaysian extends Fragment {
    TimePicker timePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_non_malaysian, container, false);

        return view;
    }
    public Map<String, Object> getFragmentMalaysianData() {
        Map<String, Object> passenger = new HashMap<>();
        View view = getView();
        String passPassportNumber = ((TextInputEditText) view.findViewById(R.id.inputPassportNumber)).getText().toString();

        timePicker = view.findViewById(R.id.selectPassportExpiry);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String passportExpiry = String.format("%02d:%02d", hour, minute);

        // Add the data to the map
        passenger.put("pass_passportNumber", passPassportNumber);
        passenger.put("pass_passportExpiry", passportExpiry);

        return passenger;
    }
}

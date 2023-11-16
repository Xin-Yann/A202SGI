package com.example.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FragmentNonMalaysian extends Fragment {
    DateFormat datePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_non_malaysian, container, false);


        return view;
    }
    public Map<String, Object> getFragmentNonMalaysianData() {
        Map<String, Object> passenger = new HashMap<>();
        View view = getView();

        String passName = ((TextInputEditText) view.findViewById(R.id.inputName)).getText().toString();
        String passPassportNumber = ((TextInputEditText) view.findViewById(R.id.inputPassportNumber)).getText().toString();

        // Add the data to the map
        passenger.put("pass_name", passName);
        passenger.put("pass_passportNumber", passPassportNumber);

        return passenger;
    }

    public void updateFragmentNonMalaysianData(Map<String, Object> selectedPassenger) {
        if (getView() != null) {
            // Update the data in FragmentNonMalaysian based on the selected passenger
            String passName = (String) selectedPassenger.get("pass_name");
            String passPassportNumber = (String) selectedPassenger.get("pass_passportNumber");

            // Update the TextInputEditText fields in the fragment
            TextInputEditText inputedName = getView().findViewById(R.id.inputName);
            TextInputEditText inputedPassportNumber = getView().findViewById(R.id.inputPassportNumber);

            // Set the values to the corresponding fields
            inputedName.setText(passName);
            inputedPassportNumber.setText(passPassportNumber);
        }
    }

}

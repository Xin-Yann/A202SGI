package com.example.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class FragmentMalaysian extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_malaysian, container, false);

        return view;
    }

    public Map<String, Object> getFragmentMalaysianData() {
        Map<String, Object> passenger = new HashMap<>();
        View view = getView();

        String passName = ((TextInputEditText) view.findViewById(R.id.inputName)).getText().toString();
        String passIcNumber = ((TextInputEditText) view.findViewById(R.id.inputIcNumber)).getText().toString();

        passenger.put("pass_name", passName);
        passenger.put("pass_icNumber", passIcNumber);

        return passenger;
    }

    public void updateFragmentMalaysianData(Map<String, Object> selectedPassenger) {
        if (getView() != null) {
            // Update the data in based on the selected passenger
            String passName = (String) selectedPassenger.get("pass_name");
            String passIcNumber = (String) selectedPassenger.get("pass_icNumber");

            // Update the TextInput fields
            TextInputEditText inputedName = getView().findViewById(R.id.inputName);
            TextInputEditText inputedIcNumber = getView().findViewById(R.id.inputIcNumber);

            inputedName.setText(passName);
            inputedIcNumber.setText(passIcNumber);
        }
    }
}

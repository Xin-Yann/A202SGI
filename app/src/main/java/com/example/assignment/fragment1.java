package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class fragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        // Find the button and set an OnClickListener
        Button toTicket = view.findViewById(R.id.selectTicket);
        toTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Select_depart_ticket activity
                Intent intent = new Intent(getActivity(), Select_depart_ticket.class);
                startActivity(intent);
            }
        });

        return view;
    }
/*    public void toTicket() {
        // Retrieve the values entered in the TextInputEditText fields
        TextInputEditText originEditText = getView().findViewById(R.id.inputOrigin);
        TextInputEditText destinationEditText = getView().findViewById(R.id.inputDes);
        TextInputEditText departureEditText = getView().findViewById(R.id.inputDepart);
        TextInputEditText arrivalEditText = getView().findViewById(R.id.inputArrival);
        TextInputEditText paxEditText = getView().findViewById(R.id.inputPax);

        String origin = originEditText.getText().toString();
        String destination = destinationEditText.getText().toString();
        String departure = departureEditText.getText().toString();
        String arrival = arrivalEditText.getText().toString();
        String pax = paxEditText.getText().toString();

        // Create an Intent to navigate to the Select_depart_ticket activity
        Intent intent = new Intent(getActivity(), Select_depart_ticket.class);

        // Pass the search criteria as extras to the new activity
        intent.putExtra("origin", origin);
        intent.putExtra("destination", destination);
        intent.putExtra("departure", departure);
        intent.putExtra("arrival", arrival);
        intent.putExtra("pax", pax);

        startActivity(intent);
    }*/

}

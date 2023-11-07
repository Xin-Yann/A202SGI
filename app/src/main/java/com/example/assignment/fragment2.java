package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class fragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

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
}

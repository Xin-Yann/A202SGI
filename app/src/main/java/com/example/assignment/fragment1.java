package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class fragment1 extends Fragment {

    ArrayList<North> datalist;
    NorthAdapter adapter;
    TextInputLayout inputLayout;
    TextInputEditText inputOrigin, inputDes, inputDate, inputPax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        datalist = new ArrayList<>();
        adapter = new NorthAdapter(getContext(), datalist); // Use getContext() to get the Context

        // Find the search button and set an OnClickListener
        Button searchButton = view.findViewById(R.id.selectTicket);
        AppData.isDepartTicketSelected = true;
        // Find the TextInputEditText within the fragment's view
        inputLayout = view.findViewById(R.id.inputOrigin);
        inputOrigin = inputLayout.findViewById(R.id.inputOriginText);

        inputLayout = view.findViewById(R.id.inputDes);
        inputDes = inputLayout.findViewById(R.id.inputDesText);

        inputLayout = view.findViewById(R.id.inputDepart);
        inputDate = inputLayout.findViewById(R.id.inputDepart1);

        inputLayout = view.findViewById(R.id.inputPax);
        inputPax = inputLayout.findViewById(R.id.inputPax1);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppData.isReturnTicketAllowed = false;
                AppData.isDepartTicketSelected = true;
                // Retrieve the train name entered by the user
                String trainOrigin = inputOrigin.getText().toString();
                String trainDes = inputDes.getText().toString();
                String trainDate = inputDate.getText().toString();
                String trainPax = inputPax.getText().toString();

                // Perform a search based on the train name
                filterList(trainOrigin, trainDes);

                // Start the Select_depart_ticket activity
                Intent intent = new Intent(getContext(), Select_depart_ticket.class);
                intent.putExtra("search_query", trainOrigin);
                intent.putExtra("search_destination", trainDes);
                intent.putExtra("search_date", trainDate);
                intent.putExtra("search_pax", trainPax);
                startActivity(intent);
            }
        });

        return view;
    }

    public void filterList(String origin, String destination) {
        List<North> filteredList = new ArrayList<>();

        if (origin.isEmpty() && destination.isEmpty()) {
            // If both queries are empty, show all items
            filteredList.addAll(datalist);
        } else {
            for (North north : datalist) {
                // Check both origin and destination for a match
                if (north.getName().toLowerCase().contains(origin.toLowerCase())
                        && north.getName().toLowerCase().contains(destination.toLowerCase())) {
                    filteredList.add(north);
                }
            }
        } 

        // Update the adapter with the filtered or all items
        adapter.setFilteredList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }
}


/*package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class fragment1 extends Fragment {

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
}*/

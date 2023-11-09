package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class fragment1 extends Fragment {

    ArrayList<North> datalist;
    NorthAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        datalist = new ArrayList<>();
        adapter = new NorthAdapter(getContext(), datalist); // Use getContext() to get the Context

        // Find the search button and set an OnClickListener
        Button searchButton = view.findViewById(R.id.selectTicket);

        // Find the TextInputEditText within the fragment's view
        TextInputLayout inputLayout = view.findViewById(R.id.inputOrigin);  // Find the TextInputLayout
        TextInputEditText inputOrigin = inputLayout.findViewById(R.id.inputOriginText);  // Find the TextInputEditText within the TextInputLayout

        AppData.isReturnTicketAllowed = false;
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Select_depart_ticket activity
                Intent intent = new Intent(getContext(), Select_depart_ticket.class);
                startActivity(intent);
                // Retrieve the train name entered by the user
                String trainName = inputOrigin.getText().toString();

                // Perform a search based on the train name
                searchByTrainName(trainName);
            }
        });

        return view;
    }

    private void searchByTrainName(String trainName) {
        List<North> filteredList = new ArrayList<>();

        // Iterate through the datalist and filter by train name
        for (North name : datalist) {
            if (name.getName().toLowerCase().contains(trainName.toLowerCase())) {
                filteredList.add(name);
            }
        }

        // Update the adapter with the filtered data
        adapter.updateData(filteredList);
        adapter.notifyDataSetChanged();
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

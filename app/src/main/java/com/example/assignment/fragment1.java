package com.example.assignment;

import android.content.Context; // Import the Context class
import android.os.Bundle;
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


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

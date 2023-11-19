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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class fragment1 extends Fragment {

    ArrayList<North> datalist;
    NorthAdapter adapter;
    TextInputLayout inputLayout;
    TextInputEditText inputOrigin, inputDes, inputDate, inputPax;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        datalist = new ArrayList<>();
        adapter = new NorthAdapter(getContext(), datalist);
        fStore = FirebaseFirestore.getInstance();

        Button searchButton = view.findViewById(R.id.selectTicket);

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

                // Retrieve the train name entered by the user
                String trainOrigin = inputOrigin.getText().toString();
                String trainDes = inputDes.getText().toString();
                String trainDate = inputDate.getText().toString();
                String trainPax = inputPax.getText().toString();

                // Perform a search based on the train name
                fetchAndFilterData(trainOrigin, trainDes, trainDate, trainPax);
            }
        });

        return view;
    }

    private void fetchAndFilterData(final String trainOrigin, final String trainDes,
                                    final String trainDate, final String trainPax) {
        fStore.collection("northbound")
                .orderBy("id", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            datalist.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String stationName = document.getString("name");
                                String stationDuration = document.getString("diffMin");

                                North northStation = new North(stationName, stationDuration);
                                datalist.add(northStation);
                            }

                            // Datalist is populated, call filterList
                            filterList(trainOrigin, trainDes);

                            Intent intent = new Intent(getContext(), Select_depart_ticket.class);
                            intent.putExtra("search_query", trainOrigin);
                            intent.putExtra("search_destination", trainDes);
                            intent.putExtra("search_date", trainDate);
                            intent.putExtra("search_pax", trainPax);
                            startActivity(intent);

                        } else {
                            Log.e("Fragment1", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void filterList(String origin, String destination) {
        List<North> filteredList = new ArrayList<>();

        if (origin.isEmpty() && destination.isEmpty()) {
            // If both queries are empty, show all items
            filteredList.addAll(datalist);
        } else {
            for (North north : datalist) {
                // Check for a partial match in either origin or destination (case-insensitive)
                String northName = north.getName().toLowerCase();
                if (northName.contains(origin.toLowerCase()) || northName.contains(destination.toLowerCase())) {
                    filteredList.add(north);
                }
            }
        }

        // Update the adapter with the filtered or all items
        adapter.setFilteredList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }

        // Log the results for debugging
        Log.d("Filtering", "User Input - Origin: " + origin + ", Destination: " + destination);
        Log.d("Filtering", "Filtered List Size: " + filteredList.size());
        for (North north : filteredList) {
            Log.d("Filtering", "Filtered Station Name: " + north.getName());
        }
    }
}
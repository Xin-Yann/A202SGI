package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.assignment.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Select_return_ticket extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    ArrayList<North> datalist;
    NorthAdapter adapter;
    TextView trainOri, trainDestination, trainD, trainP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_return_ticket);

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.return_ticket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new NorthAdapter(this, datalist);
        recyclerView.setAdapter(adapter);

        Intent returnIntent = getIntent();

        if (returnIntent.hasExtra("search_query")) {
            String trainOrigin =returnIntent.getStringExtra("search_query");
            String trainDes = returnIntent.getStringExtra("search_destination");
            String trainDate = returnIntent.getStringExtra("search_date");
            String trainPax = returnIntent.getStringExtra("search_pax");
            // Now you have the trainName, you can use it as needed.

            // For example, you might want to update UI elements based on the trainName:
            trainOri = findViewById(R.id.origin);
            trainOri.setText(trainOrigin);

            trainDestination = findViewById(R.id.destination);
            trainDestination.setText(trainDes);

            trainD = findViewById(R.id.date1);
            trainD.setText(trainDate);

            trainP = findViewById(R.id.pax);
            trainP.setText("Total: "+ trainPax + " Pax");

        }

        fStore.collection("northbound")
                .orderBy("id", Query.Direction.ASCENDING)  // Replace "customField" with the field you want to use for ordering
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            datalist.clear(); // Clear the existing data in datalist

                            // Inside the onComplete method of your Firestore query
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String stationName = document.getString("name");
                                String stationDuration = document.getString("diffMin");

                                // Create a 'North' object and add it to datalist
                                North northStation = new North(stationName, stationDuration);
                                datalist.add(northStation);
                            }

                            // Create the unique station pairs and avoid duplicates
                            Set<String> uniquePairs = new HashSet<>();
                            List<North> uniqueDatalist = new ArrayList<>();

                            for (int i = 0; i < datalist.size(); i++) {
                                for (int j = i + 1; j < datalist.size(); j++) {
                                    North origin = datalist.get(i);
                                    North destination = datalist.get(j);

                                    String forwardPair = origin.getName() + " ----------- " + destination.getName();
                                    String reversePair = destination.getName() + " ---------- " + origin.getName();
                                    double totalDuration = 0.0;

                                    // Calculate the sum of diffMin for the elements in the pair
                                    for (int k = i; k <= j; k++) {
                                        totalDuration += Double.parseDouble(datalist.get(k).getDuration());
                                    }

                                    // Add both the forward and reverse pairs along with total duration
                                    uniquePairs.add(forwardPair);
                                    uniquePairs.add(reversePair);

                                    uniqueDatalist.add(new North(forwardPair, String.format("%.2f", totalDuration)));
                                    uniqueDatalist.add(new North(reversePair, String.format("%.2f", totalDuration)));
                                }
                            }

                            // Replace datalist with the uniqueDatalist
                            datalist.clear();
                            datalist.addAll(uniqueDatalist);

                            // Update the adapter with the unique station pairs
                            adapter.updateData(datalist);
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the case where the query was not successful
                        }
                    }
                });

    }

    public void toSeat(View view){
        Intent intent = new Intent(Select_return_ticket.this, Select_seat_a.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Select_depart_ticket.class);
        ImageButton back = findViewById(R.id.back);
        startActivity(intent);
    }

    /*Footer*/
    public void toHomePage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton toHomePage = findViewById(R.id.homePage);
        startActivity(intent);
    }

    public void toBookingHistory(View view){
        Intent intent = new Intent(this, Booking_history.class);
        ImageButton toBookingHistory = findViewById(R.id.booking_history);
        startActivity(intent);
    }

    public void toAccountPage(View view){
        Intent intent = new Intent(this, Account.class);
        ImageButton toAccountPage = findViewById(R.id.profilePage);
        startActivity(intent);
    }

}

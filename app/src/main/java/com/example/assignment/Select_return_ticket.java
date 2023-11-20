package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private String departureTime;
    private String arrivalTime;

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
            String trainOrigin = returnIntent.getStringExtra("search_query");
            String trainDes = returnIntent.getStringExtra("search_destination");
            String trainArr = returnIntent.getStringExtra("search_arr");
            String trainPax = returnIntent.getStringExtra("search_pax");

            Log.d("Select_return_ticket", "Received data: Origin=" + trainOrigin + ", Destination=" + trainDes + ", Date=" + trainArr + ", Pax=" + trainPax);


            // Update UI with retrieved data
            trainOri = findViewById(R.id.origin);
            trainOri.setText(trainOrigin);

            trainDestination = findViewById(R.id.destination);
            trainDestination.setText(trainDes);

            trainD = findViewById(R.id.date1);
            trainD.setText(trainArr);  // Corrected line

            trainP = findViewById(R.id.pax);
            trainP.setText("Total: " + trainPax + " Pax");

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


                                datalist.clear();
                                datalist.addAll(uniqueDatalist);

                                adapter.updateData(datalist);
                                adapter.notifyDataSetChanged();
                                retrieveFilteredData(trainOrigin, trainDes);
                            } else {
                                // Handle the case where the query was not successful
                            }
                        }
                    });
            retrieveFilteredData(trainOrigin, trainDes);
        }
    }
    private void retrieveFilteredData(String originName, String destinationName) {
        // Filter original datalist based on the provided information
        List<North> filteredDataList = new ArrayList<>();

        for (North north : datalist) {
            if (north.getName().contains(originName) && north.getName().contains(destinationName)) {
                filteredDataList.add(north);
            }
        }
        // Set the filtered data to the adapter
        adapter.setFilteredList(filteredDataList);
    }

    public void toSeat(View view) {
        Intent intent = getIntent();
        if (intent.hasExtra("search_query")) {
            String trainOrigin = intent.getStringExtra("search_query");
            String trainDes = intent.getStringExtra("search_destination");
            String trainDate = intent.getStringExtra("search_date");
            String trainPax = intent.getStringExtra("search_pax");
            String trainArr = intent.getStringExtra("search_arr");

            String stationName = "";
            String totalDuration = "";

            if (view instanceof CardView) {
                CardView clickedCard = (CardView) view;
                TextView nameTextView = clickedCard.findViewById(R.id.name);
                TextView durationTextView = clickedCard.findViewById(R.id.duration);

                stationName = nameTextView.getText().toString();
                totalDuration = durationTextView.getText().toString();
            }

            if (stationName.contains("-----------")) {
                String[] names = stationName.split("-----------");


                saveDataToSharedPreferences(names[0].trim(), names[1].trim(), totalDuration, trainDate, trainArr, trainPax, departureTime, arrivalTime);


                startSeatActivity(Select_seat_a.class);
            } else if (stationName.contains("----------")) {
                String[] names = stationName.split("----------");


                saveDataToSharedPreferences(names[0].trim(), names[1].trim(), totalDuration, trainDate, trainArr, trainPax, departureTime, arrivalTime);

                startSeatActivity(Select_return_seat_a.class);
            }

            Intent passDataIntent = new Intent(Select_return_ticket.this, Select_return_seat_a.class);
            passDataIntent.putExtra("search_query", trainOrigin);
            passDataIntent.putExtra("search_destination", trainDes);
            passDataIntent.putExtra("search_date", trainDate);
            passDataIntent.putExtra("search_pax", trainPax);
            passDataIntent.putExtra("search_arr", trainArr);
            passDataIntent.putExtra("departureTime", departureTime);
            passDataIntent.putExtra("arrivalTime", arrivalTime);
            startActivity(passDataIntent);
        }
    }

    private void saveDataToSharedPreferences(String originName, String destinationName, String totalDuration, String trainDate, String trainArr, String trainPax, String departureTime, String arrivalTime) {
        Log.d("Select_depart_ticket", "Saving data to SharedPreferences");

        // Save data to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("originName", originName);
        editor.putString("destinationName", destinationName);
        editor.putString("totalDuration", totalDuration);
        editor.putString("trainDate", trainDate);
        editor.putString("trainArr", trainArr);
        editor.putString("trainPax", trainPax);
        editor.putString("departureTime", departureTime);
        editor.putString("arrivalTime", arrivalTime);
        editor.apply();
    }

    private void startSeatActivity(Class<?> destinationClass) {
        Intent intent = new Intent(this, destinationClass);
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
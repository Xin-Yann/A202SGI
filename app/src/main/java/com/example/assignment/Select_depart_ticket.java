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

public class Select_depart_ticket extends AppCompatActivity {
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
        setContentView(R.layout.select_depart_ticket);

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.searchResultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new NorthAdapter(this, datalist);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();

        if (intent.hasExtra("search_query")) {
            String trainOrigin = intent.getStringExtra("search_query");
            String trainDes = intent.getStringExtra("search_destination");
            String trainDate = intent.getStringExtra("search_date");
            String trainPax = intent.getStringExtra("search_pax");


            trainOri = findViewById(R.id.origin);
            trainOri.setText(trainOrigin);

            trainDestination = findViewById(R.id.destination);
            trainDestination.setText(trainDes);

            trainD = findViewById(R.id.date1);
            trainD.setText(trainDate);

            trainP = findViewById(R.id.pax);
            trainP.setText("Total: " + trainPax + " Pax");

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

                                Set<String> uniquePairs = new HashSet<>();
                                List<North> uniqueDatalist = new ArrayList<>();

                                for (int i = 0; i < datalist.size(); i++) {
                                    for (int j = i + 1; j < datalist.size(); j++) {
                                        North origin = datalist.get(i);
                                        North destination = datalist.get(j);

                                        String forwardPair = origin.getName() + " ----------- " + destination.getName();
                                        String reversePair = destination.getName() + " ---------- " + origin.getName();

                                        double totalDuration = 0.0;
                                        String initialDepartureTime = "08:00";
                                        departureTime = calculateArrivalTime(initialDepartureTime, totalDuration);


                                        for (int k = i; k <= j; k++) {
                                            totalDuration += Double.parseDouble(datalist.get(k).getDuration());
                                        }

                                        arrivalTime = calculateArrivalTime(departureTime, totalDuration);
                                        String formattedDuration = formattedDuration(totalDuration);


                                        uniquePairs.add(forwardPair);
                                        uniquePairs.add(reversePair);

                                        uniqueDatalist.add(new North(forwardPair, formattedDuration, departureTime, arrivalTime));
                                        uniqueDatalist.add(new North(reversePair, formattedDuration, departureTime, arrivalTime));
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
        }
    }

    private String calculateArrivalTime(String initialDepartureTime, double totalDuration) {
        int initialDepartureMinutes = convertToMinutes(initialDepartureTime);

        // Calculate total minutes
        int totalMinutes = initialDepartureMinutes + (int) totalDuration;

        // Calculate hours and remaining minutes
        int hours = totalMinutes / 60;
        int remainingMinutes = totalMinutes % 60;

        // Return the formatted string
        return String.format("%02d:%02d", hours, remainingMinutes);
    }

    private String formattedDuration(double totalDuration) {
        // Calculate total minutes
        int totalMinutes = (int) totalDuration;

        // Calculate hours and remaining minutes
        int hours = totalMinutes / 60;
        int remainingMinutes = totalMinutes % 60;

        // Return the formatted string
        if (remainingMinutes > 0) {
            return String.format("%d hrs %02d mins", hours, remainingMinutes);
        } else {
            return String.format("%02d hrs", hours);
        }
    }

    private int convertToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    private String convertToTimeFormat(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        return String.format("%02d:%02d", hours, remainingMinutes);
    }

    public void toSeat(View view) {
        Intent intent = getIntent();

        if (intent.hasExtra("search_query")) {
            String trainOrigin = intent.getStringExtra("search_query");
            String trainDes = intent.getStringExtra("search_destination");
            String trainDate = intent.getStringExtra("search_date");
            String trainPax = intent.getStringExtra("search_pax");

            String stationName = "";
            String totalDuration = "";

            // Check if the view is a CardView
            if (view instanceof CardView) {
                CardView clickedCard = (CardView) view;
                TextView nameTextView = clickedCard.findViewById(R.id.name);
                TextView durationTextView = clickedCard.findViewById(R.id.duration);

                stationName = nameTextView.getText().toString();
                totalDuration = durationTextView.getText().toString();
            }

            if (stationName.contains("-----------")) {
                String[] names = stationName.split("-----------");

                // Save data to SharedPreferences
                saveDataToSharedPreferences(names[0].trim(), names[1].trim(), totalDuration, trainDate, trainPax, departureTime, arrivalTime);

                // Start the seat activity
                startSeatActivity(Select_seat_a.class);
            } else if (stationName.contains("----------")) {
                String[] names = stationName.split("----------");

                // Save data to SharedPreferences
                saveDataToSharedPreferences(names[0].trim(), names[1].trim(), totalDuration, trainDate, trainPax,departureTime, arrivalTime);

                // Start the return seat activity
                startSeatActivity(Select_return_seat_a.class);
            }

            Intent passDataIntent = new Intent(Select_depart_ticket.this, Select_seat_a.class);
            passDataIntent.putExtra("search_query", trainOrigin);
            passDataIntent.putExtra("search_destination", trainDes);
            passDataIntent.putExtra("search_date", trainDate);
            passDataIntent.putExtra("search_pax", trainPax);
            passDataIntent.putExtra("departureTime", departureTime);
            passDataIntent.putExtra("arrivalTime", arrivalTime);
            startActivity(passDataIntent);

        }
    }

    private void retrieveFilteredData(String originName, String destinationName) {
        // Filter your original data list (datalist) based on the provided information
        List<North> filteredDataList = new ArrayList<>();

        for (North north : datalist) {
            if (north.getName().contains(originName) && north.getName().contains(destinationName)) {
                filteredDataList.add(north);
            }
        }

        // Set the filtered data to the adapter
        adapter.setFilteredList(filteredDataList);
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton back = findViewById(R.id.back);
        startActivity(intent);
        finish();
    }

    /* Footer */
    public void toHomePage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton toHomePage = findViewById(R.id.homePage);
        startActivity(intent);
    }

    public void toBookingHistory(View view) {
        Intent intent = new Intent(this, Booking_history.class);
        ImageButton toBookingHistory = findViewById(R.id.booking_history);
        startActivity(intent);
    }

    public void toAccountPage(View view) {
        Intent intent = new Intent(this, Account.class);
        ImageButton toAccountPage = findViewById(R.id.profilePage);
        startActivity(intent);
    }

    private void saveDataToSharedPreferences(String originName, String destinationName, String totalDuration, String trainDate, String trainPax, String departureTime, String arrivalTime) {
        Log.d("Select_depart_ticket", "Saving data to SharedPreferences");

        // Save data to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("originName", originName);
        editor.putString("destinationName", destinationName);
        editor.putString("totalDuration", totalDuration);
        editor.putString("trainDate", trainDate);
        editor.putString("trainPax", trainPax);
        editor.putString("departureTime", departureTime);
        editor.putString("arrivalTime", arrivalTime);
        editor.apply();
    }

    private void startSeatActivity(Class<?> destinationClass) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }
}
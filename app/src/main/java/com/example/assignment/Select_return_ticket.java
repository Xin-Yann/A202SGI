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

                                    String forwardPair = origin.getName() + " ---> " + destination.getName();
                                    String reversePair = destination.getName() + " ---> " + origin.getName();
                                    double totalDuration = 0.0;

                                    String initialDepartureTime = "08:00";
                                    String departureTime = calculateArrivalTime(initialDepartureTime, totalDuration);

                                    for (int k = i + 1; k <= j; k++) {
                                        totalDuration += Double.parseDouble(datalist.get(k).getDuration());
                                    }

                                    String arrivalTime = calculateArrivalTime(departureTime, totalDuration);
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
                        } else {
                            // Handle the case where the query was not successful
                        }
                    }
                });
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
        if (hours > 0) {
            return String.format("%d hrs %02d mins", hours, remainingMinutes);
        }else{
            return String.format("%02d mins", remainingMinutes);
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


    public void toSeat(View view){
        Intent intent = new Intent(Select_return_ticket.this, Select_return_seat_a.class);
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

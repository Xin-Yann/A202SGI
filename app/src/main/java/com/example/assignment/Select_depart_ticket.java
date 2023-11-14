package com.example.assignment;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
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
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_depart_ticket);

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.depart);
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
            // Now you have the trainName, you can use it as needed.

            /*filterList(trainOrigin, trainDes);*/

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


        /*if (!AppData.searchButtonClicked) {
            // Access denied, show an error message and finish the activity
            Toast.makeText(this, "Access denied. Please click the search button in the first activity first.", Toast.LENGTH_SHORT).show();
            finish();
        }*/


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



    public void toSeat(View view) {

        Intent intent = getIntent();
        if (intent.hasExtra("search_query")) {
            String trainOrigin = intent.getStringExtra("search_query");
            String trainDes = intent.getStringExtra("search_destination");
            String trainDate = intent.getStringExtra("search_date");
            String trainPax = intent.getStringExtra("search_pax");
            // Now you have the trainName, you can use it as needed.

            Intent passDataIntent = new Intent(Select_depart_ticket.this, Select_seat_a.class);
            passDataIntent.putExtra("search_query", trainOrigin);
            passDataIntent.putExtra("search_destination", trainDes);
            passDataIntent.putExtra("search_date", trainDate);
            passDataIntent.putExtra("search_pax", trainPax);
            startActivity(passDataIntent);
        }
    }


    /* public void filterList(String origin, String destination) {
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

         CardView clickedCard = (CardView) view;
         TextView nameTextView = clickedCard.findViewById(R.id.name);
         TextView durationTextView = clickedCard.findViewById(R.id.duration);

         String stationName = nameTextView.getText().toString();
         String totalDuration = durationTextView.getText().toString(); // Assuming total duration is displayed in the TextView

         if (stationName.contains("-----------")) {
             String[] names = stationName.split("-----------");

             // Save data to SharedPreferences
             saveDataToSharedPreferences(names[0].trim(), names[1].trim(), totalDuration);

             // Start the seat activity
             startSeatActivity(Select_seat_a.class);
         } else if (stationName.contains("----------")) {
             String[] names = stationName.split("----------");


             // Save data to SharedPreferences
             saveDataToSharedPreferences(names[0].trim(), names[1].trim(), totalDuration);

             // Start the return seat activity
             startSeatActivity(Select_return_seat_a.class);
         }
     }

     private void saveDataToSharedPreferences(String originName, String destinationName, String totalDuration) {
         SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
         SharedPreferences.Editor editor = preferences.edit();
         editor.putString("originName", originName);
         editor.putString("destinationName", destinationName);
         editor.putString("totalDuration", totalDuration);
         editor.apply();
     }

 */
    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton back = findViewById(R.id.back);
        startActivity(intent);
        finish();
    }


        private void startSeatActivity(Class<?> destinationClass) {
            Intent intent = new Intent(this, destinationClass);
            startActivity(intent);

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
    }
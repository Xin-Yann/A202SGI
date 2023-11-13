package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

                                    for (int k = i; k <= j; k++) {
                                        totalDuration += Double.parseDouble(datalist.get(k).getDuration());
                                    }

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
                        } else {
                            // Handle the case where the query was not successful
                        }
                    }
                });
    }

    public void toSeat(View view) {
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


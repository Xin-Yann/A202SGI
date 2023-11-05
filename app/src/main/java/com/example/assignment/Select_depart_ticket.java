package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
               .orderBy("id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            datalist.clear(); // Clear the existing data in datalist

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String stationName = document.getString("name");

                                // Create a 'North' object and add it to datalist
                                North northStation = new North(stationName);
                                datalist.add(northStation);
                            }

                            // Create the unique station pairs and avoid duplicates
                            Set<String> uniquePairs = new HashSet<>();
                            List<North> uniqueDatalist = new ArrayList<>();

                            for (int i = 0; i < datalist.size(); i++) {
                                for (int j = i + 1; j < datalist.size(); j++) {
                                    North origin = datalist.get(i);
                                    North destination = datalist.get(j);
                                    String pair = origin.getName() + " ---------- " + destination.getName();

                                    if (uniquePairs.add(pair)) {
                                        uniqueDatalist.add(new North(pair));
                                    }
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


    public void toSeat(View view) {
        Intent intent = new Intent(Select_depart_ticket.this, Select_seat_a.class);
        startActivity(intent);
    }

    /*Footer*/
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

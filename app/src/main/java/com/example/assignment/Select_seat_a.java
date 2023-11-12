package com.example.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Select_seat_a extends AppCompatActivity {
    private List<String> selectedSeats = new ArrayList<>(); // List to store selected seats
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_seat_a);
    }

    public void premiumSeatClicked(View view) {
        ImageButton premiumSeatButton = (ImageButton) view;
        String seatId = premiumSeatButton.getContentDescription().toString();
        AppData.isDepartTicketSelected = true;

        // Check if the seat is already selected or not
        if (isSeatSelected(seatId)) {
            // Seat is already selected, show a confirmation dialog
            showSeatConfirmationDialog(seatId);
        } else {
            // Seat is not selected, change the image and mark it as selected
            premiumSeatButton.setImageResource(R.drawable.selected_seat);
            // Add the seat to the selected seats list
            addSelectedSeat(seatId);
            // Show the confirmation dialog immediately after selecting the seat
            showSeatConfirmationDialog(seatId);
        }
    }

    private boolean isSeatSelected(String seatId) {
        // Implement your logic to check if the seat is already selected
        // You can use the selectedSeats list to track selected seats
        return selectedSeats.contains(seatId);
    }

    private void showSeatConfirmationDialog(final String seatId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Seat " + seatId + " is already selected. Do you want to confirm this seat?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle seat confirmation (e.g., save the selected seat to a database)
                if (AppData.isReturnTicketAllowed) {
                    Intent returnIntent = new Intent(Select_seat_a.this, Select_return_ticket.class);

                    // Get the data from the returnIntent
                    Intent passDataIntent = getIntent();
                    String trainOrigin = passDataIntent.getStringExtra("search_query");
                    String trainDes = passDataIntent.getStringExtra("search_destination");
                    String trainDate = passDataIntent.getStringExtra("search_date");
                    String trainPax = passDataIntent.getStringExtra("search_pax");

                    // Put the data into the returnIntent
                    returnIntent.putExtra("search_query", trainOrigin);
                    returnIntent.putExtra("search_destination", trainDes);
                    returnIntent.putExtra("search_date", trainDate);
                    returnIntent.putExtra("search_pax", trainPax);

                    startActivity(returnIntent);
                } else {
                    navigateToPassengerDetailsPage();
                }


            }
        });

        // Add a negative button for "No"
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the case when the user doesn't confirm the seat selection
                // You can add any specific logic here if needed
            }
        });

        builder.show();
    }

    private void navigateToPassengerDetailsPage() {
        setContentView(R.layout.passenger_details_start); // Load the passenger_details_start.xml layout
        // You may need to handle any other UI logic specific to this layout

    }


    private void addSelectedSeat(String seatId) {
        // Implement your logic to add the selected seat to the list
        // You can use the selectedSeats list to track selected seats
        selectedSeats.add(seatId);
    }


    /*public void back(View view) {
        Intent intent = new Intent(this, Select_depart_ticket.class);
        startActivity(intent);
        finish(); // Close the current activity
    }*/




}

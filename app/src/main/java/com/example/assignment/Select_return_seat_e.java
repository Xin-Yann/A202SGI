package com.example.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
public class Select_return_seat_e extends AppCompatActivity {

    private List<String> selectedSeats = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_return_seat_e);

        ScrollView scrollView = findViewById(R.id.scroll_view);

        ScrollViewHelper scrollViewHelper = new ScrollViewHelper();
        scrollViewHelper.setOnTouchListener(scrollView, this);
    }

    public void toSeat_a(View view){
        Intent intent = new Intent(this, Select_seat_a.class);
        ImageButton toCoach_a = findViewById(R.id.Coach_a);
        startActivity(intent);
    }

    public void toSeat_b(View view){
        Intent intent = new Intent(this, Select_seat_b.class);
        ImageButton toCoach_b = findViewById(R.id.Coach_b);
        startActivity(intent);
    }

    public void toSeat_c(View view){
        Intent intent = new Intent(this, Select_seat_c.class);
        ImageButton toCoach_c = findViewById(R.id.Coach_c);
        startActivity(intent);
    }

    public void toSeat_d(View view){
        Intent intent = new Intent(this, Select_seat_d.class);
        ImageButton toCoach_d = findViewById(R.id.Coach_d);
        startActivity(intent);
    }

    public void toSeat_e(View view){
        Intent intent = new Intent(this, Select_seat_e.class);
        ImageButton toCoach_e = findViewById(R.id.Coach_e);
        startActivity(intent);
    }

    public void toCoachD(View view) {
        Intent intent = new Intent(this, Select_seat_d.class);
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


    public void normalSeatClicked(View view) {
        ImageButton normalSeatButton = (ImageButton) view;
        String seatId = normalSeatButton.getContentDescription().toString();

        // Check if the seat is already selected or not
        if (isSeatSelected(seatId)) {
            // Seat is already selected, show a confirmation dialog
            showSeatConfirmationDialog(seatId);
        } else {
            // Seat is not selected, change the image and mark it as selected
            normalSeatButton.setImageResource(R.drawable.selected_seat);
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
                // You can also navigate to a confirmation page/activity here
                navigateToPassengerDetailsPage();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User chose not to confirm, do nothing or handle as needed
            }
        });
        builder.create().show();
    }

    private void addSelectedSeat(String seatId) {
        // Implement your logic to add the selected seat to the list
        // You can use the selectedSeats list to track selected seats
        selectedSeats.add(seatId);
    }

    private void navigateToPassengerDetailsPage() {
        setContentView(R.layout.passenger_details_start); // Load the passenger_details_start.xml layout
        // You may need to handle any other UI logic specific to this layout
    }


}

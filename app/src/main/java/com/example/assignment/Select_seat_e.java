package com.example.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Select_seat_e extends AppCompatActivity {

    private List<String> selectedSeats = new ArrayList<>();

    private String originName;
    private String destinationName;
    private String totalDuration;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_seat_e);

        ScrollView scrollView = findViewById(R.id.scroll_view);

        ScrollViewHelper scrollViewHelper = new ScrollViewHelper();
        scrollViewHelper.setOnTouchListener(scrollView, this);
        retrieveDataFromSharedPreferences();
        // Update UI with retrieved data
        updateUI();
        db = FirebaseFirestore.getInstance();
        calculateAndDisplayPrice();
        for (int i = 1; i <= 5; i++) {
            for (char c = 'a'; c <= 'd'; c++) {
                String seatId = "normal_seat_" + i + c;
                int resourceId = getResources().getIdentifier(seatId, "id", getPackageName());
                ImageButton seatButton = findViewById(resourceId);

                // Check and display seat availability when the UI is launched
                checkAndDisplaySeatAvailability(seatId, seatButton);
            }
        }
    }

    private void calculateAndDisplayPrice() {
        // Calculate and display the price using the utility method
        TextView priceTextView = findViewById(R.id.price);
        PriceCalculatorUtil.calculateAndDisplayPrice(originName, destinationName, priceTextView, db);
    }

    private void retrieveDataFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        originName = preferences.getString("originName", "");
        destinationName = preferences.getString("destinationName", "");
        totalDuration = preferences.getString("totalDuration", "");
    }

    private void updateUI() {
        // Update your UI with originName, destinationName, and totalDuration as needed
        // For example, you can set the text of TextViews
        TextView originTextView = findViewById(R.id.origin);
        TextView destinationTextView = findViewById(R.id.destination);
        TextView durationTextView = findViewById(R.id.duration);

        originTextView.setText(originName);
        destinationTextView.setText(destinationName);
        durationTextView.setText(totalDuration);
    }

    public void toSeat_a(View view){
        Intent intent = new Intent(this, Select_seat_a.class);
        ImageButton toCoach_a = findViewById(R.id.Coach_a);
        Select_seat.startNextSeatActivity(this, Select_seat_a.class, originName, destinationName, totalDuration);
        startActivity(intent);
    }

    public void toSeat_b(View view){
        Intent intent = new Intent(this, Select_seat_b.class);
        ImageButton toCoach_b = findViewById(R.id.Coach_b);
        Select_seat.startNextSeatActivity(this, Select_seat_b.class, originName, destinationName, totalDuration);
        startActivity(intent);
    }

    public void toSeat_c(View view){
        Intent intent = new Intent(this, Select_seat_c.class);
        ImageButton toCoach_c = findViewById(R.id.Coach_c);
        Select_seat.startNextSeatActivity(this, Select_seat_c.class, originName, destinationName, totalDuration);
        startActivity(intent);
    }

    public void toSeat_d(View view){
        Intent intent = new Intent(this, Select_seat_d.class);
        ImageButton toCoach_d = findViewById(R.id.Coach_d);
        Select_seat.startNextSeatActivity(this, Select_seat_d.class, originName, destinationName, totalDuration);
        startActivity(intent);
    }

    public void toSeat_e(View view){
        Intent intent = new Intent(this, Select_seat_e.class);
        ImageButton toCoach_e = findViewById(R.id.Coach_e);
        Select_seat.startNextSeatActivity(this, Select_seat_e.class, originName, destinationName, totalDuration);
        startActivity(intent);
    }

    public void toCoachD(View view) {
        Intent intent = new Intent(this, Select_seat_d.class);
        Select_seat.startNextSeatActivity(this, Select_seat_d.class, originName, destinationName, totalDuration);
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


    private void checkAndDisplaySeatAvailability(final String seatId, final ImageButton normalSeatButton) {
        Log.d("Seat_Id_Debug", "Seat_Id: " + seatId);

        // Extract seat ID from ImageButton content description
        String buttonSeatId = normalSeatButton.getContentDescription().toString();

        // Query the Firestore database to check if the seat is already reserved
        db.collection("departseat")
                .whereEqualTo("seat_id", buttonSeatId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // If the seat is already reserved, update UI accordingly
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Seat is reserved, update UI to show it as unavailable
                        normalSeatButton.setImageResource(R.drawable.unavailable_seat);
                        normalSeatButton.setEnabled(false);  // Disable the button to prevent further clicks
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(Select_seat_e.this, "Error checking seat availability", Toast.LENGTH_SHORT).show();
                });
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
                saveSelectedSeat(seatId);

                if (AppData.isReturnTicketAllowed) {
                    Intent returnIntent = new Intent(Select_seat_e.this, Select_return_ticket.class);
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

    private void saveSelectedSeat(String seatId) {
        // Extract seat details from seatId
        String seatType = extractSeatType(seatId);
        String seatNo = extractSeatNo(seatId);
        String seatCoach = extractSeatCoach(seatId);

        // Create a Map to represent the seat data
        Map<String, Object> seatData = new HashMap<>();
        seatData.put("seat_id", seatId);
        seatData.put("seat_type", seatType);
        seatData.put("seat_no", seatNo);
        seatData.put("seat_coach", seatCoach);

        // Save the seat data to the Firestore database
        db.collection("departseat")
                .add(seatData)
                .addOnSuccessListener(documentReference -> {
                    // Seat data added successfully
                    Toast.makeText(Select_seat_e.this, "Seat data added to database", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(Select_seat_e.this, "Error adding seat data to database", Toast.LENGTH_SHORT).show();
                });
    }


    private String extractSeatNo(String seatId) {
        // Extract seat number directly from seatId
        // Assuming seat number is part of the seat_id
        String[] parts = seatId.split(" ");

        // Find and return the seat number part
        for (String part : parts) {
            if (part.matches("[0-9][A-Z]*")) {
                return part;
            }
        }

        // Return a default value or handle the case when the seat number cannot be determined
        return "unknown";
    }

    private String extractSeatType(String seatId) {
        // Extract seat type directly from seatId
        // Assuming seat type is part of the seat_id
        String[] parts = seatId.split(" ");

        // Find and return the seat type part
        for (String part : parts) {
            if (part.equalsIgnoreCase("normal") || part.equalsIgnoreCase("oku") || part.equalsIgnoreCase("premium")) {
                return part.toLowerCase() + " seat";
            }
        }

        // Return a default value or handle the case when the seat type cannot be determined
        return "unknown type";
    }

    private String extractSeatCoach(String seatId) {
        // Extract coach information directly from seatId
        // Assuming coach information is part of the seat_id
        String[] parts = seatId.split(" ");

        // Find and return the coach information part
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("Coach")) {
                return "Coach " + parts[i + 1];
            }
        }

        // Return a default value or handle the case when the coach information cannot be determined
        return "unknown coach";
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

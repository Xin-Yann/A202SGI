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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Select_return_seat_e extends AppCompatActivity {

    private List<String> selectedSeats = new ArrayList<>();

    private String originName;
    private String destinationName;
    private String totalDuration;
    private String trainDate;
    private String trainPax;
    private String departureTime;

    private String arrivalTime;

    private FirebaseFirestore db;

    private TextView priceTextView;

    private Select_seat select_seat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_return_seat_e);

        ScrollView scrollView = findViewById(R.id.scroll_view);

        ScrollViewHelper scrollViewHelper = new ScrollViewHelper();
        scrollViewHelper.setOnTouchListener(scrollView, this);
        Select_seat.handleSeatActivity(this);

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
        priceTextView = findViewById(R.id.price);
        PriceCalculatorUtil.calculateAndDisplayPrice(originName, destinationName, priceTextView, db);
    }

    private void retrieveDataFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        originName = preferences.getString("originName", "");
        destinationName = preferences.getString("destinationName", "");
        totalDuration = preferences.getString("totalDuration", "");
        trainDate = preferences.getString("trainDate", "");
        trainPax = preferences.getString("trainPax", "");
        departureTime = preferences.getString("departureTime", "");
        arrivalTime = preferences.getString("arrivalTime", "");
    }

    private void updateUI() {

        TextView originTextView = findViewById(R.id.origin);
        TextView destinationTextView = findViewById(R.id.destination);
        TextView durationTextView = findViewById(R.id.duration);
        TextView departTextView = findViewById(R.id.depart_time);
        TextView arriveTextView = findViewById(R.id.arrive_time);

        originTextView.setText(originName);
        destinationTextView.setText(destinationName);
        durationTextView.setText(totalDuration);
        departTextView.setText(departureTime);
        arriveTextView.setText(arrivalTime);
    }

    public void toSeat_a(View view) {
        Intent intent = new Intent(this, Select_return_seat_a.class);
        ImageButton toCoach_a = findViewById(R.id.Coach_a);
        Select_seat.startNextSeatActivity(this, Select_return_seat_a.class, originName, destinationName, totalDuration, trainDate, trainPax, departureTime, arrivalTime);
        startActivity(intent);
    }

    public void toSeat_b(View view) {
        Intent intent = new Intent(this, Select_return_seat_b.class);
        ImageButton toCoach_b = findViewById(R.id.Coach_b);
        Select_seat.startNextSeatActivity(this, Select_return_seat_b.class, originName, destinationName, totalDuration, trainDate, trainPax, departureTime, arrivalTime);
        startActivity(intent);
    }

    public void toSeat_c(View view) {
        Intent intent = new Intent(this, Select_return_seat_c.class);
        ImageButton toCoach_c = findViewById(R.id.Coach_c);
        Select_seat.startNextSeatActivity(this, Select_return_seat_c.class, originName, destinationName, totalDuration, trainDate, trainPax, departureTime, arrivalTime);
        startActivity(intent);
    }

    public void toSeat_d(View view) {
        Intent intent = new Intent(this, Select_return_seat_d.class);
        ImageButton toCoach_d = findViewById(R.id.Coach_d);
        Select_seat.startNextSeatActivity(this, Select_return_seat_d.class, originName, destinationName, totalDuration, trainDate, trainPax, departureTime, arrivalTime);
        startActivity(intent);
    }

    public void toSeat_e(View view) {
        Intent intent = new Intent(this, Select_return_seat_e.class);
        ImageButton toCoach_e = findViewById(R.id.Coach_e);
        Select_seat.startNextSeatActivity(this, Select_return_seat_e.class, originName, destinationName, totalDuration, trainDate, trainPax, departureTime, arrivalTime);
        startActivity(intent);
    }

    public void toCoachD(View view) {
        Intent intent = new Intent(this, Select_return_seat_d.class);
        Select_seat.startNextSeatActivity(this, Select_return_seat_d.class, originName, destinationName, totalDuration, trainDate, trainPax, departureTime, arrivalTime);
        startActivity(intent);
    }

    public void toCoachA(View view) {
        Intent intent = new Intent(this, Select_return_seat_a.class);
        Select_seat.startNextSeatActivity(this, Select_return_seat_a.class, originName, destinationName, totalDuration, trainDate, trainPax, departureTime, arrivalTime);
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
            if (selectedSeats.size() <= Integer.parseInt(trainPax)) {

                normalSeatButton.setImageResource(R.drawable.selected_seat);

                addSelectedSeat(seatId);

                showSeatConfirmationDialog(seatId);
            } else {
                // Maximum number of seats reached, notify the user
                Toast.makeText(this, "You have already selected the maximum number of seats.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void checkAndDisplaySeatAvailability(final String seatId, final ImageButton normalSeatButton) {
        Log.d("Seat_Id_Debug", "Seat_Id: " + seatId);


        String buttonSeatId = normalSeatButton.getContentDescription().toString();

        // Query the Firestore database to check if the seat is already reserved
        db.collection("returnseat")
                .whereEqualTo("train_date", trainDate)
                .whereEqualTo("seat_id", buttonSeatId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        normalSeatButton.setImageResource(R.drawable.unavailable_seat);
                        normalSeatButton.setEnabled(false);  // Disable the button to prevent further clicks
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(Select_return_seat_e.this, "Error checking seat availability", Toast.LENGTH_SHORT).show();
                });
    }



    private boolean isSeatSelected(String seatId) {

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
                    Intent returnIntent = new Intent(Select_return_seat_e.this, Select_return_ticket.class);
                    startActivity(returnIntent);
                } else {
                    // Check if the required number of seats is selected
                    if (selectedSeats.size() == Integer.parseInt(trainPax)) {
                        // Navigate to the passenger details page
                        navigateToPassengerDetailsPage();
                    } else {

                    }
                }
            }
        });

        // Add a negative button for "No"
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void saveSelectedSeat(String seatId) {
        // Extract seat details from seatId
        String seatType = extractSeatType(seatId);
        String seatNo = extractSeatNo(seatId);
        String seatCoach = extractSeatCoach(seatId);


        priceTextView = findViewById(R.id.price);
        String originalPriceStr = priceTextView.getText().toString();


        String numericPart = originalPriceStr.replaceAll("[^\\d.]", "");


        double originalPrice = Double.parseDouble(numericPart);


        double doubledPrice = originalPrice;


        String seatPrice = String.valueOf(doubledPrice);

        // Create a Map to represent the seat data
        Map<String, Object> seatData = new HashMap<>();
        seatData.put("seat_id", seatId);
        seatData.put("seat_type", seatType);
        seatData.put("seat_no", seatNo);
        seatData.put("seat_coach", seatCoach);
        seatData.put("train_date", trainDate);
        seatData.put("user_email", getCurrentUserEmail());
        seatData.put("seat_price", seatPrice);

        seatData.put("origin_name", originName);
        seatData.put("destination_name", destinationName);
        seatData.put("total_duration", totalDuration);
        seatData.put("depart_time", departureTime);
        seatData.put("arrival_time", arrivalTime);

        // Save the seat data to the Firestore database
        db.collection("returnseat")
                .add(seatData)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(Select_return_seat_e.this, "Seat data added to database", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(Select_return_seat_e.this, "Error adding seat data to database", Toast.LENGTH_SHORT).show();
                });
    }

    private String getCurrentUserEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        return user != null ? user.getEmail() : null;
    }


    private String extractSeatNo(String seatId) {

        String[] parts = seatId.split(" ");

        // Find and return the seat number part
        for (String part : parts) {
            if (part.matches("[0-9][A-Z]*")) {
                return part;
            }
        }

        return "unknown";
    }

    private String extractSeatType(String seatId) {

        String[] parts = seatId.split(" ");

        // Find and return the seat type part
        for (String part : parts) {
            if (part.equalsIgnoreCase("normal") || part.equalsIgnoreCase("oku") || part.equalsIgnoreCase("premium")) {
                return part.toLowerCase() + " seat";
            }
        }


        return "unknown type";
    }

    private String extractSeatCoach(String seatId) {

        String[] parts = seatId.split(" ");


        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("Coach")) {
                return "Coach " + parts[i + 1];
            }
        }


        return "unknown coach";
    }


    private void addSelectedSeat(String seatId) {

        selectedSeats.add(seatId);
    }

    private void navigateToPassengerDetailsPage() {
        if ("1".equals(trainPax)) {
            launchPassengerDetailsEnd();
        } else {
            launchPassengerDetailsStart();
        }
    }

    private void launchPassengerDetailsStart() {
        Intent intent = new Intent(this, passengerDetailsStart.class);
        startActivity(intent);
        finish();
    }

    private void launchPassengerDetailsEnd() {
        Intent intent = new Intent(this, passengerDetailsEnd.class);
        startActivity(intent);
        finish();
    }

    public void back(View view) {
        Intent intent = new Intent(this, Select_return_ticket.class);
        ImageButton back = findViewById(R.id.back);
        startActivity(intent);
    }

}

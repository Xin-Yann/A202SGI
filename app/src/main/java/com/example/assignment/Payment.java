package com.example.assignment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
    FirebaseFirestore firestore;
    RadioGroup radioGroup;
    FrameLayout cardFragmentContainer;
    FrameLayout tngFragmentContainer;
    Fragment currentFragment;
    Map<String, Integer> seatTypeCountMap;
    TextView premiumTicketAmount;
    TextView normalTicketAmount;
    TextView premiumTicketPrice;
    TextView normalTicketPrice;
    TextView confirmTotalAmount;
    Button payBtn;
    ImageButton back;

    double totalPremiumPrice = 0.0;
    double totalNormalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_1);

        firestore = FirebaseFirestore.getInstance();
        seatTypeCountMap = new HashMap<>();

        fetchSeatData();
        displaySeatCounts();

        premiumTicketAmount = findViewById(R.id.premiumTicketAmount);
        normalTicketAmount = findViewById(R.id.normalTicketAmount);
        premiumTicketPrice = findViewById(R.id.premiumTicketPrice);
        normalTicketPrice = findViewById(R.id.normalTicketPrice);
        confirmTotalAmount = findViewById(R.id.confirmTotalAmount);
        payBtn = findViewById(R.id.payBtn);

        radioGroup = findViewById(R.id.radioGroup);
        cardFragmentContainer = findViewById(R.id.cardFragmentContainer);
        tngFragmentContainer = findViewById(R.id.tngFragmentContainer);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Remove the current fragment
                if (currentFragment != null) {
                    transaction.remove(currentFragment);
                }

                // Check which radio button is selected
                if (checkedId == R.id.card) {
                    currentFragment = new FragmentCard();
                    transaction.replace(R.id.cardFragmentContainer, currentFragment);
                } else if (checkedId == R.id.ewallet) {
                    currentFragment = new FragmentTng();
                    transaction.replace(R.id.tngFragmentContainer, currentFragment);
                } else {
                    currentFragment = null;
                }

                transaction.commit();
            }
        });
    }

    private void fetchSeatData() {
        String currentUserEmail = getCurrentUserEmail();

        if (currentUserEmail != null) {
            // Fetch data from "departseat" collection
            fetchSeatDataForCollection("departseat", currentUserEmail);

            // Fetch data from "returnseat" collection
            fetchSeatDataForCollection("returnseat", currentUserEmail);
        }
    }

    private void fetchSeatDataForCollection(String collectionName, String currentUserEmail) {
        firestore.collection(collectionName)
                .whereEqualTo("user_email", currentUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Check if the document has a "status" field
                                if (document.contains("status")) {
                                    String status = document.getString("status");

                                    // Skip documents with status "paid"
                                    if ("paid".equals(status)) {
                                        continue;
                                    }
                                }

                                // Get seat_price and seat_type
                                String seatPriceString = document.getString("seat_price");
                                double seatPrice = Double.parseDouble(seatPriceString);
                                String seatType = document.getString("seat_type");
                                Log.e(TAG, "Seat Type: " + seatType + ", Seat Price: " + seatPrice);

                                // Exclude the "status" field from the document data
                                Map<String, Object> seatDataWithoutStatus = document.getData();
                                seatDataWithoutStatus.remove("status");

                                // Update the seat type count
                                updateSeatTypeCount(seatType);

                                // Update the total price based on seat type
                                if ("premium seat".equals(seatType)) {
                                    totalPremiumPrice += seatPrice;
                                } else if ("normal seat".equals(seatType)) {
                                    totalNormalPrice += seatPrice;
                                }
                            }

                            // Display the seat counts
                            displaySeatCounts();

                            // Check if both "departseat" and "returnseat" have been processed
                            if (collectionName.equals("departseat") || collectionName.equals("returnseat")) {
                                // Display the total prices after processing all documents
                                displayTotalPrices(totalPremiumPrice, totalNormalPrice);
                            }

                        } else {
                            // Handle errors
                            Toast.makeText(Payment.this, "Error getting seat data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void displayTotalPrices(double totalPremiumPrice, double totalNormalPrice) {
        double orderTotal = totalPremiumPrice + totalNormalPrice;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                premiumTicketPrice.setText(String.format("RM %.2f", totalPremiumPrice));
                normalTicketPrice.setText(String.format("RM %.2f", totalNormalPrice));
                confirmTotalAmount.setText(String.format("RM %.2f", orderTotal));
            }
        });
    }

    private void updateSeatTypeCount(String seatType) {
        // Check if the seat type is already in the map
        if (seatTypeCountMap.containsKey(seatType)) {
            // If yes, increment the count
            int count = seatTypeCountMap.get(seatType);
            seatTypeCountMap.put(seatType, count + 1);
        } else {
            // If no, add the seat type to the map with the current count
            seatTypeCountMap.put(seatType, 1);
        }
    }

    private void displaySeatCounts() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Process seat type counts
                for (Map.Entry<String, Integer> entry : seatTypeCountMap.entrySet()) {
                    String seatType = entry.getKey();
                    int count = entry.getValue();

                    // Update TextViews based on seat type
                    if ("premium seat".equals(seatType)) {
                        premiumTicketAmount.setText(String.valueOf(count));
                    } else if ("normal seat".equals(seatType)) {
                        normalTicketAmount.setText(String.valueOf(count));
                    }
                }
            }
        });
    }

    private String getCurrentUserEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        return user != null ? user.getEmail() : null;
    }

    public void toPayment3(View view) {
        String currentUserEmail = getCurrentUserEmail();

        if (currentUserEmail != null) {
            // Generate the order ID
            long orderId = generateOrderId();

            String paymentMethod;
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId == R.id.card) {
                paymentMethod = "Card";
            } else if (selectedRadioButtonId == R.id.ewallet) {
                paymentMethod = "eWallet";
            } else {
                paymentMethod = "Unknown";
            }
            double totalOrderPrice = totalPremiumPrice + totalNormalPrice;

            createOrder(currentUserEmail, seatTypeCountMap, totalOrderPrice, paymentMethod, orderId);

            Intent intent = new Intent(this, Payment3.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "User email is null", Toast.LENGTH_SHORT).show();
        }
    }

    private long generateOrderId() {
        return System.currentTimeMillis() + (long) (Math.random() * 1000);
    }

    private void updateCounter() {
        // Fetch the current counter value
        firestore.collection("counters")
                .document("orderCounter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                long currentCounter = (long) document.get("counter");
                                // Increment the counter for the next order
                                updateCounterValue(currentCounter + 1);
                            } else {
                                Log.e(TAG, "Counter document does not exist");
                            }
                        } else {
                            Log.e(TAG, "Error getting counter value", task.getException());
                        }
                    }
                });
    }

    private void updateCounterValue(long newValue) {
        // Update the counter value in the "counters" collection
        firestore.collection("counters")
                .document("orderCounter")
                .update("counter", newValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Counter updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating counter", e);
                    }
                });
    }

    private void createOrder(String currentUserEmail, Map<String, Integer> seatTypeCountMap, double totalOrderPrice, String paymentMethod, long orderId) {
        firestore.collection("order")
                .document(String.valueOf(orderId)) // Use the provided order ID
                .set(createOrderData(currentUserEmail, seatTypeCountMap, totalOrderPrice, paymentMethod))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Order created with ID: " + orderId);

                        // Update counter for next order
                        updateCounter();

                        // Update the status for the "departseat" collection
                        updateSeatStatus("departseat", currentUserEmail);

                        // Update the status for the "returnseat" collection
                        updateSeatStatus("returnseat", currentUserEmail);

                        Toast.makeText(Payment.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Log.e(TAG, "Error creating order", e);
                        Toast.makeText(Payment.this, "Error creating order", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateSeatStatus(String collectionName, String currentUserEmail) {
        firestore.collection(collectionName)
                .whereEqualTo("user_email", currentUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                updateStatusForSeat(collectionName, document.getId());
                            }
                        } else {
                            Log.e(TAG, "Error getting seat data for " + collectionName, task.getException());
                        }
                    }
                });
    }

    private void updateStatusForSeat(String collectionName, String documentId) {
        firestore.collection(collectionName)
                .document(documentId)
                .update("status", "paid")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Seat status updated successfully for " + collectionName + ", Document ID: " + documentId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating seat status for " + collectionName + ", Document ID: " + documentId, e);
                    }
                });
    }

    private Map<String, Object> createOrderData(String currentUserEmail, Map<String, Integer> seatTypeCountMap, double totalOrderPrice, String paymentMethod) {
        Map<String, Object> order = new HashMap<>();
        order.put("user_email", currentUserEmail);
        order.put("seat_counts", seatTypeCountMap);
        order.put("total_order_price", totalOrderPrice);
        order.put("payment_method", paymentMethod);

        return order;
    }

    public void back(View view) {
        Intent intent = new Intent(this, Select_depart_ticket.class);
        back = findViewById(R.id.back);
        startActivity(intent);
    }
}

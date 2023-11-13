package com.example.assignment;

import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PriceCalculatorUtil {
    public static void calculateAndDisplayPrice(
            String originName,
            String destinationName,
            TextView priceTextView,
            FirebaseFirestore db
    ) {
        // Check if originName and destinationName are not empty
        if (!originName.isEmpty() && !destinationName.isEmpty()) {
            // Fetch Firestore document for the origin location using the 'name' field
            db.collection("northbound")
                    .whereEqualTo("name", originName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot originSnapshot = task.getResult().getDocuments().get(0);
                            int originId = originSnapshot.getLong("id").intValue();

                            // Fetch Firestore document for the destination location using the 'name' field
                            db.collection("northbound")
                                    .whereEqualTo("name", destinationName)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                                            DocumentSnapshot destinationSnapshot = task1.getResult().getDocuments().get(0);
                                            int destinationId = destinationSnapshot.getLong("id").intValue();

                                            // Calculate the price using your formula
                                            double price = (destinationId - originId) * 1.5;

                                            // Display the calculated price in the TextView
                                            priceTextView.setText(String.format("From: RM%.2f", price));
                                        } else {
                                            // Handle the case where destination document doesn't exist
                                            priceTextView.setText("Price calculation failed");
                                        }
                                    });
                        } else {
                            // Handle the case where origin document doesn't exist
                            priceTextView.setText("Price calculation failed");
                        }
                    });
        } else {
            // Handle the case where originName or destinationName is empty
            priceTextView.setText("Price calculation failed");
        }
    }
}

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

        if (!originName.isEmpty() && !destinationName.isEmpty()) {

            db.collection("northbound")
                    .whereEqualTo("name", originName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot originSnapshot = task.getResult().getDocuments().get(0);
                            int originId = originSnapshot.getLong("id").intValue();


                            db.collection("northbound")
                                    .whereEqualTo("name", destinationName)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                                            DocumentSnapshot destinationSnapshot = task1.getResult().getDocuments().get(0);
                                            int destinationId = destinationSnapshot.getLong("id").intValue();

                                            double price = (destinationId - originId) * 1.5;


                                            priceTextView.setText(String.format("From: RM%.2f", price));
                                        } else {

                                            priceTextView.setText("Price calculation failed");
                                        }
                                    });
                        } else {

                            priceTextView.setText("Price calculation failed");
                        }
                    });
        } else {

            priceTextView.setText("Price calculation failed");
        }
    }
}

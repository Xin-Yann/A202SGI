package com.example.assignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class Select_seat {

    public static void handleSeatActivity(Activity activity) {
        Intent intent = activity.getIntent();
        if (intent != null && intent.hasExtra("originName") && intent.hasExtra("destinationName") && intent.hasExtra("totalDuration") && intent.hasExtra("trainDate") && intent.hasExtra("trainPax")) {
            String originName = intent.getStringExtra("originName");
            String destinationName = intent.getStringExtra("destinationName");
            String totalDuration = intent.getStringExtra("totalDuration");
            String trainDate = intent.getStringExtra("trainDate");
            String trainPax = intent.getStringExtra("trainPax");
            String departureTime = intent.getStringExtra("departureTime");
            String arrivalTime = intent.getStringExtra("arrivalTime");

            // Update your UI with originName, destinationName, totalDuration, trainDate, and trainPax as needed
            // For example, you can set the text of TextViews
            TextView originTextView = activity.findViewById(R.id.origin);
            TextView destinationTextView = activity.findViewById(R.id.destination);
            TextView durationTextView = activity.findViewById(R.id.duration);
            TextView departTextView = activity.findViewById(R.id.depart_time);
            TextView arriveTextView = activity.findViewById(R.id.arrive_time);


            originTextView.setText(originName);
            destinationTextView.setText(destinationName);
            durationTextView.setText(totalDuration);
            departTextView.setText(departureTime);
            arriveTextView.setText(arrivalTime);

            // Use trainDate and trainPax as needed
        }
    }

    public static void startNextSeatActivity(Activity activity, Class<?> nextActivityClass, String originName, String destinationName, String totalDuration, String trainDate, String trainPax, String departureTime, String arrivalTime) {
        Intent intent = new Intent(activity, nextActivityClass);
        intent.putExtra("originName", originName);
        intent.putExtra("destinationName", destinationName);
        intent.putExtra("totalDuration", totalDuration);
        intent.putExtra("trainDate", trainDate);
        intent.putExtra("trainPax", trainPax);
        intent.putExtra("departureTime", departureTime);
        intent.putExtra("arrivalTime", arrivalTime);
        activity.startActivity(intent);
    }
}

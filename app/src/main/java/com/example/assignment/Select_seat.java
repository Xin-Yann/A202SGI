package com.example.assignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class Select_seat {

    public static void handleSeatActivity(Activity activity) {
        Intent intent = activity.getIntent();
        if (intent != null && intent.hasExtra("originName") && intent.hasExtra("destinationName") && intent.hasExtra("totalDuration")) {
            String originName = intent.getStringExtra("originName");
            String destinationName = intent.getStringExtra("destinationName");
            String totalDuration = intent.getStringExtra("totalDuration");

            // Update your UI with originName, destinationName, and totalDuration as needed
            // For example, you can set the text of TextViews
            TextView originTextView = activity.findViewById(R.id.origin);
            TextView destinationTextView = activity.findViewById(R.id.destination);
            TextView durationTextView = activity.findViewById(R.id.duration);

            originTextView.setText(originName);
            destinationTextView.setText(destinationName);
            durationTextView.setText(totalDuration);
        }
    }

    public static void startSeatActivity(Context context, Class<?> destinationClass, String originName, String destinationName, String totalDuration) {
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra("originName", originName);
        intent.putExtra("destinationName", destinationName);
        intent.putExtra("totalDuration", totalDuration);
        context.startActivity(intent);
    }

    public static void startNextSeatActivity(Activity activity, Class<?> nextActivityClass, String originName, String destinationName, String totalDuration) {
        Intent intent = new Intent(activity, nextActivityClass);
        intent.putExtra("originName", originName);
        intent.putExtra("destinationName", destinationName);
        intent.putExtra("totalDuration", totalDuration);
        activity.startActivity(intent);
    }
}

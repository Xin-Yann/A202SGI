package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Payment3 extends AppCompatActivity {
    Button viewYourTicketBtn;
    Button backToHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_3);
    }

    public void toBookingHistory(View view) {
        Intent intent = new Intent(this, Booking_history.class);
        viewYourTicketBtn = findViewById(R.id.viewYourTicketBtn);
        startActivity(intent);
    }

    public void toHomePage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        backToHomeBtn = findViewById(R.id.backToHomeBtn);
        startActivity(intent);
    }
}
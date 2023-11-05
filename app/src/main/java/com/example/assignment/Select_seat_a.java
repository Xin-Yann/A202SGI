package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
public class Select_seat_a extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_seat_a);
    }
   /* public void toSeat_b(View view){
        Intent intent = new Intent(this, Select_seat_b.class);
        ImageButton toHomePage = findViewById(R.id.rectangle_2);
        startActivity(intent);
    }*/



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





}

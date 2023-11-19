package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Account  extends AppCompatActivity {

    public static final String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
    }

    public void Home(View view){
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton Home = findViewById(R.id.home);
        startActivity(intent);
    }

    public void my_profile(View view){
        Intent intent = new Intent(this, Profile_details_a.class);
        TextView my_profile = findViewById(R.id.my_profile);
        startActivity(intent);
    }

    public void special_registration(View view){
        Intent intent = new Intent(this, Profile_details_b.class);
        TextView special_registration = findViewById(R.id.special_registration);
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
}

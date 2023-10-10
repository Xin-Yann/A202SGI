package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_details_b extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details_b);
    }

    public void Account(View view){
        Intent intent = new Intent(this, Account.class);
        ImageButton Account = findViewById(R.id.profilePage);
        startActivity(intent);
    }
}


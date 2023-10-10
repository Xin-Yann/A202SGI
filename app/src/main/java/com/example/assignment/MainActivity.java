package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


       tabLayout = findViewById(R.id.tablayout);
       viewPager = findViewById(R.id.viewpager);

       tabLayout.setupWithViewPager(viewPager);
       VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
       vpAdapter.addFragment(new fragment1(), "One-Way");
       vpAdapter.addFragment(new fragment2(), "Round Trip");
       viewPager.setAdapter(vpAdapter);

        ImageView login = findViewById(R.id.login);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoginValid = preferences.getBoolean("isLoginValid", false);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Check if login information is correct (You can replace this with your own logic)
                if (isLoginValid) {
                    // After successful login, navigate to the ProfileActivity
                    Intent profileIntent = new Intent(MainActivity.this, Account.class);
                    startActivity(profileIntent);

                } else {
                    Intent Loginintent = new Intent(MainActivity.this, login.class);
                    startActivity(Loginintent);
                }
            }
        });


        // Get the username from the intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        // Display the greeting message
        TextView textViewGreeting = findViewById(R.id.displayid);
        textViewGreeting.setText("Hello, " + username);
    }

    /*Footer*/
    public void toHomePage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton toHomePage = findViewById(R.id.homePage);
        startActivity(intent);
    }

    public void toBookingHistory(View view){
        Intent intent = new Intent(this, Account.class);
        ImageButton toBookingHistory = findViewById(R.id.booking_history);
        startActivity(intent);
    }

    public void toAccountPage(View view){
        Intent intent = new Intent(this, Account.class);
        ImageButton toAccountPage = findViewById(R.id.profilePage);
        startActivity(intent);
    }



}
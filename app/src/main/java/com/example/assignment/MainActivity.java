package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_seat_b);



       /*tabLayout = findViewById(R.id.tablayout);
       viewPager = findViewById(R.id.viewpager);

       tabLayout.setupWithViewPager(viewPager);
       VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
       vpAdapter.addFragment(new fragment1(), "One-Way");
       vpAdapter.addFragment(new fragment2(), "Round Trip");
       viewPager.setAdapter(vpAdapter);*/
    }

}
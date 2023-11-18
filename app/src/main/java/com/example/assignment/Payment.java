package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    RadioGroup radioGroup;
    FrameLayout cardFragmentContainer;
    FrameLayout tngFragmentContainer;
    Fragment currentFragment;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_1);

        radioGroup = findViewById(R.id.radioGroup);
        cardFragmentContainer = findViewById(R.id.cardFragmentContainer);
        tngFragmentContainer = findViewById(R.id.tngFragmentContainer);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Remove the current fragment
                if (currentFragment != null) {
                    transaction.remove(currentFragment);
                }

                // Check which radio button is selected
                if (checkedId == R.id.card) {
                    currentFragment = new FragmentCard();
                    transaction.replace(R.id.cardFragmentContainer, currentFragment);
                } else if (checkedId == R.id.ewallet) {
                    currentFragment = new FragmentTng();
                    transaction.replace(R.id.tngFragmentContainer, currentFragment);
                } else {
                    currentFragment = null;
                }

                transaction.commit();
            }
        });
    }
}
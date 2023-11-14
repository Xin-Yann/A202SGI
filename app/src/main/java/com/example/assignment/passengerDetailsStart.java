package com.example.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
//get pax
//get data from database then show name let user choose previous pass
public class passengerDetailsStart extends AppCompatActivity {
    FirebaseFirestore firestore;
    RadioGroup radioGroup;
    FrameLayout malaysianFragmentContainer;
    FrameLayout nonMalaysianFragmentContainer;
    Fragment currentFragment;
    Spinner selectTicketType;
    Button nextPassengerButton;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.passenger_details_start);

        firestore = FirebaseFirestore.getInstance();

        FirebaseApp.initializeApp(this);

        radioGroup = findViewById(R.id.radioGroup);
        malaysianFragmentContainer = findViewById(R.id.malaysianFragmentContainer);
        nonMalaysianFragmentContainer = findViewById(R.id.nonMalaysianFragmentContainer);

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
                if (checkedId == R.id.yes) {
                    currentFragment = new FragmentCard();
                    transaction.replace(R.id.malaysianFragmentContainer, currentFragment);
                } else if (checkedId == R.id.no) {
                    currentFragment = new FragmentTng();
                    transaction.replace(R.id.nonMalaysianFragmentContainer, currentFragment);
                } else {
                    currentFragment = null;
                }

                transaction.commit();
            }
        });

        selectTicketType = findViewById(R.id.selectTicketType);
        String[] selectionOptions = {"Adult", "Child", "Premium"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectionOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTicketType.setAdapter(adapter);

        nextPassengerButton = findViewById(R.id.nextPassengerBtn);
        builder = new AlertDialog.Builder(this);

        nextPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve data from the FragmentMalaysian and save it
                FragmentMalaysian fragmentMalaysian = (FragmentMalaysian) getSupportFragmentManager().findFragmentById(R.id.malaysianFragmentContainer);
                if (fragmentMalaysian != null) {
                    Map<String, Object> passenger = fragmentMalaysian.getFragmentMalaysianData();
                }

                builder.setTitle("Alert!")
                        .setMessage("Do you want to save passenger details?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addPassenger();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    private void addPassenger() {
        Map<String, Object> passenger = new HashMap<>();
        // Get data from user input
        String passGender = ((TextInputEditText) findViewById(R.id.inputGender)).getText().toString();
        String passContact = ((TextInputEditText) findViewById(R.id.inputContactNumber)).getText().toString();
        String passEmail = ((TextInputEditText) findViewById(R.id.inputEmail)).getText().toString();
        String passTicketType = selectTicketType.getSelectedItem().toString();

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        String nationality = getNationality(selectedRadioButtonId);

        // Create menu object with the added data
        passenger.put("pass_nationality", nationality);
        passenger.put("pass_gender", passGender);
        passenger.put("pass_contact", passContact);
        passenger.put("pass_email", passEmail);
        passenger.put("pass_ticketType", passTicketType);

        firestore.collection("passengers").add(passenger).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Passenger details added successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to add Passenger details", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getNationality(int selectedRadioButtonId) {
        if (selectedRadioButtonId == R.id.yes) {
            return "Yes";
        } else if (selectedRadioButtonId == R.id.no) {
            return "No";
        } else {
            return "Unknown";
        }
    }
}

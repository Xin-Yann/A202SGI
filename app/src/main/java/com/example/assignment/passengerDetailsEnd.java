package com.example.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class passengerDetailsEnd extends AppCompatActivity {
    FirebaseFirestore firestore;
    RadioGroup radioGroup;
    Fragment currentFragment;
    Spinner selectTicketType;
    Button makePaymentButton;
    Button clearButton;
    AlertDialog.Builder builder;
    Spinner selectPrePassenger;
    List<Map<String, Object>> passengers;
    ArrayAdapter<String> adapterForTicketType;
    private boolean isUserSelection = false;
    private String trainPax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.passenger_details_end);

        firestore = FirebaseFirestore.getInstance();

        FirebaseApp.initializeApp(this);

        selectPrePassenger = findViewById(R.id.selectPrePassenger);

        fetchPassengersFromDatabase();

        selectPrePassenger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected passenger's details
                Map<String, Object> selectedPassenger = passengers.get(position);

                // Check the radio button based on nationality
                String nationality = (String) selectedPassenger.get("pass_nationality");
                RadioButton radioButtonYes = findViewById(R.id.yes);
                RadioButton radioButtonNo = findViewById(R.id.no);

                radioButtonYes.setChecked("Yes".equals(nationality));
                radioButtonNo.setChecked("No".equals(nationality));

                String gender = (String) selectedPassenger.get("pass_gender");
                String contactNumber = (String) selectedPassenger.get("pass_contact");
                String email = (String) selectedPassenger.get("pass_email");
                String ticketType = (String) selectedPassenger.get("pass_ticketType");

                TextInputEditText inputedGender = findViewById(R.id.inputGender);
                TextInputEditText inputedContactNumber = findViewById(R.id.inputContactNumber);
                TextInputEditText inputedEmail = findViewById(R.id.inputEmail);

                inputedGender.setText(gender);
                inputedContactNumber.setText(contactNumber);
                inputedEmail.setText(email);

                int ticketTypePosition = adapterForTicketType.getPosition(ticketType);
                selectTicketType.setSelection(ticketTypePosition);

                // Update Fragment data based on the selected passenger
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if (currentFragment instanceof FragmentNonMalaysian) {
                                    ((FragmentNonMalaysian) currentFragment).updateFragmentNonMalaysianData(selectedPassenger);
                                } else if (currentFragment instanceof FragmentMalaysian) {
                                    ((FragmentMalaysian) currentFragment).updateFragmentMalaysianData(selectedPassenger);
                                }
                            }
                        }, 500);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected
            }
        });

        selectPrePassenger.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Set the flag when the user interacts with the spinner
                isUserSelection = true;
                return false;
            }
        });

        radioGroup = findViewById(R.id.radioGroup);

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
                    currentFragment = new FragmentMalaysian();
                } else if (checkedId == R.id.no) {
                    currentFragment = new FragmentNonMalaysian();
                } else {
                    currentFragment = null;
                }

                // Replace the fragmentContainer with the new fragment
                transaction.replace(R.id.fragmentContainer, currentFragment);

                transaction.commit();

                // Adjust the layout of elements below the RadioGroup
                if (checkedId == R.id.yes || checkedId == R.id.no) {
                    adjustLayoutForFragment(true,
                            1050,
                            1110,
                            1300,
                            1360,
                            1550,
                            1610,
                            1800,
                            470,
                            2200,
                            2450,
                            2700,
                            R.id.gender, R.id.inputGenderLayout,
                            R.id.contactNumber, R.id.inputContactNumberLayout,
                            R.id.email, R.id.inputEmailLayout, R.id.ticketType,
                            R.id.selectTicketType, R.id.confirmation,
                            R.id.confirmationInfo, R.id.makePaymentBtn);
                } else {
                    adjustLayoutForFragment(false,
                            500,
                            560,
                            750,
                            810,
                            1000,
                            1060,
                            1250,
                            -45,
                            1700,
                            1950,
                            2200,
                            R.id.gender, R.id.inputGenderLayout,
                            R.id.contactNumber, R.id.inputContactNumberLayout,
                            R.id.email, R.id.inputEmailLayout, R.id.ticketType,
                            R.id.selectTicketType, R.id.confirmation,
                            R.id.confirmationInfo, R.id.makePaymentBtn);
                }
            }
        });

        clearButton = findViewById(R.id.clearBtn);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear
                TextInputEditText inputedGender = findViewById(R.id.inputGender);
                TextInputEditText inputedContactNumber = findViewById(R.id.inputContactNumber);
                TextInputEditText inputedEmail = findViewById(R.id.inputEmail);

                inputedGender.getText().clear();
                inputedContactNumber.getText().clear();
                inputedEmail.getText().clear();

                RadioButton radioButtonYes = findViewById(R.id.yes);
                RadioButton radioButtonNo = findViewById(R.id.no);

                radioButtonYes.setChecked(false);
                radioButtonNo.setChecked(false);

                if (currentFragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.remove(currentFragment);
                    transaction.commit();
                    currentFragment = null;

                    adjustLayoutForFragment(false,
                            500,
                            560,
                            750,
                            810,
                            1000,
                            1060,
                            1250,
                            -45,
                            1700,
                            1950,
                            2200,
                            R.id.gender, R.id.inputGenderLayout,
                            R.id.contactNumber, R.id.inputContactNumberLayout,
                            R.id.email, R.id.inputEmailLayout, R.id.ticketType,
                            R.id.selectTicketType, R.id.confirmation,
                            R.id.confirmationInfo, R.id.makePaymentBtn);
                }
            }
        });

        selectTicketType = findViewById(R.id.selectTicketType);
        String[] selectionOptions = {"Choose your ticket type","Adult (free mineral water)", "Child (free orange juice)", "Premium (free soft drink)"};
        adapterForTicketType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectionOptions);
        adapterForTicketType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTicketType.setAdapter(adapterForTicketType);

        // Retrieve the trainPax value from SharedPreferences
        retrieveDataFromSharedPreferences();

        makePaymentButton = findViewById(R.id.makePaymentBtn);
        builder = new AlertDialog.Builder(this);

        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
    }

    private String getCurrentUserEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        return user != null ? user.getEmail() : null;
    }

    private void fetchPassengersFromDatabase() {
        String currentUserEmail = getCurrentUserEmail();

        if (currentUserEmail != null) {
            firestore.collection("passengers")
                    .whereEqualTo("user_email", currentUserEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        passengers = new ArrayList<>();

                        Map<String, Object> hintPassenger = new HashMap<>();
                        hintPassenger.put("pass_name", "Choose a previous passenger");
                        passengers.add(hintPassenger);

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Map<String, Object> passengerData = document.getData();
                            passengers.add(passengerData);
                        }

                        // Populate the spinner with passenger names
                        List<String> passengerNames = getPassengerNames();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, passengerNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectPrePassenger.setAdapter(adapter);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch passengers", Toast.LENGTH_LONG).show();
                    });
        }
    }

    private List<String> getPassengerNames() {
        List<String> passengerNames = new ArrayList<>();
        for (Map<String, Object> passenger : passengers) {
            String name = (String) passenger.get("pass_name"); // Change "pass_name" to the actual key in your passenger data
            passengerNames.add(name);
        }
        return passengerNames;
    }

    private void adjustLayoutForFragment(boolean fragmentVisible, int marginTopGender, int marginTopInputGender,
                                         int marginTopContactNumber, int marginTopContactNumberLayout,
                                         int marginTopEmail, int marginTopEmailLayout,
                                         int marginTopTicketType, int marginTopSelectTicketType,
                                         int marginTopConfirmation, int marginTopConfirmationInfo,
                                         int marginTopMakePaymentBtn, int... viewIds) {
        for (int viewId : viewIds) {
            View view = findViewById(viewId);
            if (view != null) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

                if (viewId == R.id.gender) {
                    layoutParams.topMargin = marginTopGender;
                } else if (viewId == R.id.inputGenderLayout) {
                    layoutParams.topMargin = marginTopInputGender;
                } else if (viewId == R.id.contactNumber) {
                    layoutParams.topMargin = marginTopContactNumber;
                } else if (viewId == R.id.inputContactNumberLayout) {
                    layoutParams.topMargin = marginTopContactNumberLayout;
                } else if (viewId == R.id.email) {
                    layoutParams.topMargin = marginTopEmail;
                } else if (viewId == R.id.inputEmailLayout) {
                    layoutParams.topMargin = marginTopEmailLayout;
                } else if (viewId == R.id.ticketType) {
                    layoutParams.topMargin = marginTopTicketType;
                } else if (viewId == R.id.selectTicketType) {
                    layoutParams.topMargin = marginTopSelectTicketType;
                } else if (viewId == R.id.confirmation) {
                    layoutParams.topMargin = marginTopConfirmation;
                } else if (viewId == R.id.confirmationInfo) {
                    layoutParams.topMargin = marginTopConfirmationInfo;
                } else if (viewId == R.id.makePaymentBtn) {
                    layoutParams.topMargin = marginTopMakePaymentBtn;
                }

                view.setLayoutParams(layoutParams);
            }
        }
    }

    private void showConfirmationDialog() {
        int selectedPosition = selectPrePassenger.getSelectedItemPosition();

        builder.setTitle("Alert!")
                .setMessage("Do you want to save passenger details?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Check if a previous passenger is selected
                        if (selectedPosition > 0) {
                            // If the user clicks "Yes" and a previous passenger is selected, do not save
                            Toast.makeText(passengerDetailsEnd.this, "Passenger details not saved for a previous passenger.", Toast.LENGTH_SHORT).show();
                            launchPayment();
                        } else {
                            // If the user clicks "Yes" and a new passenger is entered, save passenger details
                            savePassengerDetails();
                        }

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void savePassengerDetails() {
        if (currentFragment instanceof FragmentNonMalaysian) {
            // Retrieve data from FragmentNonMalaysian
            Map<String, Object> passenger = ((FragmentNonMalaysian) currentFragment).getFragmentNonMalaysianData();

            addPassenger(passenger);
            launchPayment();
        } else if (currentFragment instanceof FragmentMalaysian) {
            // Retrieve data from FragmentMalaysian
            Map<String, Object> passenger = ((FragmentMalaysian) currentFragment).getFragmentMalaysianData();

            addPassenger(passenger);
            launchPayment();
        }
    }

    private void retrieveDataFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        trainPax = preferences.getString("trainPax", "");

        // Update the passengerAmountTextView with the new trainPax value
        updatePassengerCount();
    }

    private void updatePassengerCount() {
        TextView passengerAmountTextView = findViewById(R.id.passengerAmount);

        // Update the passenger count
        passengerAmountTextView.setText("Passenger " + trainPax + "/" + trainPax);
    }

    private void addPassenger(Map<String, Object> passenger) {
        String passGender = ((TextInputEditText) findViewById(R.id.inputGender)).getText().toString();
        String passContact = ((TextInputEditText) findViewById(R.id.inputContactNumber)).getText().toString();
        String passEmail = ((TextInputEditText) findViewById(R.id.inputEmail)).getText().toString();
        String passTicketType = selectTicketType.getSelectedItem().toString();

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        String nationality = getNationality(selectedRadioButtonId);

        passenger.put("pass_nationality", nationality);
        passenger.put("pass_gender", passGender);
        passenger.put("pass_contact", passContact);
        passenger.put("pass_email", passEmail);
        passenger.put("pass_ticketType", passTicketType);
        passenger.put("user_email", getCurrentUserEmail());

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

    private void launchPayment() {
        Intent intent = new Intent(this, Payment.class);
        startActivity(intent);
        finish();
    }

    public void back(View view) {
        Intent intent = new Intent(this, Select_depart_ticket.class);
        ImageButton back = findViewById(R.id.back);
        startActivity(intent);
    }
}
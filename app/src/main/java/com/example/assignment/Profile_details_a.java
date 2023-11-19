package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Profile_details_a extends AppCompatActivity {

    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user , staffs;
    FirebaseFirestore fStore;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details_a);

        // Initialize FirebaseAuth and get the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        staffs= auth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        if (user == null) {
            // If the user is not logged in, redirect them to the login screen
            Intent intent = new Intent(Profile_details_a.this, login.class);
            startActivity(intent);
            finish();
        } else {
            // Query Firestore to retrieve additional user data
            fStore.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userName = document.getString("name");
                                    String userPass = document.getString("password");
                                    String userEmail = document.getString("email");
                                    // You can retrieve other user data here
                                    textView = findViewById(R.id.inputName);
                                    textView.setText(userName);
                                    textView = findViewById(R.id.inputPassword);
                                    textView.setText(userPass);
                                    textView = findViewById(R.id.inputEmail);
                                    textView.setText(userEmail);

                                }
                            }
                        }
                    });
        }

        saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = ((TextInputEditText) findViewById(R.id.inputEmail)).getText().toString();
                String newPass = ((TextInputEditText) findViewById(R.id.inputPassword)).getText().toString();

                updateProfile(newEmail, newPass);
            }
        });
    }

    public void updateProfile(String newEmail, String newPass) {
        if(user!=null) {
            // Get the document reference for the user's data
            DocumentReference userDocRef = fStore.collection("users").document(user.getUid());

            // Create a map with the updated data
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("email", newEmail);
            updatedData.put("password", newPass);

            // Update the data in Firestore
            userDocRef.update(updatedData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Profile_details_a.this, "Data updated successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Profile_details_a.this, "Failed to update data", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    public void Account(View view) {
        Intent intent = new Intent(this, Account.class);
        ImageButton Account = findViewById(R.id.profilePage1);
        startActivity(intent);
    }

    /*Footer*/
    public void toHomePage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton toHomePage = findViewById(R.id.homePage);
        startActivity(intent);
    }

    public void toBookingHistory(View view) {
        Intent intent = new Intent(this, Booking_history.class);
        ImageButton toBookingHistory = findViewById(R.id.booking_history);
        startActivity(intent);
    }

    public void toAccountPage(View view) {
        Intent intent = new Intent(this, Account.class);
        ImageButton toAccountPage = findViewById(R.id.profilePage);
        startActivity(intent);
    }
}

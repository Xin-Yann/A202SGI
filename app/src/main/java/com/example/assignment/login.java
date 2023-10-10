package com.example.assignment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


public class login extends AppCompatActivity {
    private EditText nameid, passid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        nameid = findViewById(R.id.nameid);
        passid = findViewById(R.id.passid);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            // After successful login, store login status
            @Override
            public void onClick(View v) {
                String username = nameid.getText().toString();
                String password = passid.getText().toString();
                // Check if login information is correct (You can replace this with your own logic)
                if (isLoginValid(username, password)) {
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoginValid", true);
                    editor.apply();

                    // Correct login, navigate to the next activity and display a greeting
                    Intent intent = new Intent(login.this,MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    // Incorrect login, show a toast message
                    Toast.makeText(login.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                }
            }

        });

        TextView register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming you want to switch to a different activity or layout
                // You should use an Intent to start a new activity or use setContentView for a new layout
                // For example, to switch to a new activity, you can do:
                Intent intent = new Intent(login.this, Register.class);
                startActivity(intent);

                // If you want to switch to a new layout within the same activity, you can do:
                // setContentView(R.layout.login);
            }
        });
    }

    // Replace this with your own validation logic
    private boolean isLoginValid(String name, String password) {
        String correctUsername = "Jane";
        String correctPassword = "jane135";

        return name.equals(correctUsername) && password.equals(correctPassword);
    }
}

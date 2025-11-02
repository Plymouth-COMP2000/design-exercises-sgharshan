// Package structure goes here (e.g., package com.restaurant.app;)

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

/**
 * MainActivity handles user login/authentication.
 * Frontend Focus: Demonstrates navigation flow upon simulated successful login.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link to activity_main.xml layout file
        setContentView(R.layout.activity_main);

        MaterialButton loginButton = findViewById(R.id.button_login);
        MaterialButton registerButton = findViewById(R.id.button_register);

        // Placeholder UI logic: Simulate successful login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assume successful login and navigate to Guest Dashboard
                Toast.makeText(MainActivity.this, "Welcome, Guest!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, GuestDashboardActivity.class);
                startActivity(intent);
                finish(); // Close login screen for a cleaner stack
            }
        });

        // Placeholder UI logic: Registration button feedback
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Feature: Navigate to Registration Screen", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.restaurantmanagementapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.UserDao;
import com.example.restaurantmanagementapp.data.model.User;
import com.example.restaurantmanagementapp.databinding.ActivityMainBinding;
import com.example.restaurantmanagementapp.ui.dashboard.GuestDashboardActivity;
import com.example.restaurantmanagementapp.ui.dashboard.StaffDashboardActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.buttonLogin.setOnClickListener(v -> handleLogin());
        binding.textRegisterPrompt.setOnClickListener(v -> handleRegistration());
        binding.textRegisterLink.setOnClickListener(v -> handleRegistration());
        binding.textForgotPassword.setOnClickListener(
                v -> Toast.makeText(MainActivity.this, "Forgot Password clicked!", Toast.LENGTH_SHORT).show());
    }

    private void handleLogin() {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            binding.inputLayoutEmail.setError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            binding.inputLayoutPassword.setError("Password is required");
            return;
        }

        try {
            AppDatabase db = AppDatabase.getDatabase(this);
            // Note: Using 'login' from DAO which expects email/password
            User user = db.userDao().login(email, password);

            if (user != null) {
                // Save session
                com.example.restaurantmanagementapp.util.SessionManager sessionManager = new com.example.restaurantmanagementapp.util.SessionManager(
                        this);
                sessionManager.saveUser(user);

                Toast.makeText(this, "Welcome " + user.firstname, Toast.LENGTH_SHORT).show();
                navigateToDashboard(user.usertype);
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Login Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleRegistration() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboard(String usertype) {
        Intent intent;
        if ("staff".equalsIgnoreCase(usertype) || "admin".equalsIgnoreCase(usertype)) {
            intent = new Intent(MainActivity.this, StaffDashboardActivity.class);
        } else {
            intent = new Intent(MainActivity.this, GuestDashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}

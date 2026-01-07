package com.example.restaurantmanagementapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.model.User;
import com.example.restaurantmanagementapp.data.repository.UserRepository;
import com.example.restaurantmanagementapp.databinding.ActivityMainBinding;
import com.example.restaurantmanagementapp.ui.dashboard.GuestDashboardActivity;
import com.example.restaurantmanagementapp.ui.dashboard.StaffDashboardActivity;
import com.example.restaurantmanagementapp.util.SessionManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository(getApplication());
        setupClickListeners();

        // Schedule Background Sync
        androidx.work.PeriodicWorkRequest syncRequest = new androidx.work.PeriodicWorkRequest.Builder(
                com.example.restaurantmanagementapp.data.remote.SyncWorker.class,
                15, java.util.concurrent.TimeUnit.MINUTES)
                .setConstraints(new androidx.work.Constraints.Builder()
                        .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                        .build())
                .build();

        androidx.work.WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "UserSyncWork",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                syncRequest);
    }

    private void setupClickListeners() {
        binding.buttonLogin.setOnClickListener(v -> handleLogin());

        View.OnClickListener registerListener = v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        };
        binding.textRegisterPrompt.setOnClickListener(registerListener);
        binding.textRegisterLink.setOnClickListener(registerListener);

        binding.textForgotPassword.setOnClickListener(
                v -> Toast.makeText(MainActivity.this, "Forgot Password clicked!", Toast.LENGTH_SHORT).show());
    }

    private void handleLogin() {
        String username = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if (username.isEmpty()) {
            binding.inputLayoutEmail.setError("Username/Email is required");
            return;
        }
        if (password.isEmpty()) {
            binding.inputLayoutPassword.setError("Password is required");
            return;
        }

        binding.buttonLogin.setEnabled(false);
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();

        userRepository.login(username, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    // Update Session
                    SessionManager sessionManager = new SessionManager(MainActivity.this);
                    sessionManager.saveUser(user);

                    binding.buttonLogin.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Welcome " + user.firstname, Toast.LENGTH_SHORT).show();
                    proceedToDashboard(user);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    binding.buttonLogin.setEnabled(true);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void proceedToDashboard(User user) {
        startActivity(new Intent(this, user.isStaff() ? StaffDashboardActivity.class : GuestDashboardActivity.class));
        finish();
    }
}

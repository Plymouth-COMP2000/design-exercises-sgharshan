package com.example.restaurantmanagementapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.databinding.GuestDashboardBinding;
import com.example.restaurantmanagementapp.ui.menu.MenuBrowseActivity;
import com.example.restaurantmanagementapp.ui.reservation.MyReservationsActivity;

public class GuestDashboardActivity extends AppCompatActivity {

    private GuestDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GuestDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        com.example.restaurantmanagementapp.util.SessionManager sessionManager = new com.example.restaurantmanagementapp.util.SessionManager(
                this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, com.example.restaurantmanagementapp.ui.auth.MainActivity.class));
            finish();
            return;
        }

        com.example.restaurantmanagementapp.data.model.User user = sessionManager.getUser();
        if (user != null) {
            binding.textUsername.setText(user.username);
            binding.textEmail.setText(user.email);
        }

        binding.buttonLogout.setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(GuestDashboardActivity.this,
                    com.example.restaurantmanagementapp.ui.auth.MainActivity.class));
            finish();
        });

        binding.buttonBrowseMenu.setOnClickListener(v -> {
            startActivity(new Intent(GuestDashboardActivity.this, MenuBrowseActivity.class));
        });

        binding.cardMakeReservation.setOnClickListener(v -> {
            // Usually reservation starts by browsing menu or picking date.
            // Pointing to MenuBrowseActivity as per original code.
            startActivity(new Intent(GuestDashboardActivity.this, MenuBrowseActivity.class));
        });

        binding.buttonViewReservations.setOnClickListener(v -> {
            startActivity(new Intent(GuestDashboardActivity.this, MyReservationsActivity.class));
        });

        binding.cardMyReservations.setOnClickListener(v -> {
            startActivity(new Intent(GuestDashboardActivity.this, MyReservationsActivity.class));
        });

        binding.buttonProfile.setOnClickListener(v -> {
            // Logout or Profile logic - Reusing for Logout just in case, or Toast
            // But main logout is now on the card.
            sessionManager.logout();
            startActivity(new Intent(GuestDashboardActivity.this,
                    com.example.restaurantmanagementapp.ui.auth.MainActivity.class));
            finish();
        });
    }
}

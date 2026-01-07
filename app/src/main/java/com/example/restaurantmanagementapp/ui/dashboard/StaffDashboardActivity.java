package com.example.restaurantmanagementapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.databinding.ActivityStaffDashboardBinding;
import com.example.restaurantmanagementapp.ui.auth.MainActivity;
import com.example.restaurantmanagementapp.ui.menu.ManageMenuActivity;
import com.example.restaurantmanagementapp.ui.reservation.StaffReservationsActivity;

public class StaffDashboardActivity extends AppCompatActivity {
    private ActivityStaffDashboardBinding binding;
    private StaffDashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStaffDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(StaffDashboardViewModel.class);

        // Security Check
        if (!viewModel.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Manage Menu Card
        binding.cardManageMenu.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this,
                    com.example.restaurantmanagementapp.ui.menu.MenuBrowseActivity.class);
            intent.putExtra("IS_STAFF_MODE", true);
            startActivity(intent);
        });

        // View Reservations Card
        binding.cardViewReservations.setOnClickListener(v -> {
            startActivity(new Intent(StaffDashboardActivity.this, StaffReservationsActivity.class));
        });

        // Logout Button
        binding.buttonLogout.setOnClickListener(v -> {
            viewModel.logout();
            startActivity(new Intent(StaffDashboardActivity.this, MainActivity.class));
            finish();
        });

        // Settings Button
        binding.buttonSettings.setOnClickListener(v -> {
            startActivity(new Intent(StaffDashboardActivity.this,
                    com.example.restaurantmanagementapp.ui.settings.SettingsActivity.class));
        });
    }
}

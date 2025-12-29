package com.example.restaurantmanagementapp.ui.reservation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantmanagementapp.databinding.GuestBookingConfirmpageBinding;
import com.example.restaurantmanagementapp.ui.dashboard.GuestDashboardActivity;
import com.example.restaurantmanagementapp.ui.reservation.MyReservationsActivity;

public class BookingConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GuestBookingConfirmpageBinding binding = GuestBookingConfirmpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(BookingConfirmedActivity.this, MyReservationsActivity.class);
            startActivity(intent);
            finish();
        });

        binding.buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(BookingConfirmedActivity.this, GuestDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}

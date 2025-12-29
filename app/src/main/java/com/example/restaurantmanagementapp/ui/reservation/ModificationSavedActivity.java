package com.example.restaurantmanagementapp.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.databinding.GuestBookingConfirmpageBinding;
// Note: We need to bind to guest_modification_confirmpage.xml, but binding class name might be GuestModificationConfirmpageBinding
// Let's assume standard naming convention.
import com.example.restaurantmanagementapp.databinding.GuestModificationConfirmpageBinding;
import com.example.restaurantmanagementapp.ui.dashboard.GuestDashboardActivity;

public class ModificationSavedActivity extends AppCompatActivity {

    private GuestModificationConfirmpageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GuestModificationConfirmpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type = getIntent().getStringExtra("TYPE");
        if ("CANCEL".equals(type)) {
            binding.textTitle.setText("Reservation Cancelled");
            binding.textSubtitle.setText("Your reservation has been successfully cancelled.");
            binding.imageSaveIcon.setImageResource(R.drawable.ic_check_circle); // Or a specific cross icon
            binding.buttonViewUpdatedDetails.setText("Return to Dashboard");
        }

        binding.buttonViewUpdatedDetails.setOnClickListener(v -> {
            Intent intent = new Intent(ModificationSavedActivity.this, GuestDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        binding.buttonDone.setOnClickListener(v -> {
            Intent intent = new Intent(ModificationSavedActivity.this, GuestDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}

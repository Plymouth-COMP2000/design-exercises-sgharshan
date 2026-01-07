package com.example.restaurantmanagementapp.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.model.Reservation;
import com.example.restaurantmanagementapp.databinding.ActivityCancelReservationBinding;

public class CancelReservationActivity extends AppCompatActivity {

    private ActivityCancelReservationBinding binding;
    private AppDatabase db;
    private Reservation reservation;
    private String reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCancelReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDatabase(this);
        reservationId = getIntent().getStringExtra("RESERVATION_ID");

        if (reservationId == null) {
            Toast.makeText(this, "Error: No reservation selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadReservation();

        binding.buttonConfirmCancel.setOnClickListener(v -> cancelReservation());
        binding.buttonBack.setOnClickListener(v -> finish());
    }

    private void loadReservation() {
        new Thread(() -> {
            reservation = db.reservationDao().getReservationById(reservationId);
            runOnUiThread(() -> {
                if (reservation != null) {
                    binding.textDate.setText(reservation.getDate());
                    binding.textTime.setText(reservation.getTime());
                    binding.textGuests.setText(reservation.getPartySize() + " Guests");
                } else {
                    Toast.makeText(this, "Reservation not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    private void cancelReservation() {
        if (reservation == null)
            return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonConfirmCancel.setEnabled(false);
        binding.buttonBack.setEnabled(false);

        new Thread(() -> {
            db.reservationDao().delete(reservation);
            runOnUiThread(() -> {
                Toast.makeText(this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();

                // Navigate to Cancellation Success Page (ModificationSavedActivity)
                Intent intent = new Intent(CancelReservationActivity.this, ModificationSavedActivity.class);
                intent.putExtra("TYPE", "CANCEL");
                // Clear back stack so user can't go back to the cancelled reservation details
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}

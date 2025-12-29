package com.example.restaurantmanagementapp.ui.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.ReservationDao;
import com.example.restaurantmanagementapp.data.model.Reservation;
import com.example.restaurantmanagementapp.databinding.GuestReservationBinding;
import com.example.restaurantmanagementapp.utils.NotificationHelper;

import java.util.Calendar;
import java.util.UUID;

public class ReservationBookingActivity extends AppCompatActivity {

    private GuestReservationBinding binding;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GuestReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupPickers();

        binding.buttonConfirmReservation.setOnClickListener(v -> confirmReservation());
        binding.buttonBack.setOnClickListener(v -> finish());
    }

    private void setupPickers() {
        binding.editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        binding.editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.editTextTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        binding.editTextTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });
    }

    private void confirmReservation() {
        String date = binding.editTextDate.getText().toString();
        String time = binding.editTextTime.getText().toString();
        String partySizeStr = binding.editTextPartySize.getText().toString();
        String name = binding.editTextName.getText().toString();
        String contact = binding.editTextContact.getText().toString();

        if (date.isEmpty() || time.isEmpty() || partySizeStr.isEmpty() || name.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int partySize = Integer.parseInt(partySizeStr);
        String id = UUID.randomUUID().toString();

        Reservation reservation = new Reservation(id, date, time, partySize, "Confirmed");
        reservation.setCustomerName(name);
        reservation.setCustomerContact(contact);
        // In a real app, we'd get the logged-in user's ID.
        // reservation.setUserId("guest@test.com");

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            ReservationDao dao = db.reservationDao();
            dao.insert(reservation);

            runOnUiThread(() -> {
                // Trigger Notification
                NotificationHelper.showNotification(this, "Reservation Confirmed",
                        "Your table for " + partySize + " is booked for " + time);

                // Navigate to Confirmation Screen
                Intent intent = new Intent(this, BookingConfirmedActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}

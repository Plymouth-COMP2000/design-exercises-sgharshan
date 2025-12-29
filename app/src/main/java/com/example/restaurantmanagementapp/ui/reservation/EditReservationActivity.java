package com.example.restaurantmanagementapp.ui.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.ReservationDao;
import com.example.restaurantmanagementapp.data.model.Reservation;
import com.example.restaurantmanagementapp.databinding.GuestReservationBinding;

import java.util.Calendar;
import java.util.Locale;

public class EditReservationActivity extends AppCompatActivity {

    private GuestReservationBinding binding;
    private AppDatabase db;
    private Reservation currentReservation;
    private String reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GuestReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDatabase(this);

        reservationId = getIntent().getStringExtra("RESERVATION_ID");
        if (reservationId == null) {
            Toast.makeText(this, "Error loading reservation", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadReservationData();
        setupClickListeners();
    }

    private void loadReservationData() {
        new Thread(() -> {
            currentReservation = db.reservationDao().getReservationById(reservationId);
            runOnUiThread(() -> {
                if (currentReservation != null) {
                    binding.textScreenTitle.setText("Modify Reservation");
                    binding.editTextDate.setText(currentReservation.getDate());
                    binding.editTextTime.setText(currentReservation.getTime());
                    binding.editTextPartySize.setText(String.valueOf(currentReservation.getPartySize()));
                    binding.editTextName.setText(currentReservation.getCustomerName());
                    binding.editTextContact.setText(currentReservation.getCustomerContact());
                    binding.buttonConfirmReservation.setText("Update Reservation");
                } else {
                    Toast.makeText(this, "Reservation not found!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    private void setupClickListeners() {
        binding.buttonBack.setOnClickListener(v -> finish());

        binding.inputLayoutDate.setEndIconOnClickListener(v -> showDatePicker());
        binding.editTextDate.setOnClickListener(v -> showDatePicker());

        binding.inputLayoutTime.setEndIconOnClickListener(v -> showTimePicker());
        binding.editTextTime.setOnClickListener(v -> showTimePicker());

        binding.buttonConfirmReservation.setOnClickListener(v -> updateReservation());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            binding.editTextDate.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            binding.editTextTime.setText(time);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void updateReservation() {
        if (currentReservation == null)
            return;

        String date = binding.editTextDate.getText().toString();
        String time = binding.editTextTime.getText().toString();
        String guestsStr = binding.editTextPartySize.getText().toString();
        String name = binding.editTextName.getText().toString();
        String contact = binding.editTextContact.getText().toString();

        if (date.isEmpty() || time.isEmpty() || guestsStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        currentReservation.setDate(date);
        currentReservation.setTime(time);
        try {
            currentReservation.setPartySize(Integer.parseInt(guestsStr));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Guest Number", Toast.LENGTH_SHORT).show();
            return;
        }
        currentReservation.setCustomerName(name);
        currentReservation.setCustomerContact(contact);

        new Thread(() -> {
            db.reservationDao().update(currentReservation);
            runOnUiThread(() -> {
                Toast.makeText(this, "Reservation Updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}

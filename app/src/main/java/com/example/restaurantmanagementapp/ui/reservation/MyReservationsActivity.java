package com.example.restaurantmanagementapp.ui.reservation;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.model.Reservation;
import com.example.restaurantmanagementapp.databinding.ManageReservationsBinding;

import java.util.ArrayList;

public class MyReservationsActivity extends AppCompatActivity {

    // Declare the binding object, generated from your layout file
    // manage_reservations.xml
    private ManageReservationsBinding binding;

    // A list to hold reservation data
    private ArrayList<Reservation> reservationList;
    // The adapter for the RecyclerView
    private ReservationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Inflate the layout using View Binding
        binding = ManageReservationsBinding.inflate(getLayoutInflater());
        // 2. Set the content view to the root of the binding
        setContentView(binding.getRoot());

        // 3. Set up the UI components and listeners
        setupUI();
        setupClickListeners();

        // 4. Load reservation data
        loadReservations();
    }

    private void setupUI() {
        // Configure the RecyclerView
        binding.recyclerViewMyReservations.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list of reservations
        reservationList = new ArrayList<>();

        // Create and set the adapter for the RecyclerView
        adapter = new ReservationListAdapter(this, reservationList);
        binding.recyclerViewMyReservations.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // Set a click listener for the back button to finish the activity
        binding.buttonBack.setOnClickListener(v -> {
            // This acts like the system's back button, taking the user to the previous
            // screen.
            finish();
        });
    }

    private void loadReservations() {
        com.example.restaurantmanagementapp.data.local.AppDatabase db = com.example.restaurantmanagementapp.data.local.AppDatabase
                .getDatabase(this);
        // For now, fetch all. In real app, fetch by User ID.
        java.util.List<Reservation> dbReservations = db.reservationDao().getAllReservations();

        reservationList.clear();
        reservationList.addAll(dbReservations);

        updateEmptyState();
        adapter.notifyDataSetChanged();
    }

    private void updateEmptyState() {
        if (reservationList.isEmpty()) {
            binding.recyclerViewMyReservations.setVisibility(View.GONE);
            binding.layoutEmptyReservations.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerViewMyReservations.setVisibility(View.VISIBLE);
            binding.layoutEmptyReservations.setVisibility(View.GONE);
        }
    }
}

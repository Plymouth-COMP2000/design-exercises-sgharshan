package com.example.restaurantmanagementapp.ui.reservation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.ReservationDao;
import com.example.restaurantmanagementapp.data.model.Reservation;
import com.example.restaurantmanagementapp.databinding.ActivityStaffReservationsBinding;
import java.util.List;

public class StaffReservationsActivity extends AppCompatActivity {

    private ActivityStaffReservationsBinding binding;
    private ReservationListAdapter adapter; // Reusing existing adapter for simplicity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStaffReservationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewStaffReservations.setLayoutManager(new LinearLayoutManager(this));

        loadReservations();
    }

    private void loadReservations() {
        AppDatabase db = AppDatabase.getDatabase(this);
        ReservationDao dao = db.reservationDao();
        List<Reservation> reservations = dao.getAllReservations();

        // Note: ReservationListAdapter might need adjustments if it expects specific
        // data types
        // For now, assuming it works with List<Reservation>
        // Adapter expects Context and ArrayList<Reservation>
        adapter = new ReservationListAdapter(this, new java.util.ArrayList<>(reservations));
        binding.recyclerViewStaffReservations.setAdapter(adapter);
    }
}

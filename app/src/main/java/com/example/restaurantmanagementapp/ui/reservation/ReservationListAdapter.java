package com.example.restaurantmanagementapp.ui.reservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.model.Reservation;
import com.example.restaurantmanagementapp.databinding.GuestReservationListBinding;

import java.util.ArrayList;

// Adapter to manage the list of reservations in the RecyclerView
public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ReservationViewHolder> {

    private final Context context;
    private final ArrayList<Reservation> reservationList;

    public ReservationListAdapter(Context context, ArrayList<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item using View Binding
        LayoutInflater inflater = LayoutInflater.from(context);
        GuestReservationListBinding binding = GuestReservationListBinding.inflate(inflater, parent, false);
        return new ReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        // Get the reservation data for the current position
        Reservation currentReservation = reservationList.get(position);
        // Bind the data to the views in the ViewHolder
        holder.bind(currentReservation);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    // ViewHolder class that holds the views for a single reservation item
    class ReservationViewHolder extends RecyclerView.ViewHolder {
        // Binding object for the guest_reservation_list.xml layout
        private final GuestReservationListBinding binding;

        public ReservationViewHolder(GuestReservationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Method to bind data to the views
        public void bind(Reservation reservation) {
            binding.textReservationDate.setText(reservation.getDate());
            String timeAndGuests = reservation.getTime() + "  •  " + reservation.getPartySize() + " Guests";
            binding.textReservationTimeAndGuests.setText(timeAndGuests);

            // Update status icon and color based on the reservation status
            if ("Confirmed".equalsIgnoreCase(reservation.getStatus())) {
                binding.iconStatus.setImageResource(R.drawable.ic_check_circle);
                // Get the gold color from the theme
                int goldColor = ContextCompat.getColor(context, R.color.pg_secondary_accent);
                binding.iconStatus.setColorFilter(goldColor);
            } else {
                binding.iconStatus.setImageResource(R.drawable.ic_access_time_24); // e.g., for "Pending"
                // Get a neutral color
                int neutralColor = ContextCompat.getColor(context, android.R.color.darker_gray);
                binding.iconStatus.setColorFilter(neutralColor);
            }

            // --- Set up click listeners for the buttons in the card ---
            binding.buttonModifyReservation.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(context, EditReservationActivity.class);
                intent.putExtra("RESERVATION_ID", reservation.getId());
                boolean isStaff = context instanceof StaffReservationsActivity;
                intent.putExtra("IS_STAFF", isStaff);
                context.startActivity(intent);
            });

            binding.buttonCancelReservation.setOnClickListener(v -> {
                // Navigate to dedicated Cancel Confirmation Page
                android.content.Intent intent = new android.content.Intent(context, CancelReservationActivity.class);
                intent.putExtra("RESERVATION_ID", reservation.getId());
                context.startActivity(intent);
            });
        }
    }
}

package com.example.restaurantmanagementapp.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.model.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItemsList;

    // Listener interface for Edit/Delete actions
    public interface OnItemClickListener {
        void onEditClick(MenuItem item);

        void onDeleteClick(MenuItem item);
    }

    private boolean isStaffMode = false;
    private OnItemClickListener listener;

    public MenuAdapter(List<MenuItem> menuItems, boolean isStaffMode, OnItemClickListener listener) {
        this.menuItemsList = new ArrayList<>(menuItems);
        this.isStaffMode = isStaffMode;
        this.listener = listener;
    }

    // Default constructor for backward compatibility (Guest Mode)
    public MenuAdapter(List<MenuItem> menuItems) {
        this(menuItems, false, null);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuItemsList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    // Method to update the adapter's list when a filter is applied
    public void filterList(List<MenuItem> filteredList) {
        menuItemsList = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemDescription, itemPrice;
        View staffActionsLayout;
        View editButton, deleteButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.image_view_food_item);
            itemName = itemView.findViewById(R.id.text_view_item_name);
            itemDescription = itemView.findViewById(R.id.text_view_item_description);
            itemPrice = itemView.findViewById(R.id.text_view_price);

            staffActionsLayout = itemView.findViewById(R.id.layout_staff_actions);
            editButton = itemView.findViewById(R.id.button_edit_item);
            deleteButton = itemView.findViewById(R.id.button_delete_item);
        }

        public void bind(MenuItem item) {
            itemName.setText(item.getName());
            itemDescription.setText(item.getDescription());
            itemPrice.setText(String.format(Locale.US, "$%.2f", item.getPrice()));

            // Dynamically set image
            Context context = itemImage.getContext();
            String imageRef = item.getImageName();
            if (imageRef != null && (imageRef.startsWith("file://") || imageRef.startsWith("content://")
                    || imageRef.contains("/"))) {
                itemImage.setImageURI(android.net.Uri.parse(imageRef));
            } else {
                int imageResId = context.getResources().getIdentifier(imageRef, "drawable", context.getPackageName());
                if (imageResId != 0) {
                    itemImage.setImageResource(imageResId);
                } else {
                    itemImage.setImageResource(R.drawable.ic_restaurant_menu); // Default fallback
                }
            }

            // Staff Mode Logic
            if (isStaffMode) {
                staffActionsLayout.setVisibility(View.VISIBLE);
                editButton.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onEditClick(item);
                });
                deleteButton.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onDeleteClick(item);
                });
            } else {
                staffActionsLayout.setVisibility(View.GONE);
            }
        }
    }
}

package com.example.restaurantmanagementapp.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.model.MenuItem;
import com.example.restaurantmanagementapp.ui.reservation.ReservationBookingActivity;

public class MenuBrowseActivity extends AppCompatActivity {

    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> allMenuItems;
    private ChipGroup categoryChipGroup;
    private boolean isStaffMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_menu_browsing);

        isStaffMode = getIntent().getBooleanExtra("IS_STAFF_MODE", false);

        // Initialize Views
        menuRecyclerView = findViewById(R.id.recycler_view_menu_items);
        categoryChipGroup = findViewById(R.id.chip_group_categories);

        // Setup the menu data and RecyclerView
        allMenuItems = new ArrayList<>();
        loadAllMenuItems();
        setupRecyclerView();

        // Setup chip group for filtering
        setupCategoryFilters();

        // Setup back button
        findViewById(R.id.button_back).setOnClickListener(v -> onBackPressed());

        // Setup FAB
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton fab = findViewById(
                R.id.fab_book_table);
        if (isStaffMode) {
            fab.setText("Add New Dish");
            fab.setIconResource(R.drawable.ic_add);
            // Reset Booking Button style overrides if necessary
            fab.setBackgroundTintList(
                    androidx.core.content.ContextCompat.getColorStateList(this, R.color.pg_secondary_accent));
            fab.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.pg_text_primary));
            fab.setIconTint(androidx.core.content.ContextCompat.getColorStateList(this, R.color.pg_text_primary));

            fab.setOnClickListener(v -> {
                startActivity(new Intent(MenuBrowseActivity.this, ManageMenuActivity.class));
            });
        } else {
            fab.setOnClickListener(v -> {
                startActivity(new Intent(MenuBrowseActivity.this, ReservationBookingActivity.class));
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if database needs seeding, then load
        loadAllMenuItems();
        // Re-setup adapter to reflect data changes
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        menuAdapter = new MenuAdapter(allMenuItems, isStaffMode, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(MenuItem item) {
                Intent intent = new Intent(MenuBrowseActivity.this, ManageMenuActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                intent.putExtra("ITEM_NAME", item.getName());
                intent.putExtra("ITEM_DESC", item.getDescription());
                intent.putExtra("ITEM_PRICE", item.getPrice());
                intent.putExtra("ITEM_CATEGORY", item.getCategory());
                intent.putExtra("ITEM_IMAGE", item.getImageName());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(MenuItem item) {
                new android.app.AlertDialog.Builder(MenuBrowseActivity.this)
                        .setTitle("Delete Dish")
                        .setMessage("Are you sure you want to delete " + item.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            new Thread(() -> {
                                AppDatabase.getDatabase(MenuBrowseActivity.this).menuItemDao().delete(item);
                                runOnUiThread(() -> {
                                    Toast.makeText(MenuBrowseActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                    loadAllMenuItems();
                                    setupRecyclerView();
                                });
                            }).start();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuRecyclerView.setAdapter(menuAdapter);
    }

    private void loadAllMenuItems() {
        new Thread(() -> {
            com.example.restaurantmanagementapp.data.local.AppDatabase db = com.example.restaurantmanagementapp.data.local.AppDatabase
                    .getDatabase(this);
            allMenuItems = db.menuItemDao().getAllMenuItems();

            // If empty, it might be first run and Database callback hasn't finished seeding
            // yet.
            // For simplicity in this non-reactive setup, we might wait or just show empty.
            // Or we can keep the manual seeding fallback but async.
            if (allMenuItems.isEmpty()) {
                seedDatabase(db);
                allMenuItems = db.menuItemDao().getAllMenuItems();
            }

            runOnUiThread(() -> setupRecyclerView());
        }).start();
    }

    private void seedDatabase(com.example.restaurantmanagementapp.data.local.AppDatabase db) {
        // Double check count to prevent duplicate if called concurrently
        if (db.menuItemDao().getAllMenuItems().size() > 0)
            return;

        List<MenuItem> initialItems = new ArrayList<>();

        // --- 5 VEGETARIAN DISHES ---
        initialItems.add(new MenuItem("Margherita Pizza", "Classic pizza with fresh mozzarella, tomatoes, and basil.",
                12.99, "Vegetarian", "ic_food_pizza"));
        initialItems.add(new MenuItem("Mushroom Risotto",
                "Creamy Arborio rice with wild mushrooms and Parmesan cheese.", 16.50, "Vegetarian", "ic_food_pasta"));
        initialItems.add(new MenuItem("Gnocchi al Pesto", "Soft potato gnocchi tossed in a vibrant basil pesto sauce.",
                14.00, "Vegetarian", "ic_food_pasta"));
        initialItems.add(new MenuItem("Veggie Burger",
                "Quinoa and black bean patty with fresh veggies.", 15.50, "Vegetarian", "ic_food_burger"));
        initialItems.add(new MenuItem("Penne Arrabbiata", "Penne pasta in a spicy tomato sauce with garlic and chili.",
                13.00, "Vegetarian", "ic_food_pasta"));

        // --- 5 NON-VEGETARIAN DISHES ---
        initialItems.add(
                new MenuItem("Spaghetti Carbonara", "Spaghetti with pancetta, pecorino cheese, and a creamy egg sauce.",
                        17.00, "Non-Vegetarian", "ic_food_pasta"));
        initialItems.add(
                new MenuItem("Chicken Alfredo", "Fettuccine pasta with grilled chicken in a rich Parmesan cream sauce.",
                        18.50, "Non-Vegetarian", "ic_food_pasta"));
        initialItems.add(new MenuItem("Beef Burger",
                "Juicy beef patty with cheddar, lettuce, and tomato.", 16.99, "Non-Vegetarian", "ic_food_burger"));
        initialItems.add(
                new MenuItem("Pepperoni Pizza", "Classic pizza topped with spicy pepperoni slices.",
                        22.00, "Non-Vegetarian", "ic_food_pizza"));
        initialItems.add(new MenuItem("BBQ Chicken Pizza",
                "Pizza topped with BBQ sauce, grilled chicken, and red onions.", 15.00, "Non-Vegetarian",
                "ic_food_pizza"));

        // --- 2 DESSERTS ---
        initialItems.add(
                new MenuItem("Tiramisu", "Classic Italian dessert with coffee-soaked ladyfingers and mascarpone cream.",
                        8.50, "Dessert", "ic_food_dessert"));
        initialItems.add(new MenuItem("Panna Cotta", "Silky smooth cooked cream dessert served with a berry coulis.",
                7.99, "Dessert", "ic_food_dessert"));

        // --- 2 BEVERAGES ---
        initialItems.add(new MenuItem("Coca Cola", "Classic fizzy drink.",
                3.99, "Beverage", "ic_food_drink"));
        initialItems.add(new MenuItem("Lemonade", "Freshly squeezed lemon juice.",
                4.50, "Beverage", "ic_food_drink"));

        for (MenuItem item : initialItems) {
            db.menuItemDao().insert(item);
        }
    }

    private void setupCategoryFilters() {
        categoryChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty())
                return; // Avoid crash if selection is cleared

            int checkedId = checkedIds.get(0); // singleSelection is true
            if (checkedId == R.id.chip_all) {
                menuAdapter.filterList(allMenuItems);
            } else if (checkedId == R.id.chip_veg) {
                filterMenuByCategory("Vegetarian"); // Mapped to correct string
            } else if (checkedId == R.id.chip_non_veg) {
                filterMenuByCategory("Non-Vegetarian"); // Mapped to correct string
            } else if (checkedId == R.id.chip_desserts) {
                filterMenuByCategory("Dessert");
            }

        });
    }

    private void filterMenuByCategory(String category) {
        // Using Java Streams to filter the list
        List<MenuItem> filteredList = allMenuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        menuAdapter.filterList(filteredList);
    }
}

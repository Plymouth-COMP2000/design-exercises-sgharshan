package com.example.restaurantmanagementapp.ui.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.MenuItemDao;
import com.example.restaurantmanagementapp.data.model.MenuItem;
import com.example.restaurantmanagementapp.databinding.ActivityManageMenuBinding;
import com.example.restaurantmanagementapp.data.remote.ApiClient;
import com.example.restaurantmanagementapp.data.remote.ApiService;

import android.content.Intent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMenuActivity extends AppCompatActivity {

    private ActivityManageMenuBinding binding;

    // Member variable to hold editing item ID
    private int editingMenuItemId = -1;
    private String selectedImageName = "ic_food_pasta"; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Toolbar
        binding.toolbarManageMenu.setNavigationOnClickListener(v -> finish());

        // Setup Spinner
        java.util.List<String> categories = new java.util.ArrayList<>();
        categories.add("Vegetarian");
        categories.add("Non-Vegetarian");
        categories.add("Dessert");
        categories.add("Beverage");
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);

        setupImageSelectors();

        // Check for Edit Mode
        if (getIntent().hasExtra("ITEM_ID")) {
            editingMenuItemId = getIntent().getIntExtra("ITEM_ID", -1);
            String name = getIntent().getStringExtra("ITEM_NAME");
            String desc = getIntent().getStringExtra("ITEM_DESC");
            double price = getIntent().getDoubleExtra("ITEM_PRICE", 0.0);
            String category = getIntent().getStringExtra("ITEM_CATEGORY");
            String image = getIntent().getStringExtra("ITEM_IMAGE");

            binding.editTextName.setText(name);
            binding.editTextDesc.setText(desc);
            binding.editTextPrice.setText(String.valueOf(price));

            // Set spinner selection
            if (category != null) {
                int spinnerPosition = adapter.getPosition(category);
                if (spinnerPosition >= 0) {
                    binding.spinnerCategory.setSelection(spinnerPosition);
                }
            }

            // Set Image Selection
            if (image != null) {
                selectImage(image);
            }

            binding.buttonAddItem.setText("Update Item");
            binding.toolbarManageMenu.setTitle("Edit Menu Item");
        }

        binding.buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void setupImageSelectors() {
        // Register ActivityResultLauncher for picking images
        androidx.activity.result.ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                        android.net.Uri uri = result.getData().getData();
                        if (uri != null) {
                            // Persist permission
                            try {
                                getContentResolver().takePersistableUriPermission(uri,
                                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            selectedImageName = uri.toString();
                            binding.imagePreview.setImageURI(uri);
                        }
                    }
                });

        binding.buttonSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
    }

    private void selectImage(String imageName) {
        selectedImageName = imageName;
        if (imageName != null && (imageName.startsWith("file://") || imageName.startsWith("content://"))) {
            binding.imagePreview.setImageURI(android.net.Uri.parse(imageName));
        } else {
            // Fallback for old resources or default
            if (imageName == null || imageName.isEmpty())
                imageName = "ic_food_pasta";
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                binding.imagePreview.setImageResource(resId);
            } else {
                binding.imagePreview.setImageResource(R.drawable.ic_restaurant_menu);
            }
        }
    }

    private void saveItem() {
        String name = binding.editTextName.getText().toString().trim();
        String desc = binding.editTextDesc.getText().toString().trim();
        String priceStr = binding.editTextPrice.getText().toString().trim();
        String category = binding.spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Name and Price are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String imageName = selectedImageName;

        AppDatabase db = AppDatabase.getDatabase(this);
        MenuItemDao dao = db.menuItemDao();

        if (editingMenuItemId != -1) {
            // Update Existing
            MenuItem updateItem = new MenuItem(name, desc, price, category, imageName);
            updateItem.setId(editingMenuItemId); // Assuming
                                                 // setter
                                                 // exists or
                                                 // field is
                                                 // public

            // Run on background thread
            new Thread(() -> {
                dao.update(updateItem);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Item Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        } else {
            // Insert New
            MenuItem newItem = new MenuItem(name, desc, price, category, imageName);
            new Thread(() -> {
                dao.insert(newItem);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        }
    }
}

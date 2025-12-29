package com.example.restaurantmanagementapp.ui.auth;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.UserDao;
import com.example.restaurantmanagementapp.data.model.User;
import com.example.restaurantmanagementapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonRegister.setOnClickListener(v -> handleRegister());

        // Setup Spinner
        java.util.List<String> roles = new java.util.ArrayList<>();
        roles.add("Guest");
        roles.add("Staff");
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRole.setAdapter(adapter);
    }

    private void handleRegister() {
        String name = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmailReg.getText().toString().trim();
        String password = binding.editTextPasswordReg.getText().toString().trim();

        // Safe check for spinner selection
        String role = "Guest";
        if (binding.spinnerRole.getSelectedItem() != null) {
            role = binding.spinnerRole.getSelectedItem().toString();
        }

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        final String finalRole = role;

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            UserDao dao = db.userDao();

            if (dao.getUser(email) != null) {
                runOnUiThread(() -> Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show());
            } else {
                // Determine names
                String firstName = name;
                String lastName = "";
                if (name.contains(" ")) {
                    String[] parts = name.split(" ", 2);
                    firstName = parts[0];
                    lastName = parts[1];
                }

                // Constructor: username, password, firstname, lastname, email, contact,
                // usertype
                User newUser = new User(email.split("@")[0], password, firstName, lastName, email, "0000000000",
                        finalRole);
                dao.insert(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Registration Successful! Please Login.", Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        }).start();
    }
}

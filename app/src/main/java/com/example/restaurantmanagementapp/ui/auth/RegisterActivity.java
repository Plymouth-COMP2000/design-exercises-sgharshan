package com.example.restaurantmanagementapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagementapp.data.model.User;
import com.example.restaurantmanagementapp.data.repository.UserRepository;
import com.example.restaurantmanagementapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository(getApplication());

        binding.buttonRegister.setOnClickListener(v -> handleRegister());

        // Setup Back Button
        binding.buttonBack.setOnClickListener(v -> finish());

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

        // Determine names
        String firstName = name;
        String lastName = "";
        if (name.contains(" ")) {
            String[] parts = name.split(" ", 2);
            firstName = parts[0];
            lastName = parts[1];
        }

        // Using email prefix as username for now
        String username = email.split("@")[0];
        final String finalRole = role;

        User newUser = new User(username, password, firstName, lastName, email, "0000000000", finalRole);

        binding.buttonRegister.setEnabled(false);
        Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show();

        userRepository.register(newUser, new UserRepository.RegisterCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Registration Successful! Please Login.", Toast.LENGTH_LONG)
                            .show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    binding.buttonRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

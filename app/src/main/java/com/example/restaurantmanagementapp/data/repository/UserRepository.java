package com.example.restaurantmanagementapp.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.UserDao;
import com.example.restaurantmanagementapp.data.model.User;
import com.example.restaurantmanagementapp.data.model.UserResponse;
import com.example.restaurantmanagementapp.data.remote.ApiClient;
import com.example.restaurantmanagementapp.data.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private UserDao userDao;
    private ApiService apiService;
    private static final String STUDENT_ID = "sgharshan";

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        userDao = db.userDao();
        apiService = ApiClient.getService();
    }

    public void login(String username, String password, LoginCallback callback) {
        // 1. Try API Login
        Call<UserResponse> call = apiService.readUser(STUDENT_ID, username);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getUser() != null) {
                    User apiUser = response.body().getUser();
                    if (apiUser.password.equals(password)) {
                        // Success - Update Local DB
                        saveUserLocally(apiUser);
                        callback.onSuccess(apiUser);
                    } else {
                        callback.onError("Invalid Password");
                    }
                } else {
                    // Fallback to Local
                    checkLocalLogin(username, password, callback);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Network Error - Fallback to Local
                checkLocalLogin(username, password, callback);
            }
        });
    }

    public void register(User user, RegisterCallback callback) {
        Call<Void> call = apiService.createUser(STUDENT_ID, user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    saveUserLocally(user);
                    callback.onSuccess();
                } else {
                    callback.onError("Registration Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network Error: " + t.getMessage());
            }
        });
    }

    private void checkLocalLogin(String usernameOrEmail, String password, LoginCallback callback) {
        new Thread(() -> {
            User user = userDao.login(usernameOrEmail, password);
            if (user != null) {
                callback.onSuccess(user);
            } else {
                callback.onError("Login Failed. Invalid credentials.");
            }
        }).start();
    }

    private void saveUserLocally(User user) {
        new Thread(() -> {
            userDao.insert(user);
        }).start();
    }

    public interface LoginCallback {
        void onSuccess(User user);

        void onError(String message);
    }

    public interface RegisterCallback {
        void onSuccess();

        void onError(String message);
    }
}

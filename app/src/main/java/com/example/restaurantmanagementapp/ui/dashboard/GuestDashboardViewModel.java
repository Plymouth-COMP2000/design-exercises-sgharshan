package com.example.restaurantmanagementapp.ui.dashboard;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.restaurantmanagementapp.data.model.User;
import com.example.restaurantmanagementapp.util.SessionManager;

public class GuestDashboardViewModel extends AndroidViewModel {

    private final SessionManager sessionManager;
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public GuestDashboardViewModel(@NonNull Application application) {
        super(application);
        sessionManager = new SessionManager(application);
        loadUser();
    }

    private void loadUser() {
        if (sessionManager.isLoggedIn()) {
            user.setValue(sessionManager.getUser());
        }
    }

    public LiveData<User> getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public void logout() {
        sessionManager.logout();
        user.setValue(null);
    }
}

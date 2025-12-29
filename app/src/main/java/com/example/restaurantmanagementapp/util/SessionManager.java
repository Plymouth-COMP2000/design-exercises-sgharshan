package com.example.restaurantmanagementapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.restaurantmanagementapp.data.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "RestaurantAppSession";
    private static final String KEY_USER = "key_user";
    private static final String KEY_IS_LOGGED_IN = "key_is_logged_in";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public User getUser() {
        if (!isLoggedIn())
            return null;
        String userJson = pref.getString(KEY_USER, null);
        if (userJson != null) {
            return new Gson().fromJson(userJson, User.class);
        }
        return null;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}

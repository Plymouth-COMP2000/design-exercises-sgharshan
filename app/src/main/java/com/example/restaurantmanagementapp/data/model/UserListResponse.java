package com.example.restaurantmanagementapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserListResponse {
    @SerializedName("users")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

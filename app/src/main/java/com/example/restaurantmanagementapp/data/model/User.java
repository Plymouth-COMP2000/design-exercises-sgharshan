package com.example.restaurantmanagementapp.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("email")
    public String email;
    @SerializedName("contact")
    public String contact;
    @SerializedName("usertype")
    public String usertype; // "staff" or "guest" (or "student" etc.)

    public User(@NonNull String username, String password, String firstname, String lastname, String email,
            String contact, String usertype) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.usertype = usertype;
    }

    // Helper to check if staff
    public boolean isStaff() {
        return "staff".equalsIgnoreCase(usertype) || "admin".equalsIgnoreCase(usertype);
    }
}

package com.example.restaurantmanagementapp.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.restaurantmanagementapp.data.model.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM users WHERE (email = :identifier OR username = :identifier) AND password = :password")
    User login(String identifier, String password);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUser(String email);
}

package com.example.restaurantmanagementapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantmanagementapp.data.model.MenuItem;

import java.util.List;

@Dao
public interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MenuItem item);

    @Update
    void update(MenuItem item);

    @Delete
    void delete(MenuItem item);

    @Query("SELECT * FROM menu_items")
    List<MenuItem> getAllMenuItems();

    @Query("SELECT * FROM menu_items WHERE category = :category")
    List<MenuItem> getMenuItemsByCategory(String category);
}

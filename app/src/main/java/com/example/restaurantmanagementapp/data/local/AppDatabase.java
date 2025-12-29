package com.example.restaurantmanagementapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.restaurantmanagementapp.data.model.MenuItem;
import com.example.restaurantmanagementapp.data.model.Reservation;
import com.example.restaurantmanagementapp.data.model.User;

@Database(entities = { User.class, MenuItem.class, Reservation.class }, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract MenuItemDao menuItemDao();

    public abstract ReservationDao reservationDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "restaurant_database")
                            .allowMainThreadQueries() // For simplicity in this assignment
                            .fallbackToDestructiveMigration()
                            .build();

                    // Synchronous seeding to ensure data exists before Login
                    try {
                        UserDao dao = INSTANCE.userDao();
                        if (dao.getUser("staff@test.com") == null) {
                            dao.insert(new User("staff_admin", "password", "Admin", "Staff",
                                    "staff@test.com", "1234567890", "staff"));
                            dao.insert(new User("guest_user", "password", "Guest", "User", "guest@test.com",
                                    "0987654321", "guest"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Swallow error to allow app to start, but log it
                    }
                }
            }
        }
        return INSTANCE;
    }
}

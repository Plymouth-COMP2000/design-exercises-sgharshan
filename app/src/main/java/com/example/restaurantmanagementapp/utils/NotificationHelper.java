package com.example.restaurantmanagementapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.restaurantmanagementapp.R;
import com.example.restaurantmanagementapp.ui.settings.SettingsActivity;

public class NotificationHelper {

    private static final String CHANNEL_ID = "restaurant_app_channel";
    private static final String CHANNEL_NAME = "Restaurant Notifications";
    private static final String CHANNEL_DESC = "Notifications for reservations and updates";

    public static void showNotification(Context context, String title, String message) {
        // Check User Preference
        SharedPreferences prefs = context.getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean isEnabled = prefs.getBoolean(SettingsActivity.KEY_NOTIFICATIONS_ENABLED, true);

        if (!isEnabled) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this exists or use a system icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}

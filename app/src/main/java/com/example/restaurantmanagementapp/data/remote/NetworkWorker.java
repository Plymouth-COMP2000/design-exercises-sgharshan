package com.example.restaurantmanagementapp.data.remote;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.restaurantmanagementapp.data.model.MenuItem;
import com.example.restaurantmanagementapp.data.local.AppDatabase;
import com.example.restaurantmanagementapp.data.local.MenuItemDao;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkWorker extends Worker {

    public NetworkWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // This runs on a background thread
        Log.d("NetworkWorker", "Starting background sync...");

        // User requested "no need of server", so we will skip the actual network call.
        // In a real app, this would fetch data from the API.

        /*
         * ApiService apiService = ApiClient.getService();
         * Call<List<MenuItem>> call = apiService.getMenu();
         * 
         * try {
         * Response<List<MenuItem>> response = call.execute(); // Synchronous call
         * if (response.isSuccessful() && response.body() != null) {
         * List<MenuItem> menuItems = response.body();
         * 
         * // Save to Local DB
         * AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
         * MenuItemDao dao = db.menuItemDao();
         * 
         * for (MenuItem item : menuItems) {
         * dao.insert(item);
         * }
         * 
         * Log.d("NetworkWorker", "Sync successful: " + menuItems.size() +
         * " items updated.");
         * return Result.success();
         * } else {
         * Log.e("NetworkWorker", "Sync failed: " + response.message());
         * return Result.retry();
         * }
         * } catch (IOException e) {
         * Log.e("NetworkWorker", "Sync error", e);
         * return Result.retry();
         * }
         */

        Log.d("NetworkWorker", "Network sync skipped (Local Mode).");
        return Result.success();
    }
}

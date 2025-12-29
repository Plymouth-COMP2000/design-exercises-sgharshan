package com.example.restaurantmanagementapp.data.remote;

import com.example.restaurantmanagementapp.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String BASE_URL = "http://10.240.72.69/comp2000/coursework/";

    // 1. Create Student Database
    @POST("create_student/{student_id}")
    Call<Void> createStudent(@Path("student_id") String studentId);

    // 2. Create User Entry
    @POST("create_user/{student_id}")
    Call<Void> createUser(@Path("student_id") String studentId, @Body User user);

    // 3. Read All Users
    @GET("read_all_users/{student_id}")
    Call<List<User>> readAllUsers(@Path("student_id") String studentId);
}

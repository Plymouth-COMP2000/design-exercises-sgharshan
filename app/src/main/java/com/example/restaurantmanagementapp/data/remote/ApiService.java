package com.example.restaurantmanagementapp.data.remote;

import com.example.restaurantmanagementapp.data.model.User;
import com.example.restaurantmanagementapp.data.model.UserListResponse;
import com.example.restaurantmanagementapp.data.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<UserListResponse> readAllUsers(@Path("student_id") String studentId);

    // 4. Read Specific User
    @GET("read_user/{student_id}/{user_id}")
    Call<UserResponse> readUser(@Path("student_id") String studentId, @Path("user_id") String userId);

    // 5. Update User
    @PUT("update_user/{student_id}/{user_id}")
    Call<Void> updateUser(@Path("student_id") String studentId, @Path("user_id") String userId, @Body User user);

    // 6. Delete User
    @DELETE("delete_user/{student_id}/{user_id}")
    Call<Void> deleteUser(@Path("student_id") String studentId, @Path("user_id") String userId);
}

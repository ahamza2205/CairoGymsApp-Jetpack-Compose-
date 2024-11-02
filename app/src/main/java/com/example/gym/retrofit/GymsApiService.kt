package com.example.gym.retrofit

import retrofit2.http.GET

interface GymsApiService {
    @GET("gyms.json")
    suspend fun getGyms () : List<Gym>
}
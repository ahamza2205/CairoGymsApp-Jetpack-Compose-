package com.example.gym.retrofit

import com.google.gson.annotations.SerializedName

data class Gym(
    val id : Int,
    @SerializedName("gym_name")
    val name: String,
    @SerializedName("gym_location")
    val place: String,
    var isFavorite : Boolean = false
)


/*

{
    "gym_location": "20 El-Gihad, Mit Akaba, Agouza, Giza Governorate 3754204, Egypt",
    "gym_name": "UpTown Gym",
    "id": 0,
    "is_open": true
}

*/

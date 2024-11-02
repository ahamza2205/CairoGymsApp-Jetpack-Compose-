package com.example.gym

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym.retrofit.Gym
import com.example.gym.retrofit.GymsApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GymsViewModel(
    val stateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(emptyList<Gym>())
    private var apiService: GymsApiService

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                "https://cairo-gyms-9c9b0-default-rtdb.firebaseio.com/"
            )
            .build()
        apiService = retrofit.create(GymsApiService::class.java)

        getGyms()
    }


    private fun getGyms() {

        viewModelScope.launch( errorHandler) {
            val gyms = getGymsFromRemoteDatabase()
            state = gyms.restoreSelectedGyms()
        }
    }

    private suspend fun getGymsFromRemoteDatabase() = withContext(Dispatchers.IO) { apiService.getGyms() }


    fun toggleFavorite(gymId: Int) {
        val gyms = state.toMutableList()
        val itemIndex = gyms.indexOfFirst { it.id == gymId }
        gyms[itemIndex] = gyms[itemIndex].copy(isFavorite = !gyms[itemIndex].isFavorite)
        storedSelectedGyms(gyms[itemIndex])
        state = gyms
    }

    fun storedSelectedGyms(gym: Gym) {
        val saveHandleList = stateHandle.get<List<Int>>(FAV_ID).orEmpty().toMutableList()
        if (gym.isFavorite) saveHandleList.add(gym.id)
        else saveHandleList.remove(gym.id)
        stateHandle[FAV_ID] = saveHandleList
    }

    fun List<Gym>.restoreSelectedGyms(): List<Gym> {
        stateHandle.get<List<Int>>(FAV_ID)?.let { savedIDs ->
            savedIDs.forEach { gymId ->
                this.find { it.id == gymId }?.isFavorite = true
            }
        }
        return this
    }

    companion object {
        const val FAV_ID = "selectedGyms"
    }
}




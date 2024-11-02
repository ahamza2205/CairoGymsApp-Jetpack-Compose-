    package com.example.gym.gymdetails

    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import androidx.lifecycle.SavedStateHandle
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.gym.retrofit.Gym
    import com.example.gym.retrofit.GymsApiService
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory

    class GymDetailsViewModel(
       private val stateHandle: SavedStateHandle
    ) : ViewModel() {

        var state by mutableStateOf<Gym?>(null)
        private var apiService: GymsApiService

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

            val gymId = stateHandle.get<Int>("gym_id") ?: 0
            getGym(gymId)
        }

        private fun getGym(id: Int) {
            viewModelScope.launch {
                val gym = getGymFromRemoteDatabase(id)
                state = gym
            }
        }

        suspend fun getGymFromRemoteDatabase(id: Int) = withContext(Dispatchers.IO) {
            apiService.getGym(id).values.first()
        }
    }
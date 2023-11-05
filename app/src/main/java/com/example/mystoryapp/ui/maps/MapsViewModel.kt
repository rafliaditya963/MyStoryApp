package com.example.mystoryapp.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.model.FileUploadResponse
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.model.StoryResponse
import com.example.mystoryapp.model.UserModel
import com.example.mystoryapp.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val preference: UserRepository) : ViewModel() {
    val _stories = MutableLiveData<ArrayList<ListStoryItem>?>()
    private val _loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val loading: LiveData<Boolean> = _loading

    init {
        errorMessage.value = ""
        _loading.value = false
    }

    fun getMaps(token: String) {
        _loading.value = true

        val locationService = ApiConfig.getApiService().getLocation("Bearer $token", 1)
        locationService.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _loading.value = false

                if (response.isSuccessful) {
                    val result = response.body()

                    if (result != null) {
                        errorMessage.value = ""
                        _stories.value = (result.listStory)
                    }
                } else {
                    Log.e(TAG, "Error message: ${response.message()}")
                    errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "Error message: ${t.message}")
                _loading.value = false
                errorMessage.value = t.message
            }
        })
    }






    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}
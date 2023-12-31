package com.example.mystoryapp.ui.login

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.model.AuthenticationResponse
import com.example.mystoryapp.model.UserModel
import com.example.mystoryapp.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: UserRepository) : ViewModel() {
    private val _emailValid = MutableLiveData<Boolean>()
    private val _passwordValid = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    val emailValid: LiveData<Boolean> = _emailValid
    val passwordValid: LiveData<Boolean> = _passwordValid
    val errorMsg = MutableLiveData<String>()

    val loading: LiveData<Boolean> = _loading

    init {
        _emailValid.value = false
        _passwordValid.value = false
        errorMsg.value = ""
        _loading.value = false
    }

    fun updateEmailStatus(status: Boolean) {
        _emailValid.value = status
    }

    fun updatePasswordStatus(status: Boolean) {
        _passwordValid.value = status
    }

    fun login(email: String, password: String) {
        _loading.value = true

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<AuthenticationResponse> {
            override fun onResponse(
                call: Call<AuthenticationResponse>,
                response: Response<AuthenticationResponse>
            ) {
                _loading.value = false

                if (response.isSuccessful) {
                    val result = response.body()

                    if (result != null) {
                        if (!result.error) {
                            errorMsg.value = ""
                            viewModelScope.launch {
                                preference.login(
                                    UserModel(
                                        result.loginResult?.name.toString(),
                                        email,
                                        password,
                                        result.loginResult?.token.toString(),
                                        isLogin = true,
                                        lat = 0.0,
                                        lon = 0.0
                                    )
                                )
                            }
                        }
                    }
                } else {
                    Log.e(TAG, response.message())
                    errorMsg.value = response.message()
                }
            }

            override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                Log.e(TAG, "Error message: ${t.message}")
                _loading.value = false
                errorMsg.value = t.message
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            preference.saveSession(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
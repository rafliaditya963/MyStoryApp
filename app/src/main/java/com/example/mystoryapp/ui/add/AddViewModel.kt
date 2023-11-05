package com.example.mystoryapp.ui.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.R
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.model.FileUploadResponse
import com.example.mystoryapp.model.UserModel
import com.example.mystoryapp.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import com.example.mystoryapp.utils.uriToFile
import com.example.mystoryapp.utils.reduceFileImage
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response

class AddViewModel(private val preference: UserRepository) :
    ViewModel() {

    var token = ""
    val loading = MutableLiveData<Boolean>()
    private val messageLiveData =
        MutableLiveData<Pair<Boolean, String>>()

    fun getMessageLiveData(): LiveData<Pair<Boolean, String>> {
        return messageLiveData
    }

    fun showMessage(success: Boolean, message: String) {
        messageLiveData.value = Pair(success, message)
    }

    init {
        loading.value = false
    }

    fun uploadImage(uri: Uri, description: String,  context: Context) {
        loading.value = true

        val imageFile = uriToFile(uri, context).reduceFileImage()
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

        val service = ApiConfig.getApiService()
            .uploadImage("Bearer $token", imageMultipart, descriptionRequestBody)

        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                loading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (!responseBody.error) {
                            showMessage(true, responseBody.message)
                        } else {
                            showMessage(
                                false,
                                context.getString(R.string.camera_failed_value) + " ${response.message()}"
                            )
                        }
                    }
                } else {
                    showMessage(
                        false,
                        context.getString(R.string.camera_failed_value) + " ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                loading.value = false
                showMessage(
                    false,
                    context.getString(R.string.camera_failed_value) + " ${t.message}"
                )
            }
        })
    }


    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }
}
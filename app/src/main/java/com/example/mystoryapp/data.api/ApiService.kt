package com.example.mystoryapp.data.api

import com.example.mystoryapp.model.AuthenticationResponse
import com.example.mystoryapp.model.StoryResponse
import com.example.mystoryapp.model.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthenticationResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthenticationResponse>

    @GET("stories")
    fun getLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
    ) : Call<StoryResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") token: String
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>
}

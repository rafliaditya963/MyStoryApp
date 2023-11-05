package com.example.mystoryapp.model

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<ListStoryItem>,
    @field:SerializedName("message")
    val message: String
)
package com.example.mystoryapp.model

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val lat: Double,
    val lon: Double,
    val isLogin: Boolean = false
)
package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.data.api.UserPreference
import com.example.mystoryapp.repository.UserRepository
import com.example.mystoryapp.data.api.dataStore
import com.example.mystoryapp.data.api.local.StoryDatabase

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(pref, database, apiService)
    }
}
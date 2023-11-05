package com.example.mystoryapp.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.model.UserModel
import com.example.mystoryapp.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val preference: UserRepository) : ViewModel() {
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> =
        preference.getStories(token).cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }
}
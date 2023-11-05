package com.example.mystoryapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.di.Injection
import com.example.mystoryapp.repository.UserRepository
import com.example.mystoryapp.ui.add.AddViewModel
import com.example.mystoryapp.ui.login.LoginViewModel
import com.example.mystoryapp.ui.main.MainViewModel
import com.example.mystoryapp.ui.maps.MapsViewModel

class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository) as T
            modelClass.isAssignableFrom(AddViewModel::class.java) -> AddViewModel(repository) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
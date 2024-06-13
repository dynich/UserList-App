package com.example.userlistapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.userlistapp.Repository.UserRepository
import com.example.userlistapp.database.FavoriteUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepository(application)

    suspend fun isFavorite(user: String): Boolean {
        return userRepository.isFavorite(user)
    }

    fun insert(user: FavoriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insert(user)
        }
    }

    fun delete(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.delete(user)
        }
    }
}

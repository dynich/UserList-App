package com.example.userlistapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.userlistapp.Repository.UserRepository
import com.example.userlistapp.database.FavoriteUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepository(application)

    val favoriteUsers: LiveData<List<FavoriteUser>> = userRepository.getAllFavoriteUsers()
    fun fetchFavoriteUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAllFavoriteUsers()
        }
    }
}
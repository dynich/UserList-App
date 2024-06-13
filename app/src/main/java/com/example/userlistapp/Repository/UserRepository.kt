package com.example.userlistapp.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.userlistapp.database.AppDatabase
import com.example.userlistapp.database.FavoriteUser
import com.example.userlistapp.database.FavoriteUserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(application: Application) {
    private val userDao: FavoriteUserDao

    init {
        val db = AppDatabase.getDatabase(application)
        userDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        // LiveData can be observed directly
        return userDao.getFavoriteUsers()
    }

    suspend fun insert(user: FavoriteUser) {
        withContext(Dispatchers.IO) {
            userDao.addFavoriteUser(user)
        }
    }

    suspend fun delete(user: String) {
        withContext(Dispatchers.IO) {
            userDao.removeFavoriteUser(user)
        }
    }

    suspend fun isFavorite(user: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.isFavorite(user)
        }
    }
}

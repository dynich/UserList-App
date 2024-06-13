package com.example.userlistapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteUser(user: FavoriteUser)

    @Query("DELETE FROM favorite_users WHERE userId = :userId")
    fun removeFavoriteUser(userId: String)

    @Query("SELECT * FROM favorite_users")
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_users WHERE userId = :userId)")
    fun isFavorite(userId: String): Boolean
}

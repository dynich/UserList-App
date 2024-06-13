package com.example.userlistapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class FavoriteUser(
    @PrimaryKey val userId: String,
    val avatarUrl: String
)

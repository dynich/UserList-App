package com.example.userlistapp


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int
)

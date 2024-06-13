package com.example.userlistapp

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items") val items: List<User>
)

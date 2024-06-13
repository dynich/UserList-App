package com.example.userlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        recyclerView = findViewById(R.id.recyclerView)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        // Initialize RecyclerView and FavoriteAdapter
        favoriteAdapter = FavoriteAdapter(emptyList()) { username ->
            // Handle item click if needed
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = favoriteAdapter

        // Observe favorite users from ViewModel
        viewModel.favoriteUsers.observe(this, { favoriteUsers ->
            favoriteAdapter.updateData(favoriteUsers)
        })
    }
}

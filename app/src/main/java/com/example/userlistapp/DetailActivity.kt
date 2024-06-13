package com.example.userlistapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.userlistapp.api.RetrofitClient
import com.example.userlistapp.database.FavoriteUser
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var userAvatar: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var userStatsTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var fabFavorite: FloatingActionButton

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        username = intent.getStringExtra("username") ?: ""
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        userAvatar = findViewById(R.id.userAvatar)
        usernameTextView = findViewById(R.id.usernameTextView)
        userStatsTextView = findViewById(R.id.userStatsTextView)
        progressBar = findViewById(R.id.progressBar)
        fabFavorite = findViewById(R.id.fab_favorite)

        fetchUserInfo(username)

        lifecycleScope.launch {
            if (detailViewModel.isFavorite(username)) {
                fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }

        fabFavorite.setOnClickListener {
            lifecycleScope.launch {
                if (detailViewModel.isFavorite(username)) {
                    detailViewModel.delete(username)
                    fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    Toast.makeText(this@DetailActivity, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    val user = FavoriteUser(userId = username, avatarUrl = "")  // Make sure to update with correct data
                    detailViewModel.insert(user)
                    fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                    Toast.makeText(this@DetailActivity, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchUserInfo(username: String) {
        progressBar.visibility = View.VISIBLE
        val apiService = RetrofitClient.apiService
        apiService.getUser(username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        updateUI(it)
                        setupViewPager(it)
                    }
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Handle network error
            }
        })
    }

    private fun updateUI(user: User) {
        usernameTextView.text = user.login
        userStatsTextView.text = "Followers: ${user.followers} | Following: ${user.following}"
        Glide.with(this)
            .load(user.avatarUrl)
            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
            .into(userAvatar)
    }

    private fun setupViewPager(user: User) {
        viewPager.adapter = DetailPagerAdapter(this, user.login)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> null
            }
        }.attach()
    }

    private inner class DetailPagerAdapter(fa: AppCompatActivity, private val username: String) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FollowersFragment.newInstance(username)
                1 -> FollowingFragment.newInstance(username)
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }
}

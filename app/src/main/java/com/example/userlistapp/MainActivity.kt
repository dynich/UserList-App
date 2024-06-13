package com.example.userlistapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userlistapp.api.RetrofitClient
import com.example.userlistapp.SearchResponse
import com.example.userlistapp.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: View
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.searchView)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        userAdapter = UserAdapter(emptyList()) { username ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        // Load default users on activity creation
        loadDefaultUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchUsers(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadDefaultUsers()
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                startActivity(Intent(this, FavoriteUserActivity::class.java))
                return true
            }
            R.id.dark_mode -> {
                startActivity(Intent(this, DarkModeActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun loadDefaultUsers() {
        progressBar.visibility = View.VISIBLE
        val apiService = RetrofitClient.apiService
        apiService.getDefaultUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        userAdapter.updateData(it)
                    }
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Handle network error
            }
        })
    }

    private fun searchUsers(query: String) {
        progressBar.visibility = View.VISIBLE
        val apiService = RetrofitClient.apiService
        apiService.searchUsers(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    searchResponse?.let {
                        userAdapter.updateData(it.items)
                    }
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Handle network error
            }
        })
    }
}

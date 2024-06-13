package com.example.userlistapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userlistapp.api.RetrofitClient
import com.example.userlistapp.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var followersProgressBar: ProgressBar // Tambahkan ProgressBar
    private val apiService = RetrofitClient.apiService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME) ?: return

        recyclerView = view.findViewById(R.id.recyclerView)
        followersProgressBar = view.findViewById(R.id.followersProgressBar) // Inisialisasi ProgressBar
        userAdapter = UserAdapter(emptyList()) {}
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = userAdapter

        getFollowers(username)
    }

    private fun getFollowers(username: String) {
        followersProgressBar.visibility = View.VISIBLE // Tampilkan ProgressBar saat memuat data
        apiService.getFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                followersProgressBar.visibility = View.GONE // Sembunyikan ProgressBar setelah data terambil
                if (response.isSuccessful) {
                    val followers = response.body() ?: emptyList()
                    userAdapter.updateData(followers)
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                followersProgressBar.visibility = View.GONE // Sembunyikan ProgressBar saat terjadi error
                // Handle error
            }
        })
    }

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}

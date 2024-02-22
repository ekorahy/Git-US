package com.ekorahy.gitus.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.gitus.R
import com.ekorahy.gitus.adapter.UserAdapter
import com.ekorahy.gitus.data.remote.response.GithubResponse
import com.ekorahy.gitus.data.remote.response.ItemsItem
import com.ekorahy.gitus.data.remote.retrofit.ApiConfig
import com.ekorahy.gitus.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        findUsers(Q)

        with(binding) {
            svUser.setupWithSearchBar(sbUser)
            svUser
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    if (svUser.text.toString().isEmpty()) {
                        svUser.editText.error = getString(R.string.empty_input_warning)
                    } else {
                        sbUser.setText(svUser.text)
                        svUser.hide()
                        findUsers(svUser.text.toString())
                    }
                    true
                }
        }
    }

    private fun findUsers(q: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserByQuery(q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let { setUserData(it.items) }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(user: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(user)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val Q = "a"
    }
}
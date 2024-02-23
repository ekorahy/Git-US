package com.ekorahy.gitus.view.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekorahy.gitus.data.remote.response.ItemsItem
import com.ekorahy.gitus.data.remote.retrofit.ApiConfig
import com.ekorahy.gitus.view.follow.FollowFragment.Companion.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _listFollowersUser = MutableLiveData<List<ItemsItem>>()
    val listFollowersUser: LiveData<List<ItemsItem>> = _listFollowersUser

    private val _listFollowingUser = MutableLiveData<List<ItemsItem>>()
    val listFollowingUser: LiveData<List<ItemsItem>> = _listFollowingUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _usernameFollowers = MutableLiveData<String?>(null)
    val usernameFollowers: LiveData<String?> = _usernameFollowers

    private val _usernameFollowing = MutableLiveData<String?>(null)
    val usernameFollowing: LiveData<String?> = _usernameFollowing

    fun getFollow(username: String, isFollowers: Boolean) {
        _isLoading.value = true
        val client = if (isFollowers) ApiConfig.getApiService()
            .getFollowers(username) else ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (isFollowers) {
                        _listFollowersUser.value = response.body()
                    } else {
                        _listFollowingUser.value = response.body()
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun setUsernameFollowers(username: String) {
        _usernameFollowers.value = username
    }

    fun setUsernameFollowing(username: String) {
        _usernameFollowing.value = username
    }
}
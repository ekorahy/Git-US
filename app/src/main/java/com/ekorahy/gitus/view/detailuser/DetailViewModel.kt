package com.ekorahy.gitus.view.detailuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekorahy.gitus.data.local.entity.FavoriteUser
import com.ekorahy.gitus.data.remote.response.DetailUserResponse
import com.ekorahy.gitus.data.remote.retrofit.ApiConfig
import com.ekorahy.gitus.repository.FavoriteUserRepository
import com.ekorahy.gitus.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _username = MutableLiveData<String?>(null)
    val username: LiveData<String?> = _username

    private val _warningText = MutableLiveData<Event<String>>()
    val warningText: LiveData<Event<String>> = _warningText

    fun findDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    _warningText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _warningText.value = Event(t.message.toString())
            }
        })
    }

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun deleteByUsername(username: String) {
        mFavoriteUserRepository.deleteByUsername(username)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteUserRepository.getFavoriteUserByUsername(username)

    fun setUsername(username: String) {
        _username.value = username
    }
}
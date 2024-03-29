package com.ekorahy.gitus.view.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ekorahy.gitus.data.datastore.SettingPreferences
import com.ekorahy.gitus.data.remote.response.GithubResponse
import com.ekorahy.gitus.data.remote.response.ItemsItem
import com.ekorahy.gitus.data.remote.retrofit.ApiConfig
import com.ekorahy.gitus.utils.Event
import com.ekorahy.gitus.view.main.MainActivity.Companion.Q
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _warningText = MutableLiveData<Event<String>>()
    val warningText: LiveData<Event<String>> = _warningText

    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int> = _totalCount

    init {
        findUsers(Q)
    }

    fun findUsers(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserByQuery(q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                    _totalCount.value = response.body()?.totalCount
                } else {
                    _warningText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _warningText.value = Event(t.message.toString())
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}
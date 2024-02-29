package com.ekorahy.gitus.view.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ekorahy.gitus.data.local.FavoriteUser
import com.ekorahy.gitus.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> =
        mFavoriteUserRepository.getFavoriteUsers()
}
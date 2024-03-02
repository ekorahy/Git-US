package com.ekorahy.gitus.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ekorahy.gitus.data.local.entity.FavoriteUser
import com.ekorahy.gitus.data.local.room.FavoriteUserDao
import com.ekorahy.gitus.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getFavoriteUsers()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun deleteByUsername(username: String) {
        executorService.execute { mFavoriteUserDao.deleteByUsername(username) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteUserDao.getFavoriteUserByUsername(username)
}
package com.ekorahy.gitus.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ekorahy.gitus.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Query("DELETE from favoriteuser Where username = :username")
    fun deleteByUsername(username: String)

    @Query("SELECT * from favoriteuser ORDER BY id DESC")
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT * from favoriteuser Where username= :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>
}
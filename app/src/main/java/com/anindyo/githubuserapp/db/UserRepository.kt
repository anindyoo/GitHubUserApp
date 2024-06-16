package com.anindyo.githubuserapp.db

import androidx.lifecycle.LiveData
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.db.local.room.FavoriteUserDao
import com.anindyo.githubuserapp.db.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: FavoriteUserDao,
){
    fun getFavoriteUsers(): LiveData<List<User>> = userDao.getFavoriteUsers()

    suspend fun insertUserFavorite(user: User) {
        userDao.insertUserFavorite(user)
    }

    suspend fun deleteUserFavorite(user: User) {
        userDao.deleteUserFavorite(user)
    }

    suspend fun isFavorite(id: Int): Boolean = userDao.isFavorite(id)

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: FavoriteUserDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao)
            }.also { instance = it }
    }
}
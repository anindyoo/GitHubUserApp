package com.anindyo.githubuserapp.di

import android.content.Context
import com.anindyo.githubuserapp.db.UserRepository
import com.anindyo.githubuserapp.db.local.room.FavoriteUserDatabase
import com.anindyo.githubuserapp.db.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}
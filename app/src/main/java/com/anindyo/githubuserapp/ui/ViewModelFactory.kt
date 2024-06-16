package com.anindyo.githubuserapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anindyo.githubuserapp.db.UserRepository
import com.anindyo.githubuserapp.di.Injection
import com.anindyo.githubuserapp.ui.favorite.FavoriteViewModel
import com.anindyo.githubuserapp.ui.main.UserMainModel
import com.anindyo.githubuserapp.ui.userdetail.DetailUserViewModel
import com.anindyo.githubuserapp.ui.userdetail.followers.FollowersViewModel

class ViewModelFactory private constructor(private val newsRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserMainModel::class.java)) {
            return UserMainModel(newsRepository) as T
        }
        else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(newsRepository) as T
        }
        else if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(newsRepository) as T
        }
        else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
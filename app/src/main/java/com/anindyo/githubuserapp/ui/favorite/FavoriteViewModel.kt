package com.anindyo.githubuserapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anindyo.githubuserapp.db.UserRepository
import com.anindyo.githubuserapp.db.local.entity.User

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteList() : LiveData<List<User>> = userRepository.getFavoriteUsers()
}
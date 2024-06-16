package com.anindyo.githubuserapp.ui.userdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anindyo.githubuserapp.db.UserRepository
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.db.remote.response.DetailUserResponse
import com.anindyo.githubuserapp.db.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser : LiveData<DetailUserResponse> = _detailUser

    fun setUserDetail(username: String) {
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    suspend fun insertUserFavorite(user: User) {
        viewModelScope.launch {
            userRepository.insertUserFavorite(user)
        }
    }

    suspend fun deleteUserFavorite(user: User) {
        viewModelScope.launch {
            userRepository.deleteUserFavorite(user)
        }
    }

    suspend fun isFavorite(id: Int): Boolean = userRepository.isFavorite(id)

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}
package com.anindyo.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anindyo.githubuserapp.db.UserRepository
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.db.remote.response.UserResponse
import com.anindyo.githubuserapp.db.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserMainModel(private val userRepository: UserRepository) : ViewModel() {
    private val _listUsers = MutableLiveData<ArrayList<User>>()
    private val listUsers : LiveData<ArrayList<User>> = _listUsers

    fun setSearchUsers(query: String) {
        val client = ApiConfig.getApiService().getSearchUsers(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<UserResponse>,
                t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSearchUsers() : LiveData<ArrayList<User>> {
        return listUsers
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
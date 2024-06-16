package com.anindyo.githubuserapp.ui.userdetail.followers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anindyo.githubuserapp.db.UserRepository
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.db.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _followers = MutableLiveData<ArrayList<User>>()
    private val followers : LiveData<ArrayList<User>> = _followers

    private val _following = MutableLiveData<ArrayList<User>>()
    private val following : LiveData<ArrayList<User>> = _following

    fun setFollowersList(followers: String) {
        val client = ApiConfig.getApiService().getFollowers(followers)
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    _followers.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")

            }
        })
    }

    fun getFollowersList() : LiveData<ArrayList<User>> {
        return followers
    }

    fun setFollowingList(following: String) {
        val client = ApiConfig.getApiService().getFollowing(following)
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    _following.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")

            }
        })
    }

    fun getFollowingList() : LiveData<ArrayList<User>> {
        return following
    }


    companion object {
        private const val TAG = "FollowViewModel"
    }
}
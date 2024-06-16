package com.anindyo.githubuserapp.db.remote.retrofit

import com.anindyo.githubuserapp.BuildConfig
import com.anindyo.githubuserapp.db.remote.response.DetailUserResponse
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.db.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token $TOKEN_KEY")
    fun getSearchUsers(
        @Query("q") query : String
    ) : Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN_KEY")
    fun getUserDetail(
        @Path("username") username : String
    ) : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $TOKEN_KEY")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $TOKEN_KEY")
    fun getFollowing(
        @Path("username") username: String
    ) : Call<ArrayList<User>>

    companion object {
        const val TOKEN_KEY = BuildConfig.KEY
    }
}


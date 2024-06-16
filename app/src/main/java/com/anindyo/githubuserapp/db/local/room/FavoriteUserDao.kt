package com.anindyo.githubuserapp.db.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anindyo.githubuserapp.db.local.entity.User

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM user WHERE favorite = 1")
    fun getFavoriteUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserFavorite(user: User)

    @Update
    fun updateUserFavorite(user: User)

    @Delete
    suspend fun deleteUserFavorite(user: User)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND favorite = 1)")
    suspend fun isFavorite(id: Int): Boolean
}
package com.anindyo.githubuserapp.db.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class User(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    var id: Int,

    @field:ColumnInfo(name = "login")
    var login: String,

    @field:ColumnInfo(name = "avatarUrl")
    var avatar_url: String,

    @field:ColumnInfo(name = "htmlUrl")
    var html_url: String,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean
) : Parcelable

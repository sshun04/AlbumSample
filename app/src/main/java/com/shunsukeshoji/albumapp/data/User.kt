package com.shunsukeshoji.albumapp.data

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class User(
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "login") val name: String,
    @Json(name = "repos_url") val caption: String
) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.name == newItem.name
        }
    }
}
package com.shunsukeshoji.albumapp.data

import retrofit2.http.GET

interface GithubService {
    @GET("users")
    suspend fun getUser(): List<User>
}
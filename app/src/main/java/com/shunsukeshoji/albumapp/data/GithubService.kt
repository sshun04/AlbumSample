package com.shunsukeshoji.albumapp.data

import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.random.Random

interface GithubService {
    @GET("users")
    suspend fun getUser(@Query(value = "since") since:Int = Random.nextInt(1000000)): List<User>
}
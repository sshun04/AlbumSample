package com.shunsukeshoji.albumapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shunsukeshoji.albumapp.data.GithubService
import com.shunsukeshoji.albumapp.data.User
import com.shunsukeshoji.albumapp.fragment.GridFragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_CURRENT_POSITION = "key_current_position"
        var currentList: List<User> = emptyList()
        var currentPosition = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
            return
        }

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.github.com/")
            .build()

        val githubService = retrofit.create(GithubService::class.java)

        runBlocking(Dispatchers.IO) {
            runCatching {
                githubService.getUser()
            }.onSuccess {
                currentList = it
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, GridFragment(), GridFragment::class.simpleName)
                    .commit()
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, currentPosition)
    }
}

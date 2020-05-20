package com.shunsukeshoji.albumapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shunsukeshoji.albumapp.fragment.GridFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_CURRENT_POSITION = "key_current_position"
        var currentPosition = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION,0)
            return
        }


        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, GridFragment(), GridFragment::class.simpleName)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, currentPosition)
    }
}

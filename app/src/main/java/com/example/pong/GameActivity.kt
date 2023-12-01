package com.example.pong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.pong.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(){
    private lateinit var binder: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binder.root)


        supportFragmentManager.commit {
            add(R.id.frame_play, GameFragment())
        }

    }




}
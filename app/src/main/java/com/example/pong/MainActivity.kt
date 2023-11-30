package com.example.pong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong.databinding.ActivityMainBinding

//yes
class MainActivity : AppCompatActivity() {
    private lateinit var binder: ActivityMainBinding
    private lateinit var gameMode1: PongGameActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        setContentView(binder.root)
        val gameView = PongGameActivity(this)
        val container = binder.root
        container.addView(gameView)



    }

}


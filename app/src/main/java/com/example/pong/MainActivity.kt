package com.example.pong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binder: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binder.root)



        binder.button1.setOnClickListener() {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binder.button2.setOnClickListener {
            val intent = Intent(this, LeaderBordActivity::class.java)
            startActivity(intent)
        }

    }
}
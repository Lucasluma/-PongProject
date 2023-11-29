package com.example.pong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong.databinding.ActivityMainBinding

//yes
class MainActivity : AppCompatActivity() {
    private lateinit var binder : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)
    }
}
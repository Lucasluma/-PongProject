package com.example.pong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.example.pong.databinding.ActivityMainBinding

//yes
 class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding
    private lateinit var gameView: PongGameActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binder.root)
        val gameView = PongGameActivity(this)
        val container = binder.root
        container.addView(gameView)

       // binder.button1.setOnClickListener {
            // Initialize and add PongGameActivity when button1 is clicked
        //    gameView = PongGameActivity(this)
        //    val container = binder.root
         //   container.addView(gameView)

            // Start the game
           // gameView.toggleGameState()
       // }
    }
}





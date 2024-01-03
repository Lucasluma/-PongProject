package com.example.pong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.pong.databinding.ActivityGame2Binding

class Game2Activity : AppCompatActivity() {

        private lateinit var binder: ActivityGame2Binding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binder = ActivityGame2Binding.inflate(layoutInflater)
            setContentView(binder.root)


            supportFragmentManager.commit {
                add(R.id.frame_play2, Game2Fragment())
            }

        }




    }

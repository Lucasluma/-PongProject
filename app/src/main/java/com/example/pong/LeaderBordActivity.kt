package com.example.pong

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pong.databinding.ActivityLeaderBordBinding

class LeaderBordActivity : AppCompatActivity() {


    lateinit var binder : ActivityLeaderBordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityLeaderBordBinding.inflate(layoutInflater)
        setContentView(binder.root)


        val recyclerView = binder.lbRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)





       binder.btnLbM1.setOnClickListener {


           val sortedList = DataManager.sortPlayerList(DataManager.playerListM1)
           val adapter1 = RecyclerViewAdapter(sortedList)
           recyclerView.adapter = adapter1
       }


        binder.btnLbM2.setOnClickListener {


            val sortedList = DataManager.sortPlayerList(DataManager.playerListM2)
            val adapter1 = RecyclerViewAdapter(sortedList)
            recyclerView.adapter = adapter1

        }

        // Apply the animation to the ImageView
        val imageView = findViewById<ImageView>(R.id.flyin)
        val flyAnimation = AnimationUtils.loadAnimation(this, R.anim.flyin_ship)
        imageView.startAnimation(flyAnimation)
    }

}
package com.example.pong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
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


        //var isSorted =  DataManager.sortPlayerList()
        DataManager.sortPlayerList(DataManager.playerList)
        val adapter1 = RecyclerViewAdapter(DataManager.playerList)
        recyclerView.adapter = adapter1

    }
}
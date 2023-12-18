package com.example.pong

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(val playerList: List<Player> ): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(userView: View) : RecyclerView.ViewHolder(userView) {


        val namn: TextView = userView.findViewById(R.id.list_item_name)
        val score: TextView = userView.findViewById(R.id.list_item_score)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPlayer = playerList[position]
        val playerName = currentPlayer.name
        val PlayerPosition = position + 2
        holder.namn.text = "$PlayerPosition. $playerName"
        holder.score.text = currentPlayer.score.toString()
    }
}
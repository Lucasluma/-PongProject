package com.example.pong

object DataManager {

    var playerList = arrayListOf<Player>()

    fun createPlayer(name: String, score: Int){

        val player: Player = Player(name, score)
        playerList.add(player)
    }
}
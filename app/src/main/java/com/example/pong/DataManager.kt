package com.example.pong

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson

object DataManager {

    var wasUploaded = false
    var playerListM1 = arrayListOf<Player>()
    var playerListM2 = arrayListOf<Player>()



    fun createPlayer(name: String, score: Int, context: Context, mode: Int){

        val player: Player = Player(name, score)
        if (mode == 1) {
            playerListM1.add(player)

            val dbHelper = DatabaseHelper(context)
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put("object_data", convertObjectToJson(player))
            db.insert("objects", null, values)
            db.close()
        }

        else if( mode == 2){

            playerListM2.add(player)

            val dbHelper = DatabaseHelper(context)
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put("object_data", convertObjectToJson(player))
            db.insert("objects2", null, values)
            db.close()
        }



    }

    fun addPlayer(player: Player, mode: Int){

        if(mode == 1 ){
            playerListM1.add(player)
        }
        else if(mode == 2){
            playerListM2.add(player)
        }

    }


    fun convertObjectToJson( myObject: Player): String {
        return Gson().toJson(myObject)
    }

    fun convertJsonToObject(jsonString: String): Player {
        return Gson().fromJson(jsonString, Player::class.java)
    }

    fun sortPlayerList(playerListT: ArrayList<Player>): ArrayList<Player>{
        val range = playerListT.size - 2
        for (i in 0..range) {
            for (i in 0..range) {

                if (playerListT[i].score < playerListT[i + 1].score) {
                    val swapPlayer: Player = playerListT[i]
                    playerListT[i] = playerListT[i + 1]
                    playerListT[i + 1] = swapPlayer
                }
            }
        }
       return playerListT
    }


    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object {
            private const val DATABASE_NAME = "PongDataBase"
            private const val DATABASE_VERSION = 1
            const val TABLE_NAME = "objects"
            private const val TABLE_CREATE =
                "CREATE TABLE $TABLE_NAME (id INTEGER PRIMARY KEY AUTOINCREMENT, object_data TEXT);"
        }

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(TABLE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Handle database schema upgrades here
        }
    }

}
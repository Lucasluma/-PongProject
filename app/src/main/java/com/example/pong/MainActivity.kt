package com.example.pong

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binder: ActivityMainBinding


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binder.root)

        var firstRun = DataManager.wasUploaded
        if (!firstRun) {
            val context = applicationContext

            val dbHelper =DataManager.DatabaseHelper(context)
            val db = dbHelper.readableDatabase

            db.execSQL("CREATE TABLE IF NOT EXISTS objects (id INTEGER PRIMARY KEY AUTOINCREMENT, object_data TEXT)")

            val cursor = db.rawQuery("SELECT object_data FROM objects ORDER BY id ASC", null)

            while (cursor.moveToNext()) {
                val json = cursor.getString(cursor.getColumnIndex("object_data"))
                val player = DataManager.convertJsonToObject(json)
                DataManager.addPlayer(player, 1)
            }
            cursor.close()


            // begins here

            db.execSQL("CREATE TABLE IF NOT EXISTS objects2 (id INTEGER PRIMARY KEY AUTOINCREMENT, object_data TEXT)")

            val cursor2 = db.rawQuery("SELECT object_data FROM objects2 ORDER BY id ASC", null)

            while (cursor2.moveToNext()) {
                val json = cursor.getString(cursor.getColumnIndex("object_data"))
                val player = DataManager.convertJsonToObject(json)
                DataManager.addPlayer(player, 2)
            }
            cursor2.close()
            // ends Here

            DataManager.wasUploaded = true

        }



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
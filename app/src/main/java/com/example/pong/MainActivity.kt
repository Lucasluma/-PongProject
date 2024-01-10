package com.example.pong

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.pong.databinding.ActivityMainBinding
// Lucas, Ali och Abdulrahman

class MainActivity : AppCompatActivity() {


    private lateinit var binder: ActivityMainBinding
    private val background = (0)


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binder.root)

        SpinStar()


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
                val json = cursor2.getString(cursor2.getColumnIndex("object_data"))
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

        binder.button3.setOnClickListener() {
            val intent = Intent(this, Game2Activity::class.java)
            startActivity(intent)
        }


        val imgId = arrayOf(R.drawable.stars)

        binder.mainBack.setBackgroundResource(imgId[background])




    }
    fun SpinStar(){
        val spin = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)

        val spinImg = binder.twinkel

        spinImg.animation = spin
    }



    }

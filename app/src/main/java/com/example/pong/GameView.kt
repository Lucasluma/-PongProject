package com.example.pong

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.fragment.app.commit

class GameView(context: Context):SurfaceView(context), SurfaceHolder.Callback, Runnable{
    var thread: Thread? = null
    var running = false
    lateinit var canvas: Canvas
    var limit = Rect()
    var objects: ArrayList<Object> = ArrayList()
    var mHolder : SurfaceHolder? = holder
    var objectsCreated: Int = 0
    var score: Int = 0
    var bestScore: Int = 0
    var gameActivity = context as? GameActivity
    var stop = false
    var touchX: Float? = null
    var touchY: Float? = null

    private var background1: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.stars)
    var mutablebackground = background1.copy(Bitmap.Config.ARGB_8888, true)


    init {
        var playerList = DataManager.playerList
        if (!playerList.isEmpty()) {
            for (i in 0..playerList.size - 1) {
                if (playerList[i].score > bestScore)
                    bestScore = playerList[i].score
            }
        }
    }
    init {
        if (context is GameActivity) {
            gameActivity = context
        }
        if (mHolder != null){
            mHolder?.addCallback(this)
        }
        objects.add(PongBall(this, "PongBall", 300f, 200f, 0f,
            15f,50f,BitmapFactory.decodeResource(context.resources, R.drawable.astroid)))
        objects.add(Paddle(this, "PongBall", 300f, 1700f, 0f,
            0f,BitmapFactory.decodeResource(context.resources, R.drawable.paddel)))
    }

    private fun setup() {

    }

    fun start(){
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop(){
        running = false
        thread?.join()
    }

    fun update(){
        objects.forEach{
            it.update()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        touchX = event?.x
        touchY = event?.y

        return true
    }



    fun draw() {
        canvas = holder!!.lockCanvas()

        if (canvas != null) {
            canvas.drawBitmap(mutablebackground, matrix, null)
            objects.forEach {
                it.draw(canvas)
            }


            val textPaint = Paint().apply {
                textSize = 50f
                color = Color.YELLOW
            }
            // Draw the new score
            canvas.drawText("Score: $score", 100f, 100f, textPaint)
            canvas.drawText("Best Ever: $bestScore",  700f, 100f, textPaint)

            holder?.unlockCanvasAndPost(canvas)
        }
    }

    /*fun draw(){
        canvas = holder!!.lockCanvas()

        if(canvas != null) {
            canvas.drawBitmap(mutablebackground, matrix, null)
            objects.forEach {
                it.draw(canvas)
            }
            val canvas2 = Canvas(mutablebackground)
            val textPaint = Paint().apply {
                textSize = 50f }
            textPaint.color = Color.YELLOW
            canvas2.drawText("Score: $score", 100f, 100f, textPaint)

            holder!!.unlockCanvasAndPost(canvas)
        }
    }

     */

    fun saveScore(){
        gameActivity!!.supportFragmentManager.commit {

            val saveFragment = SaveFragment()
            var bundle = Bundle()
            bundle.putInt("Score", score)
            saveFragment.arguments = bundle
            replace(R.id.frame_play, saveFragment)
        }

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        limit = Rect(0, 0, width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while (running){
            if(!stop)
                update()
            draw()
        }
    }

}
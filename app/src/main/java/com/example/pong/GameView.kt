package com.example.pong

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context):SurfaceView(context), SurfaceHolder.Callback, Runnable{
    var thread: Thread? = null
    var running = false
    lateinit var canvas: Canvas
    var limit = Rect()
    var objects: ArrayList<Object> = ArrayList()
    var mHolder : SurfaceHolder? = holder
    var objectsCreated: Int = 0

    init {
        if (mHolder != null){
            mHolder?.addCallback(this)
        }
        objects.add(Rect(this, "Rect1", 310f, 0f, 0f, 4f,500f, 50f, Color.GREEN))
        objects.add(Rect(this, "Rect1", 300f, 500f, 0f, 0f,50f, 300f, Color.GREEN))
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

        return true
    }


    fun draw(){
        canvas = holder!!.lockCanvas()
        canvas.drawColor(Color.BLUE)
        objects.forEach{
            it.draw(canvas)
        }
        holder!!.unlockCanvasAndPost(canvas)
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
            update()
            draw()
        }
    }

}
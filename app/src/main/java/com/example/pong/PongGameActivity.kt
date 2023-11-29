package com.example.pong

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView

class PongGameActivity(context: Context) : SurfaceView(context),SurfaceHolder.Callback,Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var  canvas: Canvas
    private lateinit var paddle: Paddle
    private lateinit var ball: Ball
    var mHolder: SurfaceHolder? = holder

    init {
        if (mHolder != null){
            mHolder?.addCallback(this)
        }
    }
    private fun setup(){
        paddle = Paddle(this.context)
        ball = Ball(this.context)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun run() {
        TODO("Not yet implemented")
    }

}
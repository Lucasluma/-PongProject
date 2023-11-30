package com.example.pong

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView

class PongGameActivity(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private lateinit var paddle: Paddle
    private lateinit var ball: Ball
    private var bounds = Rect()
    private var mHolder: SurfaceHolder? = holder
    // paddel spökar lite med draw den går via background call just nu skall se över!
    private var background: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.paddel)
    private var background1: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.stars)

    init {
        if (mHolder != null) {
            mHolder!!.addCallback(this)
        }
        setup()
    }

    private fun setup() {
        paddle = Paddle(this.context)
        ball = Ball(this.context, 50f, 100f, 50f, 30f, 40f)
        //Starting position for ball and paddle
        ball.posY = 100f
        ball.posX = 500f
        paddle.posX = 500f

    }

    fun start() {
        running = true
        thread = Thread(this)
        thread?.start()
    }

    private fun stop() {
        running = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    fun toggleGameState() {
        if (running) {
            stop()
        } else {
            start()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    // Perform any initialization related to the surface creation here

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Perform any actions needed when the surface changes (e.g., resizing)
        bounds = Rect(0, 0, 0, 0)

        paddle.posY = bounds.bottom.toFloat() - 100f
        start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Perform cleanup when the surface is destroyed
        stop()
    }

    override fun run() {
        while (running) {
            // Lock the canvas before drawing
            canvas = mHolder!!.lockCanvas()

            // Draw on the canvas
            canvas.drawBitmap(background1, matrix, null)
            canvas.drawBitmap(background,matrix,null)
            paddle.draw(canvas)
            ball.draw(canvas)
            mHolder!!.unlockCanvasAndPost(canvas)

        }
    }
}

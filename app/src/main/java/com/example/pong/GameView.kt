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

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private lateinit var paddle: Paddle
    private lateinit var ball: Ball
    private var limit = Rect()
    private var mHolder: SurfaceHolder? = holder
    private var score = 0

    private var background1: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.stars)


    var mutablebackground = background1.copy(Bitmap.Config.ARGB_8888, true)
    var gameActivity = context as? GameActivity


    init {
        if (context is GameActivity) {
            gameActivity = context
        }

        setup()
    }

    init {
        if (mHolder != null) {
            mHolder!!.addCallback(this)
        }

    }

    // paddeln är ej låst i vertikalt läge måste lösas! Den löses genom att unvika ändra posy i onTouchEvent
    override fun onTouchEvent(event: MotionEvent?): Boolean {
       // paddle.posY = event!!.y
        paddle.posX = event!!.x

        //Sets the position of paddle to right of screen if paddle goes "outside" screen
        if (paddle.posX + paddle.width > limit.right) {
            paddle.posX = limit.right.toFloat() - paddle.width
            paddle.posY = limit.bottom.toFloat() - paddle.width // behövs ej när låser paddel i vertikalt läge

        }

        return true
    }


    private fun setup() {
        paddle = Paddle(this.context)
        ball = Ball(this.context, 50f, 100f, 50f, 30f, 10f)
        //Starting position for ball and paddle
        ball.posY = 100f
        ball.posX = 500f
        paddle.posX = 500f

        // arrayList<arraylist>(Abdul, 888)


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


    fun draw() {

        // Lock the canvas before drawing

        canvas = mHolder!!.lockCanvas()

        if (canvas != null) {
            // Draw on the canvas

            val canvas2 = Canvas(mutablebackground)
            val textPaint = Paint().apply {
                textSize = 50f }
            textPaint.color = Color.YELLOW
            canvas2.drawText("Score: $score", 100f, 100f, textPaint)

            canvas.drawBitmap(mutablebackground, matrix, null)
            paddle.draw(canvas)
            ball.draw(canvas)
            mHolder!!.unlockCanvasAndPost(canvas)
        }

    }

    fun update(){
        ball.seeCage(limit)
        ball.update()
    }

    fun saveScore(){

        gameActivity!!.supportFragmentManager.commit {

            val saveFragment = SaveFragment()
            var bundle = Bundle()
            bundle.putInt("Score", score)
            saveFragment.arguments = bundle
            replace(R.id.frame_play, saveFragment)
        }

    }

    fun youLose(){
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage("You lose \nYour score is: $score")
            .setTitle("Game over")
            .setCancelable(false)
            .setPositiveButton("Save Score "){dialog, _ ->

                saveScore()
                dialog.dismiss()

            }

            .setNegativeButton("ok"){dialog, _ ->

                ball.speedY = 10f
                dialog.dismiss()}



        val dialog = builder.create()
        dialog.show()

    }
    fun hasLost(){

        if (ball.posY + ball.size > limit.bottom){

            ball.speedY = 0f
            ball.posY = 100f
            youLose()

        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        // Perform any initialization related to the surface creation here
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        limit = Rect(0, 0, p2, p3)

        paddle.posY = limit.bottom.toFloat() - paddle.width       // paddle.posY = limit.bottom.toFloat() - 100f
        //what are line 154 and 156 for ?
        paddle.posX = limit.bottom.toFloat() - 100f







        // toggleGameState()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Perform cleanup when the surface is destroyed
        stop()
    }

    override fun run() {
        while (running) {



            val handler = Handler(Looper.getMainLooper())
            handler.post {
                // Perform UI operations here
                hasLost()
            }

            update()
            draw()

        }
    }
}
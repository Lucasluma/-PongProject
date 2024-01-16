package com.example.pong
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.graphics.scale
import androidx.fragment.app.commit

class GameView2(context: Context): SurfaceView(context), SurfaceHolder.Callback, Runnable{
    var thread: Thread? = null
    var running = false
    lateinit var canvas: Canvas
    var limit = Rect()
    var objects: ArrayList<Object> = ArrayList()
    var mHolder : SurfaceHolder? = holder
    var objectsCreated: Int = 0
    var score: Int = 0
    var bestScore: Int = 0
    var game2Activity = context as Game2Activity
    var stop = false
    var touchX: Float? = null
    var touchY: Float? = null
    val mode = 2
    val enemyDrawables = arrayOf(R.drawable.spacecargo1_1, R.drawable.spacecargo1_2,
                                  R.drawable.spacemine1_1,R.drawable.spacenuke1_1)
    val pongBallDrawables = arrayOf(R.drawable.ball3, R.drawable.ball3_1,R.drawable.bomb1)
    val paddleDrawables = arrayOf(R.drawable.beampaddle2, R.drawable.beampaddle2_1,R.drawable.saber1_2)


   // val randomEnemy = (0 until enemyDrawables.size).random()
    val randomPongBall = (0 until pongBallDrawables.size).random()
    val randomPaddle = (0 until paddleDrawables.size).random()
    private val random = (0..4).random()


    var idsToRemove: ArrayList<Int> = ArrayList()
    var objectsToAdd: ArrayList<Object> = ArrayList()

    private val imgId = arrayOf(
        R.drawable.planetjpg, R.drawable.cometcrash, R.drawable.planetearth, R.drawable.planet2,
        R.drawable.astronaut_spaceman_do_spacewalk

    )
    private var background1: Bitmap = BitmapFactory.decodeResource(resources, imgId[random])



   // private var background1: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.planetearth)
    var mutablebackground = background1.copy(Bitmap.Config.ARGB_8888, true)
       .scale(getScreenWidth(), getScreenHeight())

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }




    init {
        var playerList = DataManager.playerListM2
        if (!playerList.isEmpty()) {
            for (i in 0..playerList.size - 1) {
                if (playerList[i].score > bestScore)
                    bestScore = playerList[i].score
            }
        }
    }



    init {
        if (context is Game2Activity) {
            game2Activity = context
        }
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }


       objects.add(EnemyGenerator(this, 6000f, 3f,
                                150f, 300f, 75f))

        objects.add(
            PongBall2(
                this, "PongBall", 300f, 100f, 5f,
                14f, 50f, BitmapFactory.decodeResource(context.resources, pongBallDrawables[randomPongBall])
            )
        )
        objects.add(
            Paddle2(
                this, "Paddle", 300f,  2200f, 0f,
                0f, 400f, 75f, BitmapFactory.decodeResource(context.resources, paddleDrawables[randomPaddle])
            )
        )



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
        // Check if holder is null
        val currentHolder = holder ?: return

        // Lock the canvas
        canvas = currentHolder.lockCanvas() ?: return

        try {
            // Draw on the canvas
            canvas.drawBitmap(mutablebackground, matrix, null)
            objects.forEach {
                it.draw(canvas)
            }

            // Draw score
            val textPaint = Paint().apply {
                textSize = 50f
                color = Color.YELLOW
            }
            canvas.drawText("Score: $score", 100f, 100f, textPaint)
            canvas.drawText("Best Ever: $bestScore",  700f, 100f, textPaint)

        } finally {
            // Unlock the canvas in a final block to ensure it always happens
            currentHolder.unlockCanvasAndPost(canvas)
        }
    }


    fun saveScore(){
        game2Activity!!.supportFragmentManager.commit {

            val saveFragment = SaveFragment()
            var bundle = Bundle()
            bundle.putInt("Score", score)
            bundle.putInt("mode", mode)
            saveFragment.arguments = bundle
            replace(R.id.frame_play2, saveFragment)
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

            for(i in idsToRemove) {//eftersom det ställde till mycket problem med att remova objects blir det en safe metod här för att remova
                var posToRemove: Int = -1//börjar vid -1 eftersom första borde vara 0 och posToRemove++ händer varje gång
                var okToRemove: Boolean = false
                for(x in objects) {//på så sätt removar man object utan att ha problem med for satser innan som i update
                    posToRemove++
                   if(i == x.id) {
                        okToRemove = true
                        break
                    }
                }
                if(okToRemove)
                    objects.removeAt(posToRemove)
                if(idsToRemove.indexOf(i) == idsToRemove.count() -1) {
                    println(objects.count())
                    idsToRemove = ArrayList()
                }

            }
            objectsToAdd.forEach{//samma som rakt uppe händer det problem med for loopen i update när man försöker adda normalt så det får bli så
                objects.add(it)
                if(objectsToAdd.indexOf(it) == objectsToAdd.count() -1){
                    objectsToAdd = ArrayList()
                }
            }


        }
    }



}
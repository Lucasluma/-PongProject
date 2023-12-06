package com.example.pong

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

class Ball(context: Context, var posX: Float, var posY: Float, var size: Float, var speedX: Float,
           var speedY: Float) {

    var paint = Paint()

    lateinit var ballAsteroid: RectF




    // Bitmap for the ball
    var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.astroid) //R.drawable.asteroid

    fun update() {
        // Implement any update logic for the ball (e.g., movement)
        posY += speedY
        //posX += speedX
    }

    fun draw(canvas: Canvas?) {
        // Draw the bitmap on the canvas
        ballAsteroid = RectF(posX - size, posY - size, posX + size , posY + size) // what is this for ?
        canvas?.drawBitmap(bitmap, posX - size / 2, posY - size / 2, paint)
    }
    fun seeCage(limit: Rect){
        if(posX - size < 0){
            speedX *= -1

        }
        if(posX + size > limit.right){

            speedX *= -1

        }
        if(posY - size < 0){
            speedY *= -1

        }
        //MOVED THIS TO GAME VIEW TO BE ABLE TO STOP GAME LOOP
       /* if (posY + size > limit.bottom){
            speedY *= -1// this line was a comment



        }

        */
    }


}
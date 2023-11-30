package com.example.pong

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Paddle(context: Context) {
    var posX = 0f
    var posY = 0f
    var width = 400f
    var height = 300f
    var paint = Paint()

    private lateinit var paddle: RectF


    // Bitmap for paddle
    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.paddel)

    fun draw(canvas: Canvas?) {
        // Set the position and size of the rectangle
       // paddleRect.set(posX, posY, posX + width, posY + height)
        paddle = RectF(posX, posY, posX + width, posY + height)

        // Draw the bitmap on the canvas
        canvas?.drawBitmap(bitmap, null, paddle, paint )
    }
}

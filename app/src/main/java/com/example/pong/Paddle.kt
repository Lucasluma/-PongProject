package com.example.pong

import android.content.Context
import android.graphics.Paint

class Paddle(context: Context) {
    var posX = 0f
    var posY = 0f
    var paint = Paint()
    var size = 0f
    var speed = 0f
}
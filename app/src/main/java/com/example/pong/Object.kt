package com.example.pong

import android.graphics.Canvas

abstract class Object {
    abstract var tag:String
    abstract var name:String
    abstract var posX: Float
    abstract var posY: Float
    abstract var size: Float
    abstract var sizeY: Float
    abstract var sizeX: Float
    abstract var speedY: Float
    abstract var speedX: Float
    abstract var stillObject: Boolean

    abstract var id:Int
    abstract fun update()
    abstract fun draw(canvas: Canvas)
}
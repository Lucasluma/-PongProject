package com.example.pong

import kotlin.math.abs
import kotlin.math.atan

fun pointToDegrees(aPosX: Float, aPosY:Float):Float {
    if (aPosX < 0) {
        return atan(aPosY / aPosX) + 180f
    } else return abs(atan(aPosX / aPosY))
}

fun trueDistance(mainPos: Float, aPos: Float): Float {
    if(aPos > mainPos)
        return -abs(mainPos - aPos)
    else
        return  abs(mainPos - aPos)
}
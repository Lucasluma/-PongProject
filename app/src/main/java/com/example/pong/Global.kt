package com.example.pong

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan

fun pointToDegrees(aPosX: Float, aPosY:Float):Float{
    if(aPosX< 0) {//genom tester i kalkylatorn jag kom till att det är så man räknar vinkeln i bara plusgrader genom tangens
        return ((atan(aPosY/aPosX) + PI.toFloat())/(2f* PI.toFloat()))*360f
    }
    else if((atan(aPosY/aPosX)/(2f* PI.toFloat()))*360f < 0)
        return 360f + (atan(aPosY/aPosX)/(2f* PI.toFloat()))*360f
    else
        return (atan(aPosY/aPosX)/(2f* PI.toFloat()))*360f
}
fun trueDistance(mainPos: Float, aPos: Float): Float {//distans mellan 2 nummer
    if(aPos >= mainPos)//om nummret man räknar distanset är större så blire det "distans"
        return abs(mainPos - aPos)
    else//om nummret man räknar distanset är mindere så blire det "-distans"
        return  -abs(mainPos - aPos)
}

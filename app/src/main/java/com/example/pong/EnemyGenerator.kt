package com.example.pong

import android.graphics.Canvas

class EnemyGenerator(aGameView: GameView): Object(){
    override val tag: String = "Still"
    override var name: String = ""
    override var posX: Float = 0f
    override var posY: Float = 0f
    override var size: Float = 0f
    override var sizeY: Float = 0f
    override var sizeX: Float = 0f
    override var speedY: Float = 0f
    override var speedX: Float = 0f
    override var stillObject: Boolean = true
    var gameView: GameView
    override var id: Int

    init {
        gameView = aGameView
        id = aGameView.objectsCreated +1 //varje objekt har en speciell id så att kollisions kan fungera
        aGameView.objectsCreated++ //objectscreated hjälper med att objekt ska ha unika ids

    }
    override fun update() {
        TODO("Not yet implemented")
    }

    override fun draw(canvas: Canvas) {
        TODO("Not yet implemented")
    }
}
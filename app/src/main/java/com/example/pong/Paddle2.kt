package com.example.pong

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Paddle2(aGameView: GameView2):Object() {
    override var name: String = ""
    override var tag: String = "Rect"
    override var posX = 0f
    override var posY = 0f
    override var id: Int
    override var size = 10f
    override var sizeX = 0f
    override var sizeY = 0f
    override var speedX = 0f
    override var speedY = 0f
    lateinit var bitmap: Bitmap
    var isBitmap: Boolean = false
    override var stillObject: Boolean = false
    private var paint = Paint()
    private var gameView: GameView2

    private var inCollisionObjects: ArrayList<Object> = ArrayList()
    var idsToRemove: ArrayList<Int> = ArrayList()


    init {
        gameView = aGameView
        id = aGameView.objectsCreated +1
        aGameView.objectsCreated++
    }

    constructor(aGameView: GameView2, aName: String) : this(aGameView){
        name = aName
        gameView = aGameView
    }
    constructor(aGameView: GameView2, aName: String, aPosX: Float, aPosY: Float, aSpeedX: Float, aSpeedY: Float, aSizeX: Float, aSizeY: Float, color: Int) : this(aGameView){
        name = aName
        posX = aPosX
        posY = aPosY
        speedX = aSpeedX
        sizeX = aSizeX
        sizeY = aSizeY
        speedY = aSpeedY
        paint.color = color
        gameView = aGameView
    }
    constructor(aGameView: GameView2, aName: String, aPosX: Float, aPosY: Float, aSpeedX: Float, aSpeedY: Float,aSizeX: Float,aSizeY: Float, aBitmap: Bitmap) : this(aGameView){
        name = aName
        posX = aPosX
        posY = aPosY
        speedX = aSpeedX
        if(aSizeX == 0f)
            sizeX = aBitmap.width.toFloat()
        else
            sizeX = aSizeX
        if(aSizeY == 0f)
            sizeY = aBitmap.height.toFloat()
        else
            sizeY = aSizeY
        speedY = aSpeedY
        bitmap = aBitmap
        isBitmap = true
        gameView = aGameView


    }





    override fun update(){
        if(!stillObject) {
            if(gameView.touchX != null)
                posX = gameView.touchX!! - sizeX/2
            //Sets the position of paddle to right of screen if paddle goes "outside" screen
            detectExistCollision()
            detectCollision()
            detectBorderCollision()
        }
    }

    override fun start() {

    }

    private fun onCollision(collision: Object, collisionPosX: Float, collisionPosY: Float) {
        if(collision.tag.contains("Enemy")) {
            gameView.lives--
            inCollisionObjects.remove(collision)
            collision.tag = "remove"//på så sätt försvinner den på ett säkert sätt medans den removas på ett säkert sätt från listan
            gameView.idsToRemove.add(collision.id)//så att den removas senare
        }
    }
    private fun onExitCollision(collision: Object) {//när ett object som kolliderade innan går ut och slutar kollidera
    }
    private fun detectBorderCollision() {
        if (posX <= 0) {//Left
            posX = 0f
        }
        if (posX + sizeX >= gameView.limit.right) {//Right
            posX = gameView.limit.right - sizeX
        }
        if (posY <= 0) {//Top

        }
        if (posY + sizeY >= gameView.limit.bottom) {//Bottom

        }
    }
    override fun draw(canvas: Canvas) {
        if(!isBitmap)
            canvas?.drawRect(posX, posY, posX + sizeX, posY + sizeY, paint)
        else {
            val aRect = RectF(posX, posY, posX + sizeX , posY + sizeY)//existerar för att kunna ändra storleken på objectet med frihet genom sizeX och sizeY
            canvas.drawBitmap(bitmap, null, aRect, paint)
        }
    }

    private fun detectCollision(){
        for (it in gameView.objects){
            var skip: Boolean = false
            if(inCollisionObjects.isNotEmpty()) {
                for (i in inCollisionObjects) {
                    if (it.id == i.id) {
                        skip = true
                        break
                    }
                }
                if (skip) {
                    continue
                }
            }
            if(it.id == id){

            }
            else if(it.tag.contains("Rect")) {
                if(it.posX >= posX && it.posX <=(posX + sizeX) && it.posY >= posY && it.posY <=(posY + sizeY)) {//om uppe-vänsta hörnan kolliderar med rektangeln
                    if((it.posX + it.sizeX)>= posX && (it.posX + it.sizeX) <=(posX + sizeX) && it.posY >= posY && it.posY <=(posY + sizeY)) {//om uppe-högra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, it.posX + it.sizeX/2f, it.posY)
                    }
                    else if(it.posX >= posX && it.posX <=(posX + sizeX) && (it.posY + it.sizeY) >= posY && (it.posY + it.sizeY) <=(posY + sizeY)) {//om nedre-vänstra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, it.posX , it.posY + it.sizeY/2)
                    }
                    else {
                        inCollisionObjects.add(it)
                        onCollision(it, it.posX, it.posY)
                    }
                }
                else if((it.posX + it.sizeX)>= posX && (it.posX + it.sizeX) <=(posX + sizeX) && it.posY >= posY && it.posY <=(posY + sizeY)) {//om uppe-högra hörnan kolliderar med rektangeln
                    if((it.posX + it.sizeX) >= posX && (it.posX + it.sizeX) <=(posX + sizeX) && (it.posY + it.sizeY)>= posY && (it.posY + it.sizeY) <=(posY + sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, it.posX + it.sizeX , it.posY + it.sizeY/2)
                    }
                    else {
                        inCollisionObjects.add(it)
                        onCollision(it, (it.posX + it.sizeX), it.posY)

                    }
                }
                else if(it.posX >= posX && it.posX <=(posX + sizeX) && (it.posY + it.sizeY) >= posY && (it.posY + it.sizeY) <=(posY + sizeY)) {//om nedre-vänstra hörnan kolliderar med rektangeln
                    if((it.posX + it.sizeX) >= posX && (it.posX + it.sizeX) <=(posX + sizeX) && (it.posY + it.sizeY)>= posY && (it.posY + it.sizeY) <=(posY + sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, it.posX + it.sizeX/2f, it.posY + it.sizeY)
                    }
                    else {
                        inCollisionObjects.add(it)
                        onCollision(it, it.posX, it.posY + it.sizeY)
                    }
                }
                else if((it.posX + it.sizeX) >= posX && (it.posX + it.sizeX) <=(posX + sizeX) && (it.posY + it.sizeY)>= posY && (it.posY + it.sizeY) <=(posY + sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX , it.posY + it.sizeY)
                }
                ////
                else if(posX >= it.posX && posX <=(it.posX + it.sizeX) && posY >= it.posY && posY <=(it.posY + it.sizeY)) {//om uppe-vänsta hörnan kolliderar med rektangeln
                    if((posX + sizeX)>= it.posX && (posX + sizeX) <=(it.posX + it.sizeX) && posY >= it.posY && posY <=(it.posY + it.sizeY)) {//om uppe-högra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, posX + sizeX/2f, posY)
                    }
                    else if(posX >= it.posX && posX <=(it.posX + it.sizeX) && (posY + sizeY) >= it.posY && (posY + sizeY) <=(it.posY + it.sizeY)) {//om nedre-vänstra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, posX , posY + sizeY/2)
                    }
                    else {
                        inCollisionObjects.add(it)
                        onCollision(it, posX, posY)
                    }
                }
                else if((posX + sizeX)>= it.posX && (posX + sizeX) <=(it.posX + it.sizeX) && posY >= it.posY && posY <=(it.posY + it.sizeY)) {//om uppe-högra hörnan kolliderar med rektangeln
                    if((posX + sizeX) >= it.posX && (posX + sizeX) <=(it.posX + it.sizeX) && (posY + sizeY)>= it.posY && (posY + sizeY) <=(it.posY + it.sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, posX + sizeX , posY + sizeY/2)
                    }////
                    else {
                        inCollisionObjects.add(it)
                        onCollision(it, (posX + sizeX), posY)

                    }
                }
                else if(posX >= it.posX && posX <=(it.posX + it.sizeX) && (posY + sizeY) >= it.posY && (posY + sizeY) <=(it.posY + it.sizeY)) {//om nedre-vänstra hörnan kolliderar med rektangeln
                    if((posX + sizeX) >= it.posX && (posX + sizeX) <=(it.posX + it.sizeX) && (posY + sizeY)>= it.posY && (posY + sizeY) <=(it.posY + it.sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                        inCollisionObjects.add(it)
                        onCollision(it, posX + sizeX/2f, posY + sizeY)
                    }
                    else {
                        inCollisionObjects.add(it)
                        onCollision(it, posX, posY + sizeY)
                    }
                }
                else if((posX + sizeX) >= it.posX && (posX + sizeX) <=(it.posX + it.sizeX) && (posY + sizeY)>= it.posY && (posY + sizeY) <=(it.posY + it.sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX , posY + sizeY)
                }
                else if(sizeY > it.sizeY && sizeX < it.sizeX && it.posY > posY && it.posY + it.sizeY > posY &&// om de är i varandra med de har inga av deras punkter i varandra
                    it.posY < posY + sizeY && it.posY + it.sizeY < posY + sizeY
                    && posX > it.posX && posX + sizeX > it.posX &&
                    posX < it.posX + it.sizeX && posX + sizeX < it.posX + it.sizeX)
                {
                    inCollisionObjects.add(it)
                    onCollision(it, posX + sizeX/2, it.posY + it.sizeY/2)
                }
                else if(it.sizeY > sizeY && it.sizeX < sizeX && posY > it.posY && posY + sizeY > it.posY &&
                    posY < it.posY + it.sizeY && posY + sizeY < it.posY + it.sizeY
                    && it.posX > posX && it.posX + it.sizeX > posX &&
                    it.posX < posX + sizeX && it.posX + it.sizeX < posX + sizeX)
                {
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX + it.sizeX/2, posY + sizeY/2)
                }

            }
            else if(it.tag.contains("Ball")) {
                if(sqrt((it.posX - posX).pow(2) + (it.posY - posY).pow(2)) <= it.size) {
                    inCollisionObjects.add(it)
                    onCollision(it, posX, posY)
                }
                else if(sqrt((it.posX - posX).pow(2) + (it.posY - (posY + sizeY)).pow(2)) <= it.size){
                    inCollisionObjects.add(it)
                    onCollision(it, posX, posY + sizeY)
                }
                else if(sqrt((it.posX - (posX + sizeX)).pow(2) + (it.posY - posY).pow(2)) <= it.size){
                    inCollisionObjects.add(it)
                    onCollision(it, posX + sizeX, posY)
                }
                else if(sqrt((it.posX - (posX + sizeX)).pow(2) + (it.posY - (posY + sizeY)).pow(2)) <= it.size){
                    inCollisionObjects.add(it)
                    onCollision(it, posX + sizeX, posX + sizeX)
                }

                else if(abs(it.posY-posY) <= it.size && it.posX > posX && it.posX <(posX + sizeX)){//träffar rektangeln uppefrån
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX, posY)
                }
                else if(abs(it.posY-(posY +sizeY)) <= it.size && it.posX > posX && it.posX <(posX + sizeX)){//träffar rektangeln underfrån
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX, posY + sizeY)
                }
                else if(abs(it.posX-posX) <= it.size && it.posY > posY && it.posY <(posY + sizeY)){//träffar rektangeln vänter infrån
                    inCollisionObjects.add(it)
                    onCollision(it, posX, it.posY)
                }
                else if(abs(it.posX-(posX + sizeX)) <= it.size && it.posY > posY && it.posY <(posY + sizeY)){//träffar rektangeln höger infrån
                    inCollisionObjects.add(it)
                    onCollision(it, posX + sizeX, it.posY)
                }
            }
        }
    }
    fun detectExistCollision(){
        for(it in inCollisionObjects) {
            if(idsToRemove.contains(it.id)) {
                idsToRemove.add(it.id)
            }
            if (it.tag.contains("Rect")) {
                if(it.posX >= posX && it.posX <=(posX + sizeX) && it.posY >= posY && it.posY <=(posY + sizeY)) {
                    continue
                }
                else if((it.posX + it.sizeX)>= posX && (it.posX + it.sizeX) <=(posX + sizeX) && it.posY >= posY && it.posY <=(posY + sizeY)) {//om uppe-högra hörnan kolliderar med rektangeln
                    continue
                }
                else if(it.posX >= posX && it.posX <=(posX + sizeX) && (it.posY + it.sizeY) >= posY && (it.posY + it.sizeY) <=(posY + sizeY)) {//om nedre-vänstra hörnan kolliderar med rektangeln
                    continue
                }
                else if((it.posX + it.sizeX) >= posX && (it.posX + it.sizeX) <=(posX + sizeX) && (it.posY + it.sizeY)>= posY && (it.posY + it.sizeY) <=(posY + sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                    continue
                }
                ////
                else if(posX >= it.posX && posX <=(it.posX + it.sizeX) && posY >= it.posY && posY <=(it.posY + it.sizeY)) {//om uppe-vänsta hörnan kolliderar med rektangeln
                    continue
                }
                else if((posX + sizeX)>= it.posX && (posX + sizeX) <=(it.posX + it.sizeX) && posY >= it.posY && posY <=(it.posY + it.sizeY)) {//om uppe-högra hörnan kolliderar med rektangeln
                    continue
                }
                else if(posX >= it.posX && posX <=(it.posX + it.sizeX) && (posY + sizeY) >= it.posY && (posY + sizeY) <=(it.posY + it.sizeY)) {//om nedre-vänstra hörnan kolliderar med rektangeln
                    continue
                }
                else if((posX + sizeX) >= it.posX && (posX + sizeX) <=(it.posX + it.sizeX) && (posY + sizeY)>= it.posY && (posY + sizeY) <=(it.posY + it.sizeY)) {//om nedre-högra hörnan kolliderar med rektangeln
                    continue
                }
                else if(sizeY > it.sizeY && sizeX < it.sizeX && it.posY > posY && it.posY + it.sizeY > posY &&// om de är i varandra med de har inga av deras punkter i varandra
                    it.posY < posY + sizeY && it.posY + it.sizeY < posY + sizeY
                    && posX > it.posX && posX + sizeX > it.posX &&
                    posX < it.posX + it.sizeX && posX + sizeX < it.posX + it.sizeX)
                {
                    continue
                }
                else if(it.sizeY > sizeY && it.sizeX < sizeX && posY > it.posY && posY + sizeY > it.posY &&
                    posY < it.posY + it.sizeY && posY + sizeY < it.posY + it.sizeY
                    && it.posX > posX && it.posX + it.sizeX > posX &&
                    it.posX < posX + sizeX && it.posX + it.sizeX < posX + sizeX)
                {
                    continue
                }
                else {
                    idsToRemove.add(it.id)
                    onExitCollision(it)
                }


            }
            else if(it.tag.contains("Ball")){

                if(sqrt((it.posX - posX).pow(2) + (it.posY - posY).pow(2)) <= it.size) {
                    continue
                }
                else if(sqrt((it.posX - posX).pow(2) + (it.posY - (posY + sizeY)).pow(2)) <= it.size){
                    continue
                }
                else if(sqrt((it.posX - (posX + sizeX)).pow(2) + (it.posY - posY).pow(2)) <= it.size){
                    continue
                }
                else if(sqrt((it.posX - (posX + sizeX)).pow(2) + (it.posY - (posY + sizeY)).pow(2)) <= it.size){
                    continue
                }

                else if(abs(it.posY-posY) <= it.size && it.posX > posX && it.posX <(posX + sizeX)){//träffar rektangeln uppefrån
                    continue
                }
                else if(abs(it.posY-(posY +sizeY)) <= it.size && it.posX > posX && it.posX <(posX + sizeX)){//träffar rektangeln underfrån
                    continue
                }
                else if(abs(it.posX-posX) <= it.size && it.posY > posY && it.posY <(posY + sizeY)){//träffar rektangeln vänter infrån
                    continue
                }
                else if(abs(it.posX-(posX + sizeX)) <= it.size && it.posY > posY && it.posY <(posY + sizeY)){//träffar rektangeln höger infrån
                    continue
                }
                else if(it.posX >= posX && it.posX <=(posX + sizeX) && it.posY >= posY && it.posY <=(posY + sizeY)) {
                    continue
                }
                else {
                    idsToRemove.add(it.id)
                    onExitCollision(it)
                }
            }
        }
        for(i in idsToRemove) {//eftersom det ställde till mycket problem med att remova objects blir det en safe metod här för att remova
            var posToRemove: Int = -1//börjar vid -1 eftersom första borde vara 0 och posToRemove++ händer varje gång
            var okToRemove: Boolean = false
            for(x in inCollisionObjects) {//på så sätt removar man object utan att ha problem med for satser innan som i update
                posToRemove++
                if(i == x.id) {
                    okToRemove = true
                    break
                }
            }
            if(okToRemove)
                inCollisionObjects.removeAt(posToRemove)
            //if(idsToRemove.indexOf(i) == idsToRemove.count() -1) {
            //    idsToRemove = ArrayList()
            //}
        }
        idsToRemove = ArrayList()

    }

}
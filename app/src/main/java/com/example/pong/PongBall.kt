package com.example.pong

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Looper
import java.util.logging.Handler
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class PongBall(aGameView: GameView):Object() {
    override var name: String = ""
    override val tag: String = "Ball"
    override var posX = 0f
    override var posY = 0f
    override var id: Int //varje objekt har en speciell id så att kollisions kan fungera
    var paint = Paint()
    override var size = 10f
    override var sizeX = 0f
    override var sizeY = 0f
    override var speedX = 0f
    override var speedY = 0f

    lateinit var bitmap: Bitmap
    var isBitmap: Boolean = false
    override var stillObject: Boolean = false

    var gameView: GameView
    var inCollisionObjects: ArrayList<Object> = ArrayList()

    init {
        gameView = aGameView
        id = aGameView.objectsCreated +1 //varje objekt har en speciell id så att kollisions kan fungera
        aGameView.objectsCreated++ //objectscreated hjälper med att objekt ska ha unika ids
    }
    constructor(aGameView: GameView, aName: String) : this(aGameView){
        name = aName
        gameView = aGameView
    }
    constructor(aGameView: GameView, aName: String, aPosX: Float, aPosY: Float, aSpeedX: Float, aSpeedY: Float, aSize: Float, color: Int) : this(aGameView){
        name = aName
        posX = aPosX
        posY = aPosY
        speedX = aSpeedX
        size = aSize
        speedY = aSpeedY
        paint.color = color
        gameView = aGameView
    }
    constructor(aGameView: GameView, aName: String, aPosX: Float, aPosY: Float, aSpeedX: Float, aSpeedY: Float,aSize: Float, aBitmap: Bitmap) : this(aGameView){
        name = aName
        posX = aPosX
        posY = aPosY
        speedX = aSpeedX
        size = aSize
        speedY = aSpeedY
        bitmap = aBitmap
        isBitmap = true
        gameView = aGameView
    }

    override fun update(){
        if(!stillObject) {
            posY += speedY
            posX += speedX
            detectCollision()
            detectExistCollision()
            detectBorderCollision()
        }

    }
    private fun onCollision(collision: Object, collisionPosX: Float, collisionPosY: Float) {//när ett object kolliderar
        if(collision.tag.contains("Ball") || collision.tag.contains("Rect")) {
            var collisionAngle: Float = pointToDegrees(trueDistance(posX, collisionPosX), trueDistance(posY,collisionPosY))
            var speedAngle: Float = pointToDegrees(speedX, speedY)
            var diagonalSpeed: Float = sqrt((speedX).pow(2) + (speedY).pow(2))
            //angleDifference =  180 - (angleDifference- 180) //angle difference when it's bigger than 180
            var nextSpeedAngle: Float
            val angleDifference: Float = abs(collisionAngle - speedAngle)

            println("angleDif = $angleDifference  collisionAng = $collisionAngle  speedAng = $speedAngle")
            println("collisionX = $collisionPosX  collisionY = $collisionPosY  posX = $posX  posY = $posY")

            if(angleDifference >= 90 || collisionAngle == speedAngle) {
                nextSpeedAngle = collisionAngle +180
                if(nextSpeedAngle > 360)
                    nextSpeedAngle -= 360
            }
            else if(collisionAngle - 90 <0) {
                if(speedAngle < collisionAngle || speedAngle > 360 - (collisionAngle - 90)) {
                    nextSpeedAngle = speedAngle - 180f *(1f- angleDifference/90f)
                    if(nextSpeedAngle < 0)
                        nextSpeedAngle = 360 - abs(nextSpeedAngle)
                }
                else {
                    nextSpeedAngle = speedAngle + 180f *(1f- angleDifference/90f)
                }
            }
            else if(collisionAngle + 90 > 360) {
                if(speedAngle > collisionAngle || speedAngle < (collisionAngle + 90) - 360) {
                    nextSpeedAngle = speedAngle +180f *(1f- angleDifference/90f)
                    if(nextSpeedAngle > 360)
                        nextSpeedAngle -= 360
                }
                else {
                    nextSpeedAngle = speedAngle - 180f *(1f- angleDifference/90f)
                }
            }
            else {
                if(speedAngle > collisionAngle)
                    nextSpeedAngle = speedAngle + 180f *(1f- angleDifference/90f)
                else
                    nextSpeedAngle = speedAngle - 180f *(1f- angleDifference/90f)
            }
            speedX = cos(nextSpeedAngle) * diagonalSpeed
            speedY = sin(nextSpeedAngle) * diagonalSpeed
        }
        if(collision.tag.contains("Enemy")) {
            gameView.objects.remove(collision)
        }
    }
    private fun onExitCollision(collision: Object) {//när ett object som kolliderade innan går ut och slutar kollidera
    }
    private fun detectBorderCollision() {
        if (posX - size <= 0) {//Left

        }
        if (posX + size > gameView.limit.right) {//Right

        }
        if (posY - size <= 0) {//Top

        }
        if (posY + size > gameView.limit.bottom) {//Bottom
            val handler = android.os.Handler(Looper.getMainLooper())
            posY = 100f
            handler.post {
                val builder = AlertDialog.Builder(gameView.context)
                builder.setMessage("You lose \nYour score is: ${gameView.score}")
                    .setTitle("Game over")
                    .setCancelable(false)
                    .setPositiveButton("Save Score "){dialog, _ ->

                        gameView.saveScore()
                        dialog.dismiss()

                    }

                    .setNegativeButton("ok"){dialog, _ ->
                        dialog.dismiss()
                        gameView.stop = false
                    }



                val dialog = builder.create()
                dialog.show()

            }
            gameView.stop = true
        }
    }

    override fun draw(canvas: Canvas) {
        if(!isBitmap)
            canvas?.drawCircle(posX,posY, size, paint)
        else {
            val aRect = RectF(posX - size, posY - size, posX + size , posY + size) // what is this for ?
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
            else if(it.tag.contains("Ball")) {
                if(sqrt((posX - it.posX).pow(2) + (posY - it.posY).pow(2)) <= size+it.size) {

                    inCollisionObjects.add(it)
                    var tempPosX: Float
                    var tempPosY: Float

                    var sizeRatio: Float = (size / it.size +1)/(size / it.size)// p.g.a storlekerna på bollarna kan vara olika så kommer kollision punkten vara olika nära till bollarna beroende av deras storlekar i förhållande till varandra
                    if(posX > it.posX)
                        tempPosX = posX - (abs(posX - it.posX) /sizeRatio)
                    else if(posX < it.posX)
                        tempPosX = posX + (abs(posX - it.posX) /sizeRatio)
                    else
                        tempPosX = posX

                    if(posY > it.posY)
                        tempPosY = posY - (abs(posY - it.posY) /sizeRatio)
                    else if(posY < it.posY)
                        tempPosY = posY + (abs(posY - it.posY) /sizeRatio)
                    else
                        tempPosY = posY


                    onCollision(it, tempPosX, tempPosY)
                }
            }
            else if(it.tag.contains("Rect")) {
                if(sqrt((posX - it.posX).pow(2) + (posY - it.posY).pow(2)) <= size) {
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX, it.posY)
                }
                else if(sqrt((posX - it.posX).pow(2) + (posY - (it.posY + it.sizeY)).pow(2)) <= size){
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX, it.posY + it.sizeY)
                }
                else if(sqrt((posX - (it.posX + it.sizeX)).pow(2) + (posY - it.posY).pow(2)) <= size){
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX + it.sizeX, it.posY)
                }
                else if(sqrt((posX - (it.posX + it.sizeX)).pow(2) + (posY - (it.posX + it.sizeX)).pow(2)) <= size){
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX + it.sizeX, it.posX + it.sizeX)
                }

                else if(abs(posY-it.posY) <= size && posX > it.posX && posX <(it.posX + it.sizeX)){//träffar rektangeln uppefrån
                    inCollisionObjects.add(it)
                    onCollision(it, posX, it.posY)
                }
                else if(abs(posY-(it.posY +it.sizeY)) <= size && posX > it.posX && posX <(it.posX + it.sizeX)){//träffar rektangeln underfrån
                    inCollisionObjects.add(it)
                    onCollision(it, posX, it.posY + it.sizeY)
                }
                else if(abs(posX-it.posX) <= size && posY > it.posY && posY <(it.posY + it.sizeY)){//träffar rektangeln vänter infrån
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX, posY)
                }
                else if(abs(posX-(it.posX + it.sizeX)) <= size && posY > it.posY && posY <(it.posY + it.sizeY)){//träffar rektangeln höger infrån
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX + it.sizeX, posY)
                }
            }
        }
    }
    private fun detectExistCollision(){
        for(it in inCollisionObjects) {
            if (it.tag.contains("Ball")) {
                if (sqrt((posX - it.posX).pow(2) + (posY - it.posY).pow(2)) > size + it.size) {
                    inCollisionObjects.remove(it)
                    onExitCollision(it)
                }
            }
            else if(it.tag.contains("Rect")){

                if(sqrt((posX - it.posX).pow(2) + (posY - it.posY).pow(2)) <= size) {
                    continue
                }
                else if(sqrt((posX - it.posX).pow(2) + (posY - (it.posY + it.sizeY)).pow(2)) <= size){
                    continue
                }
                else if(sqrt((posX - (it.posX + it.sizeX)).pow(2) + (posY - it.posY).pow(2)) <= size){
                    continue
                }
                else if(sqrt((posX - (it.posX + it.sizeX)).pow(2) + (posY - (it.posX + it.sizeX)).pow(2)) <= size){
                    continue
                }

                else if(abs(posY-it.posY) <= size && posX > it.posX && posX <(it.posX + it.sizeX)){//träffar rektangeln uppefrån
                    continue
                }
                else if(abs(posY-(it.posY +it.sizeY)) <= size && posX > it.posX && posX <(it.posX + it.sizeX)){//träffar rektangeln underfrån
                    continue
                }
                else if(abs(posX-it.posX) <= size && posY > it.posY && posY <(it.posY + it.sizeY)){//träffar rektangeln vänter infrån
                    continue
                }
                else if(abs(posX-(it.posX + it.sizeX)) <= size && posY > it.posY && posY <(it.posY + it.sizeY)){//träffar rektangeln höger infrån
                    continue
                }
                else if(posX > it.posX && posX <(it.posX + it.sizeX) && posY > it.posY && posY <(it.posY + it.sizeY)) {
                    continue
                }
                else {
                    inCollisionObjects.remove(it)
                    onExitCollision(it)
                }


            }
        }
    }
}
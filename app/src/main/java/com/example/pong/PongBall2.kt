package com.example.pong

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Looper
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class PongBall2(aGameView: GameView2):Object() {
    override var name: String = ""
    override var tag: String = "Ball"
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
    var difficultyIncreaseThreshold: Int = 0
    var gameView: GameView2
    var inCollisionObjects: ArrayList<Object> = ArrayList()

    init {
        gameView = aGameView
        id = aGameView.objectsCreated +1 //varje objekt har en speciell id så att kollisions kan fungera
        aGameView.objectsCreated++ //objectscreated hjälper med att objekt ska ha unika ids
    }
    constructor(aGameView: GameView2, aName: String) : this(aGameView){
        name = aName
        gameView = aGameView
    }
    constructor(aGameView: GameView2, aName: String, aPosX: Float, aPosY: Float, aSpeedX: Float, aSpeedY: Float, aSize: Float, color: Int) : this(aGameView){
        name = aName
        posX = aPosX
        posY = aPosY
        speedX = aSpeedX
        size = aSize
        speedY = aSpeedY
        paint.color = color
        gameView = aGameView
    }
    constructor(aGameView: GameView2, aName: String, aPosX: Float, aPosY: Float, aSpeedX: Float, aSpeedY: Float,aSize: Float, aBitmap: Bitmap) : this(aGameView){
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
            increaseDifficulty()
            posY += speedY
            posX += speedX
            if(gameView.score > gameView.bestScore)
                gameView.bestScore = gameView.score
            detectCollision()
            detectExistCollision()
            detectBorderCollision()

        }

    }

    private fun increaseDifficulty(){

        if((gameView.score - 10) >= difficultyIncreaseThreshold) {
            speedY *= 1.5f
            speedX *= 1.5f
            difficultyIncreaseThreshold += 10
        }

    }
    private fun onCollision(collision: Object, collisionPosX: Float, collisionPosY: Float) {//när ett object kolliderar
        if(collision.tag.contains("Ball") || collision.tag.contains("Rect")) {
            if(!collision.name.equals("Paddle"))
                ballBounce(collisionPosX, collisionPosY)
        }
        if(collision.name.equals("Paddle")) {
            val temp: Float = abs(collision.posX - collisionPosX)/collision.sizeX//om den är 1 så är collisionen lägst till höger, om 0 är det längst till vänster
            val nextSpeedAngle: Float = 200f + temp*140f
            val diagonalSpeed: Float = sqrt((speedX).pow(2) + (speedY).pow(2))//själva hastighet storheten så att säga
            speedX = cos((nextSpeedAngle/360f)*(2f* PI.toFloat())) * diagonalSpeed//på så sätt man ändrar riktningen på hastigheten men behåller samma hastighet
            speedY = sin((nextSpeedAngle/360)*(2* PI.toFloat())) * diagonalSpeed
        }
        if(collision.tag.contains("Enemy")) {
            gameView.score++
            println("yo?: ${collision.name}")
            inCollisionObjects.remove(collision)
            collision.tag = "remove"//på så sätt försvinner den på ett säkert sätt medans den removas på ett säkert sätt från listan
            gameView.idsToRemove.add(collision.id)//så att den removas senare
            println( gameView.objects.count())
        }
    }
    private fun onExitCollision(collision: Object) {//när ett object som kolliderade innan går ut och slutar kollidera
    }
    private fun detectBorderCollision() {
        if (posX - size <= 0) {//Left
            ballBounce(0f, posY)
        }
        if (posX + size > gameView.limit.right) {//Right
            ballBounce(gameView.limit.right.toFloat(), posY)
        }
        if (posY - size <= 0) {//Top
            ballBounce(posX, 0f)
        }
        if (posY + size > gameView.limit.bottom) {//Bottom
            val handler = android.os.Handler(Looper.getMainLooper())
            handler.post {
                val builder = AlertDialog.Builder(gameView.context)
                builder.setMessage("You lose \nYour score is: ${gameView.score}")
                    .setTitle("Game over")
                    .setCancelable(false)
                    .setPositiveButton("Save Score "){dialog, _ ->

                        gameView.saveScore()
                        dialog.dismiss()

                    }

                    .setNegativeButton("Replay"){dialog, _ ->
                        dialog.dismiss()
                        gameView.score = 0
                        gameView.stop = false
                        resetGame()
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
            val aRect = RectF(posX - size, posY - size, posX + size , posY + size)//to draw the bitmap in a circle way
            canvas.drawBitmap(bitmap, null, aRect, paint)
        }

    }

    private fun ballBounce(collisionPosX: Float, collisionPosY: Float){
        var collisionAngle: Float = pointToDegrees(trueDistance(posX, collisionPosX), trueDistance(posY,collisionPosY))//beroende på differenspunkten man får en vinkel
        var speedAngle: Float = pointToDegrees(speedX, speedY)//vinkel av hastigheten beroende på en denna punkt (speedX, speedY)
        var diagonalSpeed: Float = sqrt((speedX).pow(2) + (speedY).pow(2))//själva hastighet storheten så att säga

        var nextSpeedAngle: Float //nästa vinkel för hanstigheten. så med diagnoal speed och den man kommer skapa en ny punkt av hasighet (speedX, speedY)
        var angleDifference: Float = abs(collisionAngle - speedAngle)//skillnaden i grader mellan kollision vinkeln och hasighetsvinkeln
        if(angleDifference > 180) {//om skillnaden är större än 180 grader så är det faktiskt en skillnad mindre än 180 grader
            angleDifference -= 180
            angleDifference = 180 - angleDifference
        }

        //de här bara finns för testning
        //println("angleDif = $angleDifference  collisionAng = $collisionAngle  speedAng = $speedAngle")
        //println("collisionX = $collisionPosX  collisionY = $collisionPosY  posX = $posX  posY = $posY")
        //println("trueX = ${trueDistance(posX, collisionPosX)}  trueY = ${trueDistance(posY, collisionPosY)}")

        if(angleDifference >= 90 || collisionAngle == speedAngle) {//om skillnaden i grader är större än 90 så betyder det att ett object träffade bollen inte att bollen träffade den och vill man flytta iväg från den
            nextSpeedAngle = collisionAngle +180//och på så sätt man flyttar iväg från den man väljer vinkeln på motsatt sida
            if(nextSpeedAngle > 360)
                nextSpeedAngle -= 360
        }
        else if(collisionAngle - 90 <0) {// eftersom det skulle skapa problem om detta är true händer speciella beräkningar här inne
            if(speedAngle < collisionAngle || speedAngle > 360 + (collisionAngle - 90)) {
                nextSpeedAngle = speedAngle - 180f *(1f- angleDifference/90f)//det är så bollar stutsar beroende på vinklar av kollisionen och hastighet
                if(nextSpeedAngle < 0)
                    nextSpeedAngle = 360 - abs(nextSpeedAngle)
            }
            else {//om speed vinkeln är större än kollision vinkeln
                nextSpeedAngle = speedAngle + 180f *(1f- angleDifference/90f)
            }
        }
        else if(collisionAngle + 90 > 360) {//skapar också lite problem
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
        //println("nextspeedangle = $nextSpeedAngle")
        //println("cos = ${cos(nextSpeedAngle)}")
        //println("sin = ${sin(nextSpeedAngle)}")

        speedX = cos((nextSpeedAngle/360f)*(2f* PI.toFloat())) * diagonalSpeed//på så sätt man ändrar riktningen på hastigheten men behåller samma hastighet
        speedY = sin((nextSpeedAngle/360)*(2* PI.toFloat())) * diagonalSpeed

    }
     private fun resetGame(){
         //bara enemyGenerator resettas själv i sig själv
        gameView.objects.forEach{
            if(it.tag.contains("Enemy")){
                gameView.idsToRemove.add(it.id)
                it.tag = "remove"
            }
        }
        while(difficultyIncreaseThreshold -10 >= 0) {

            speedX /= 1.5f
            speedY /= 1.5f
            difficultyIncreaseThreshold -= 10
        }
        posY = 100f

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
                if(sqrt((posX - it.posX).pow(2) + (posY - it.posY).pow(2)) <= size) {//uppe-väntster
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX, it.posY)
                }
                else if(sqrt((posX - it.posX).pow(2) + (posY - (it.posY + it.sizeY)).pow(2)) <= size){//nedre-vänster
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX, it.posY + it.sizeY)
                }
                else if(sqrt((posX - (it.posX + it.sizeX)).pow(2) + (posY - it.posY).pow(2)) <= size){//högra-uppe
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX + it.sizeX, it.posY)
                }
                else if(sqrt((posX - (it.posX + it.sizeX)).pow(2) + (posY - (it.posY + it.sizeY)).pow(2)) <= size){//nedre-vänster
                    inCollisionObjects.add(it)
                    onCollision(it, it.posX + it.sizeX, it.posY + it.sizeY)
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
                else if(sqrt((posX - (it.posX + it.sizeX)).pow(2) + (posY - (it.posY + it.sizeY)).pow(2)) <= size){
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
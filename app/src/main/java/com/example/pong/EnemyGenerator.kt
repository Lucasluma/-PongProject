package com.example.pong

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Looper
import androidx.lifecycle.LifecycleCoroutineScope
import kotlin.random.Random

class EnemyGenerator(aGameView: GameView2, aLoopTime: Float, aDefaultEnemySpeed: Float,aDefaultEnemySizeX: Float, aDefaultEnemySizeY: Float, aDefaultRoundEnemySize: Float): Object(){
    override var tag: String = "Still"
    override var name: String = ""
    override var posX: Float = 0f
    override var posY: Float = 0f
    override var size: Float = 0f
    override var sizeY: Float = 0f
    override var sizeX: Float = 0f
    override var speedY: Float = 0f
    override var speedX: Float = 0f
    override var stillObject: Boolean = true
    var gameView: GameView2
    override var id: Int
    var difficultyIncreaseThreshold: Int = 0

    var loopTime: Float = 0f
    var timeLeft: Float = 0f
    var myThread: TimerThread = TimerThread(loopTime)
    var enemyDefaultSpeed: Float
    var enemyDefaultSizeX: Float
    var enemyDefaultSizeY: Float
    var roundEnemyDefaultSize: Float

    var threadWasRunning: Boolean = false//används när spelet stoppas för att spara om threaden var running för att starta den senare
    init {
        loopTime = aLoopTime
        gameView = aGameView
        id = aGameView.objectsCreated +1 //varje objekt har en speciell id så att kollisions kan fungera
        aGameView.objectsCreated++ //objectscreated hjälper med att objekt ska ha unika ids
        myThread = TimerThread(loopTime)
        myThread.start()//startar här eftersom den behöver bli startad en gång bara
        enemyDefaultSpeed = aDefaultEnemySpeed
        enemyDefaultSizeX = aDefaultEnemySizeX
        enemyDefaultSizeY = aDefaultEnemySizeY
        roundEnemyDefaultSize = aDefaultRoundEnemySize
    }



    override fun update() {
        increaseDifficulty()
        timeLeft = myThread.time
        if(timeLeft == 0f) {
            println("wtf")
            spawnEnemies()
            myThread.time = loopTime
            timeLeft = loopTime
        }
    }

    override fun start() {

    }

    fun increaseDifficulty(){
        if((gameView.score - 10) >= difficultyIncreaseThreshold) {
            if(loopTime - 1000f >= 500)
                loopTime -= 1000f
            difficultyIncreaseThreshold += 10
        }

    }
    fun reset(){
        while(difficultyIncreaseThreshold -10 >= 0) {
            loopTime += 1000f
            difficultyIncreaseThreshold -= 10
        }
    }
    fun spawnEnemies(){
        var tempRandom = Random.nextInt(0, 2)
        if(tempRandom == 0) {
            gameView.objectsToAdd.add(
                EnemyRect(
                    gameView,
                    "enemyRect$id",
                    Random.nextDouble(0.0, gameView.limit.right.toDouble() - enemyDefaultSizeX)
                        .toFloat(),
                    0f - enemyDefaultSizeY,
                    0f,
                    enemyDefaultSpeed,
                    enemyDefaultSizeX,
                    enemyDefaultSizeY,
                    BitmapFactory.decodeResource(gameView.context.resources, R.drawable.spacenuke)
                )
            )
        }
        else if(tempRandom == 1){
            gameView.objectsToAdd.add(
                Enemy(
                    gameView,
                    "enemy",
                    Random.nextDouble(0.0, gameView.limit.right.toDouble() - roundEnemyDefaultSize).toFloat(),
                    0 - roundEnemyDefaultSize,
                    0f,
                    enemyDefaultSpeed,
                    roundEnemyDefaultSize,
                    BitmapFactory.decodeResource(gameView.context.resources, R.drawable.spacemine)
                )
            )
        }
        else {

        }
    }
    override fun draw(canvas: Canvas) {
        if(gameView.gameOverUText.contains("yes") && !gameView.gameOverUText.contains("#$id#")) {//man kontrollerar här om man har förlorat eftersom draw runnar även när man förlorar
            myThread.stop = true//på så sätt stoppas thread när spelet stoppas: draw fortfarande runnar när spelet stoppas
            myThread.time = loopTime
            threadWasRunning = true
            timeLeft = 10000f
            gameView.gameOverUText += "#$id#"

        }
        if(gameView.gameOverUText.contains("reset") && !gameView.gameOverUText.contains("#$id#")) {//man kontrollerar här om man har förlorat eftersom draw runnar även när man förlorar
            reset()
            myThread.time = loopTime
            myThread.stop = false
            gameView.gameOverUText += "#$id#"
        }


    }
}
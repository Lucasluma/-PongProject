package com.example.pong

import android.graphics.BitmapFactory
import android.graphics.Canvas
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
        if(myThread.isAlive) {
            timeLeft = myThread.getTime()

        }
        if(timeLeft == 0f) {
            println("wtf")
            myThread.stop = true
            myThread.join()
            spawnEnemies()
            myThread = TimerThread(loopTime)
            myThread.start()
            timeLeft = 10000f//för att den här if satsen inte ska kallas igen medans man får nya tiden från nya thread
        }
    }
    fun spawnEnemies(){
        val enemyRectDrawables = arrayOf(R.drawable.spacecargo1_1, R.drawable.spacecargo1_2, R.drawable.spacenuke1_1,
                                         R.drawable.shipdebri1_1,R.drawable.shipdebri1_2)

        val tempRandom = Random.nextInt(0, 1)

        if (tempRandom == 0) {
            val randomDrawableIndex = (0 until enemyRectDrawables.size).random()
            gameView.objectsToAdd.add(
                EnemyRect(
                    gameView,
                    "enemyRect$id",
                    Random.nextDouble(0.0, gameView.limit.right.toDouble() - enemyDefaultSizeX).toFloat(),
                    0f - enemyDefaultSizeY,
                    0f,
                    enemyDefaultSpeed,
                    enemyDefaultSizeX,
                    enemyDefaultSizeY,
                    BitmapFactory.decodeResource(
                        gameView.context.resources,
                        enemyRectDrawables[randomDrawableIndex]
                    )
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
                    BitmapFactory.decodeResource(gameView.context.resources, R.drawable.spacemine1_1)
                )
            )
        }
        else {

        }
    }
    override fun draw(canvas: Canvas) {
        if(gameView.stop) {
            if (myThread.isAlive && !threadWasRunning) {
                myThread.stop = true//på så sätt stoppas thread när spelet stoppas: draw fortfarande runnar när spelet stoppas
                myThread.join()
                threadWasRunning = true
                timeLeft = 10000f
            }
        }
        else {
            if(threadWasRunning) {// och på så sätt är genereringen back in action när spelet startar igen
                myThread = TimerThread(loopTime)
                myThread.start()
                threadWasRunning = false
            }
        }


    }
}
package com.example.pong

import kotlinx.coroutines.delay

class TimerThread(aTime: Float): Thread() {
    public var stop: Boolean = false
    private var time: Float
    init {
        time = aTime
    }
    override fun run() {
        while(time > 0 && !stop) {
            sleep(100)
            if(time >= 100)
                time -= 100
            else
                time = 0f
            println(time)
        }
        sleep(100)//man kan inte få tillgång till sista tiden vilket är 0 om man inte gör så, helt enkelt enemygeneratorn hinner inte få den
    }
    fun getTime(): Float {
        return time
    }
}
package com.example.pong

import kotlinx.coroutines.delay

class TimerThread(aTime: Float): Thread() {
    public var stop: Boolean = false
    var time: Float
    init {
        time = aTime
    }
    override fun run() {
        while(1 == 1) {
            if(!stop) {
                sleep(100)
                if (time >= 100)
                    time -= 100
                else
                    time = 0f
            }
        }
        //sleep(10000)//man kan inte få tillgång till sista tiden vilket är 0 om man inte gör så, helt enkelt enemygeneratorn hinner inte få den
    }

}
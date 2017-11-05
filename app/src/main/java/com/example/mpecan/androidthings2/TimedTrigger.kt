package com.example.mpecan.androidthings2

import android.util.Log

/**
 * TimedTrigger runs in the background and triggers the supplied function every <sleep> milliseconds
 */
class TimedTrigger(public var sleep: Long, var function: ()->Unit) : Thread() {
    override fun run() {
        while(true) {
            function.invoke()
            Thread.sleep(sleep)
            Log.i("BLINKER", "Blinking")
        }
    }
}
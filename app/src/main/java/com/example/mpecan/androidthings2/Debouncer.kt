package com.example.mpecan.androidthings2

import android.util.Log
import java.util.*

/**
 * Dumb little debounce that will ignore additional triggers in the quiescent period defined by the offset constructor parameter
 * parameters:
 *  offset: how much time after the first trigger should we ignore the signal
 *  function: the function that should be triggered
 */
class Debouncer(private var offset: Long, private var function: () -> Unit){
    private var lastTriggered = 0L

    fun trigger() {
        val currentTimeInMilis = System.currentTimeMillis()
        if(lastTriggered + offset < currentTimeInMilis){
            function.invoke()
            Log.i("DEBOUNCER", "Triggering")
        } else {
            Log.i("DEBOUNCER", "Skipping trigger")
        }
        lastTriggered = currentTimeInMilis
    }
}
package com.example.mpecan.androidthings2

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManagerService

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {


    var sleep = 2000L
    var step = 200L

    private lateinit var currentTiming: EditText

    private lateinit var stepEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = PeripheralManagerService()
        val led = service.openGpio("GPIO_37")
        led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

        currentTiming = findViewById<EditText>(R.id.current_timing)


        findViewById<Button>(R.id.up).setOnClickListener {
            increase()
        }
        findViewById<Button>(R.id.down).setOnClickListener {
            decrease()
        }
        findViewById<Button>(R.id.step_up).setOnClickListener {
            increase_step()
        }
        findViewById<Button>(R.id.step_down).setOnClickListener {
            decrease_step()
        }

        currentTiming.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                sleep = currentTiming.text.toString().toLong()
            }
        }

        stepEdit = findViewById<EditText>(R.id.step)
        stepEdit.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                step = stepEdit.text.toString().toLong()
            }
        }

        val button = service.openGpio("GPIO_32")
        button.setDirection(Gpio.DIRECTION_IN)
        button.setEdgeTriggerType(Gpio.EDGE_FALLING)

        button.registerGpioCallback(object: GpioCallback() {
            override fun onGpioEdge(gpio: Gpio?): Boolean {
                decrease()
                return true
            }
        })
        val thread = object: Thread(){
            override fun run() {
                while (true) {
                    led.value = !led.value
                    Thread.sleep(sleep)
                }
            }
        }
        thread.start()

    }

    fun increase() {
        sleep += step;
        currentTiming.text.clear()
        currentTiming.text.append(sleep.toString())
    }

    fun decrease() {
        sleep -= step;
        if(sleep < 1){
            sleep = 2000
        }
        currentTiming.text.clear()
        currentTiming.text.append(sleep.toString())
    }

    fun increase_step() {
        step += 50;
        stepEdit.text.clear()
        stepEdit.text.append(step.toString())
    }

    fun decrease_step() {
        step -= 50;
        if(sleep < 1){
            sleep = 200
        }
        stepEdit.text.clear()
        stepEdit.text.append(step.toString())
    }


}

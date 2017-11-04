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

    private lateinit var currentTiming: TextView

    private lateinit var stepEdit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = PeripheralManagerService()
        val led = service.openGpio("GPIO_37")
        led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

        findElements()

        registerListeners()

        val button = service.openGpio("GPIO_32")
        button.setDirection(Gpio.DIRECTION_IN)
        button.setEdgeTriggerType(Gpio.EDGE_FALLING)

        button.registerGpioCallback(object : GpioCallback() {
            override fun onGpioEdge(gpio: Gpio?): Boolean {
                decrease()
                return true
            }
        })
        val thread = object : Thread() {
            override fun run() {
                while (true) {
                    led.value = !led.value
                    Thread.sleep(sleep)
                }
            }
        }
        thread.start()

    }

    private fun registerListeners() {
        findViewById<Button>(R.id.up).setOnClickListener {
            increase()
        }
        findViewById<Button>(R.id.down).setOnClickListener {
            decrease()
        }
        findViewById<Button>(R.id.step_up).setOnClickListener {
            increaseStep()
        }
        findViewById<Button>(R.id.step_down).setOnClickListener {
            decreaseStep()
        }

        currentTiming.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                sleep = currentTiming.text.toString().toLong()
            }
        }

        stepEdit.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                step = stepEdit.text.toString().toLong()
            }
        }
    }

    private fun findElements() {
        currentTiming = findViewById(R.id.current_timing)
        stepEdit = findViewById(R.id.step)
    }

    private fun increase() {
        sleep += step;
        displaySleepValue()
    }

    fun decrease() {
        sleep -= step;
        if (sleep < 1) {
            sleep = 2000
        }
        displaySleepValue()
    }

    private fun displaySleepValue() {
        currentTiming.text = sleep.toString()
    }

    private fun increaseStep() {
        step += 50;
        displayStepValue()
    }

    private fun displayStepValue() {
        stepEdit.text = step.toString()
    }

    private fun decreaseStep() {
        step -= 50;
        if (sleep < 1) {
            sleep = 200
        }
        displayStepValue()
    }


}

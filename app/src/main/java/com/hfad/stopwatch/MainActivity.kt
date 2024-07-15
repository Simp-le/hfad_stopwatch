package com.hfad.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var stopwatch: Chronometer
    private var running = false
    private var offset: Long = 0

    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"
    private val OFFSET_KEY = "offset"

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }
    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        stopwatch = findViewById(R.id.stopwatch)

        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            offset = savedInstanceState.getLong(OFFSET_KEY)

            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }


        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if (!running) {
                running = true
                setBaseTime()
                stopwatch.start()
            }
        }

        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running) {
                running = false
                saveOffset()
                stopwatch.stop()
            }
        }

        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            if (running) {
                running = false
                stopwatch.stop()
            }
            offset = 0
            setBaseTime()
        }
    }

    // Save the bundle for the next onCreate() call. There is no Bundle object in onDestroy()
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, stopwatch.base)
        outState.putLong(OFFSET_KEY, offset)
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            offset = 0
            stopwatch.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (running){
            saveOffset()
            stopwatch.stop()
        }
    }
}
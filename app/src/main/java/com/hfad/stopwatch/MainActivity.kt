package com.hfad.stopwatch

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hfad.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var running = false
    private var offset: Long = 0

    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"
    private val OFFSET_KEY = "offset"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Creates an ActivityMainBinding object that is linked to the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Set view to the root view
        val view = binding.root
        // Pass the root view to setContentView()
        setContentView(view)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            offset = savedInstanceState.getLong(OFFSET_KEY)

            if (running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else setBaseTime()
        }


        binding.startButton.setOnClickListener {
            if (!running) {
                running = true
                setBaseTime()
                binding.stopwatch.start()
            }
        }

        binding.pauseButton.setOnClickListener {
            if (running) {
                running = false
                saveOffset()
                binding.stopwatch.stop()
            }
        }

        binding.resetButton.setOnClickListener {
            if (running) {
                running = false
                binding.stopwatch.stop()
            }
            offset = 0
            setBaseTime()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            offset = 0
            binding.stopwatch.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (running){
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    // Save the bundle for the next onCreate() call. There is no Bundle object in onDestroy()
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, binding.stopwatch.base)
        outState.putLong(OFFSET_KEY, offset)
    }

    private fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }
    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }
}
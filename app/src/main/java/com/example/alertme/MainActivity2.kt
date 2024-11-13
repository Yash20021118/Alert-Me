package com.example.alertme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alertme.databinding.ActivityMain2Binding // Ensure this matches your layout file name
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding // Change to ActivityMain2Binding
    private var isRunning = false
    private var timeInMilliseconds = 0L
    private var startTime = 0L
    private var updatedTime = 0L
    private val handler = Handler()

    private var taskName: String = ""
    private val taskList = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater) // Inflate the correct binding
        setContentView(binding.root)

        // Initialize RecyclerView and adapter
        adapter = TaskAdapter(taskList)
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter

        // Bottom Navigation Setup
        setupBottomNavigation()

        // Start Button
        binding.btnStart.setOnClickListener {
            taskName = binding.etTaskName.text.toString().trim()
            if (taskName.isNotEmpty() && !isRunning) {
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(runnable, 0)
                isRunning = true
            }
        }

        // Stop Button
        binding.btnStop.setOnClickListener {
            if (isRunning) {
                timeInMilliseconds += SystemClock.uptimeMillis() - startTime
                handler.removeCallbacks(runnable)
                isRunning = false
                saveTaskTime(taskName, updatedTime)
            }
        }

        // Reset Button
        binding.btnReset.setOnClickListener {
            timeInMilliseconds = 0L
            startTime = 0L
            updatedTime = 0L
            binding.tvTime.text = "00:00:00"
            binding.etTaskName.setText("")
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_stopwatch -> {
                    // Handle stopwatch selection
                    // This is the current activity, so no action needed
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_tasks -> {
                    // Handle tasks selection
                    // You can add code to navigate to a tasks fragment if needed
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            updatedTime = timeInMilliseconds + (SystemClock.uptimeMillis() - startTime)
            val seconds = (updatedTime / 1000).toInt()
            val minutes = seconds / 60
            val hours = minutes / 60
            binding.tvTime.text = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
            handler.postDelayed(this, 0)
        }
    }

    private fun saveTaskTime(taskName: String, timeSpent: Long) {
        val seconds = (timeSpent / 1000).toInt()
        val minutes = seconds / 60
        val hours = minutes / 60
        val timeString = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
        val task = Task(taskName, timeString)

        // Add task to the list
        taskList.add(task)
        adapter.notifyItemInserted(taskList.size - 1)

        println("Task: $taskName, Time Spent: $timeString")
    }
}

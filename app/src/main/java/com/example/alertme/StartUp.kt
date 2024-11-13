package com.example.alertme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        val navigateButton: Button = findViewById(R.id.navigate_button)

        navigateButton.setOnClickListener {
            // Navigate to the SecondActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.caleblimb.spellcrastination

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.button_start)


        startButton?.setOnClickListener()
        {
//            val playerCount = findViewById<EditText>(R.id.editTextNumberPlayerCount).text as Int
//            val game : Game = Game(playerCount)
            val intent = Intent(this@MainActivity, Game::class.java)
            startActivity(intent)
        }
    }
}

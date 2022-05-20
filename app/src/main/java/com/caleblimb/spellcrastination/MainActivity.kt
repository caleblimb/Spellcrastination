package com.caleblimb.spellcrastination

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.button_start)

        val playerCountTextView = findViewById<TextView>(R.id.textViewPlayerCount)
        val playerSpinner = findViewById<Spinner>(R.id.playerSpinner)
        var playerCount = 2

        if (playerSpinner != null) {
//            val adapter = ArrayAdapter( this, android.R.layout.simple_spinner_item, playerSpinner)
//            playerSpinner.adapter = adapter
            playerSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    playerCount = position + 2
                    playerCountTextView.text = "Player Count ".plus(playerCount.toString())
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }


        startButton?.setOnClickListener()
        {

            val intent = Intent(this@MainActivity, Game::class.java)

//            intent.putExtra("PLAYER_COUNT", playerCount)
            startActivity(intent)
        }
    }
}

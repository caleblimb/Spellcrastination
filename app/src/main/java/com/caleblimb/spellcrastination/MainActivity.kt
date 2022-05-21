package com.caleblimb.spellcrastination

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.EditText
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.button_start)

        val playerCountTextView = findViewById<TextView>(R.id.textViewPlayerCount)
        val playerSpinner = findViewById<Spinner>(R.id.playerSpinner)
        var playerCount = 2
        val playersView = findViewById<LinearLayout>(R.id.playersView)
        val context = this

        if (playerSpinner != null) {
            playerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    playerCount = position + 2
                    playerCountTextView.text = "Players ".plus(playerCount.toString())
                    if (playerCount > playersView.childCount) {
                        while (playerCount > playersView.childCount) {
                            val player: EditText = EditText(context)
                            player.hint = "Player Name"
//                            player.setBackgroundColor(Color.BLUE)
                            player.layoutParams = ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT,
                                96
                            )
                            playersView.addView(player)
                        }
                    } else if (playerCount < playersView.childCount) {
                        while (playerCount < playersView.childCount) {
                            playersView.removeViewAt(playersView.childCount - 1)
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

        startButton?.setOnClickListener()
        {
            val intent = Intent(this@MainActivity, Game::class.java)
            val players: MutableList<String> = ArrayList<String>()
            for (p in 0 until playerCount) {
                val np = playersView[p] as EditText
                players.add(np.text.toString())
            }
            intent.putStringArrayListExtra("PLAYERS", players as ArrayList<String>)
            startActivity(intent)
        }
    }
}

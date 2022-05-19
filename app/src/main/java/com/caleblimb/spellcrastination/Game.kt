package com.caleblimb.spellcrastination

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.caleblimb.scrabblelookup.Dictionary

class Game : AppCompatActivity {
    val players: Int
    var currentPlayer: Int = 0

    constructor() {
        this.players = 2
    }

    constructor(players: Int) {
        this.players = players
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val message = findViewById<TextView>(R.id.textView_message)

        var isChallenging: Boolean = false

        val nextLetter = findViewById<EditText>(R.id.editTextNextLetter)
        nextLetter.setPadding(8, 2, 8, 2)
//        nextLetter.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        nextLetter.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(1), InputFilter.AllCaps())
        nextLetter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        var players: Int

        val letters = findViewById<TextView>(R.id.textView_word)

        val dictionary = Dictionary(this)

        val buttonCompleted = findViewById<Button>(R.id.button_completed)
        buttonCompleted?.setOnClickListener()
        {
            message.text = "Enter a letter"
            val message = AlertDialog.Builder(this@Game)

            val word = letters.text.toString()

            if (dictionary.isWord(word)) {
                message.setTitle(word.plus(" is a word!"))
                message.setMessage("The Previous Player Loses")
            } else {
                message.setTitle(word.plus(" is NOT a word!"))
                message.setMessage("The current Player Loses")
            }

            message.setPositiveButton("Continue") { dialog, which ->
            }
            val dialog: AlertDialog = message.create()
            dialog.show()
        }

        val buttonChallenge = findViewById<Button>(R.id.button_challenge)
        buttonChallenge?.setOnClickListener()
        {
            message.text = previousPlayer().toString().plus("\nComplete a word")
            isChallenging = true
            nextLetter.filters =
                arrayOf<InputFilter>(InputFilter.LengthFilter(10), InputFilter.AllCaps())
        }

        val buttonSubmitLetter = findViewById<ImageButton>(R.id.imageButton_submit)
        buttonSubmitLetter?.setOnClickListener()
        {
            val input = nextLetter.text.toString()
            if (input.isNotEmpty()) {
                if (!isChallenging) {
                    val letter = input[input.length - 1]
                    letters.text = letters.text.toString().plus(letter)
                    nextLetter.text.clear()
                } else {
                    message.text = "Enter a letter"
                    val message = AlertDialog.Builder(this@Game)

                    val word = letters.text.toString().plus(input)

                    if (dictionary.isWord(word)) {
                        message.setTitle(word.plus(" is a word!"))
                        message.setMessage("The Previous Player Loses")
                    } else {
                        message.setTitle(word.plus(" is NOT a word!"))
                        message.setMessage("The current Player Loses")
                    }

                    message.setPositiveButton("Continue") { dialog, which ->
                    }
                    val dialog: AlertDialog = message.create()
                    dialog.show()

                    isChallenging = false
                    letters.text = ""
                    nextLetter.text.clear()
                }
            }
        }
    }
    fun nextPlayer() : Int {
        return (currentPlayer + 1) % players
    }
    fun currentPlayer() : Int {
        return currentPlayer
    }
    fun previousPlayer() : Int {
        return (currentPlayer - 1 ) % players
    }
    fun advancePlayer() {
        currentPlayer = nextPlayer()
    }

    fun openSoftKeyboard(view: View) {
        view.requestFocus()
    }

}
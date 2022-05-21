package com.caleblimb.spellcrastination

import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.caleblimb.scrabblelookup.Dictionary

class Game : AppCompatActivity() {
    private var players: ArrayList<String>? = null
    private var playerCount: Int = 0
    private var previousPlayer: Int = 0
    private var currentPlayer: Int = 0
    private var isChallenging: Boolean = false
    private lateinit var messageView: TextView
    private lateinit var nextLetterView: EditText
    private lateinit var lettersView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        messageView = findViewById<TextView>(R.id.textView_message)
        nextLetterView = findViewById<EditText>(R.id.editTextNextLetter)
        lettersView = findViewById<TextView>(R.id.textView_word)
        val dictionary: Dictionary = Dictionary(this)
        val buttonCompleted: Button = findViewById<Button>(R.id.button_completed)
        val buttonChallenge: Button = findViewById<Button>(R.id.button_challenge)
        val buttonSubmitLetter: ImageButton = findViewById<ImageButton>(R.id.imageButton_submit)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmExitGame()
                }
            }
        )

        players = intent.getStringArrayListExtra("PLAYERS")
        initGame()

        nextLetterView.setPadding(8, 2, 8, 2)

        buttonCompleted.setOnClickListener()
        {
            val word = lettersView.text.toString()
            if (word.length > 3) {
                if (dictionary.isWord(word)) {
                    showDialog(
                        word.plus(" is a word!"),
                        previousPlayer().plus(" is eliminated!")
                    )
                    eliminatePlayer(previousPlayer)
                } else {
                    showDialog(
                        word.plus(" is NOT a word!"),
                        currentPlayer().plus(" is eliminated!")
                    )
                    eliminatePlayer(currentPlayer)
                }
                initGame()
            }
        }

        buttonChallenge.setOnClickListener()
        {
            val word = lettersView.text.toString()
            if (word.length > 1) {
                messageView.text = previousPlayer().plus("\nComplete a word")
                isChallenging = true
                nextLetterView.filters =
                    arrayOf<InputFilter>(InputFilter.LengthFilter(20), InputFilter.AllCaps())
            }
        }

        buttonSubmitLetter.setOnClickListener()
        {
            val input = nextLetterView.text.toString()
            if (input.any { !it.isWhitespace() }) {
                if (!isChallenging) {
                    val letter = input[input.length - 1]
                    lettersView.text = lettersView.text.toString().plus(letter)
                    nextLetterView.text.clear()
                    advancePlayer()
                } else {
                    val word = lettersView.text.toString().plus(input)
                    if (dictionary.isWord(word)) {
                        showDialog(
                            word.plus(" is a word!"),
                            currentPlayer().plus(" is eliminated!")
                        )
                        eliminatePlayer(currentPlayer)
                    } else {
                        showDialog(
                            word.plus(" is NOT a word!"),
                            previousPlayer().plus(" is eliminated!")
                        )
                        eliminatePlayer(previousPlayer)
                    }
                    advancePlayer()
                    initGame()
                }
            }
        }
    }

    fun currentPlayer(): String {
        return players!![currentPlayer]
    }

    fun previousPlayer(): String {
        return players!![previousPlayer]
    }

    fun advancePlayer() {
        previousPlayer = currentPlayer
        currentPlayer = (currentPlayer + 1) % playerCount
        messageView.text = currentPlayer().plus("\nEnter a letter")
    }

    fun initGame() {
        playerCount = players!!.size
        isChallenging = false
        messageView.text = currentPlayer().plus("\nEnter a letter")
        lettersView.text = ""
        nextLetterView.text.clear()
        nextLetterView.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(1), InputFilter.AllCaps())
    }

    fun showDialog(title: String, message: String) {
        val alert = AlertDialog.Builder(this@Game)

        alert.setTitle(title)
        alert.setMessage(message)

        alert.setPositiveButton("Continue") { _dialog, _which ->
        }
        val dialog: AlertDialog = alert.create()
        dialog.show()
    }

    fun eliminatePlayer(player: Int) {
        players?.drop(player)
        playerCount--
        if (playerCount == 1) {
            val p = players?.get(0)
            showDialog(p.plus(" Wins!"), p.plus(" was the last survivor."))
        }
    }

    fun confirmExitGame() {
        val alert = AlertDialog.Builder(this@Game)

        alert.setTitle("Quit Game")
        alert.setMessage("Are you sure you want to leave?")

        alert.setPositiveButton("Continue") { _dialog, _which ->
        }
        alert.setNegativeButton("Quit") { _dialog, _which ->
            finish()
        }
        val dialog: AlertDialog = alert.create()
        dialog.show()
    }

}
package com.caleblimb.spellcrastination

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.caleblimb.scrabblelookup.Dictionary

class Game : AppCompatActivity() {
    private var players: MutableList<String>? = null
    private var playerCount: Int = 0
    private var previousPlayer: Int = 0
    private var currentPlayer: Int = 0
    private var isChallenging: Boolean = false
    private lateinit var messageView: TextView
    private lateinit var nextLetterView: EditText
    private lateinit var lettersView: TextView
    private lateinit var buttonCompleted: Button
    private lateinit var buttonChallenge: Button
    private lateinit var buttonSubmitLetter: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        messageView = findViewById<TextView>(R.id.textView_message)
        nextLetterView = findViewById<EditText>(R.id.editTextNextLetter)
        lettersView = findViewById<TextView>(R.id.textView_word)
        val dictionary: Dictionary = Dictionary(this)
        buttonCompleted = findViewById<Button>(R.id.button_completed)
        buttonChallenge = findViewById<Button>(R.id.button_challenge)
        buttonSubmitLetter = findViewById<ImageButton>(R.id.imageButton_submit)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmExitGame()
                }
            }
        )

        players = intent.getStringArrayListExtra("PLAYERS")?.toMutableList()
        initGame()

        nextLetterView.setPadding(8, 2, 8, 2)

        nextLetterView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (nextLetterView.text.any{!it.isWhitespace()}) {
                    enableButton(buttonSubmitLetter)
                } else {
                    disableButton(buttonSubmitLetter)
                }
            }
        })

        buttonCompleted.setOnClickListener()
        {
            val word = lettersView.text.toString()
            if (word.length > 3) {
                if (dictionary.isWord(word)) {
                    eliminatePlayer(previousPlayer)
                    showDialog(
                        word.plus(" is a word!"),
                        previousPlayer().plus(" is eliminated!")
                    )
                } else {
                    eliminatePlayer(currentPlayer)
                    showDialog(
                        word.plus(" is NOT a word!"),
                        currentPlayer().plus(" is eliminated!")
                    )
                }
                initGame()
            }
        }

        buttonChallenge.setOnClickListener()
        {
            val word = lettersView.text.toString()
            if (word.length > 1) {
                disableButton(buttonCompleted)
                disableButton(buttonChallenge)
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
                        eliminatePlayer(currentPlayer)
                        showDialog(
                            word.plus(" is a word!"),
                            currentPlayer().plus(" is eliminated!")
                        )
                    } else {
                        eliminatePlayer(previousPlayer)
                        showDialog(
                            word.plus(" is NOT a word!"),
                            previousPlayer().plus(" is eliminated!")
                        )
                    }
                    advancePlayer()
                    initGame()
                }
            }
        }
    }

    fun disableButton(button: ImageButton) {
        button.isEnabled = false
        button.isClickable = false
        button.backgroundTintList = ColorStateList.valueOf(Color.GRAY);
    }

    fun enableButton(button: ImageButton){
        button.isEnabled = true
        button.isClickable = true
        button.backgroundTintList = ColorStateList.valueOf(Color.rgb(85,238,85));
    }

    fun disableButton(button: Button) {
        button.isEnabled = false
        button.isClickable = false
        button.backgroundTintList = ColorStateList.valueOf(Color.GRAY);
    }

    fun enableButton(button: Button){
        button.isEnabled = true
        button.isClickable = true
        button.backgroundTintList = ColorStateList.valueOf(Color.rgb(85,238,85));
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
        if (lettersView.text.length > 3) {
            enableButton(buttonCompleted)
        }
        if (lettersView.text.length > 1) {
            enableButton(buttonChallenge)
        }
    }

    fun initGame() {
        disableButton(buttonCompleted)
        disableButton(buttonChallenge)
        disableButton(buttonSubmitLetter)
        playerCount = players!!.size
        isChallenging = false
        messageView.text = currentPlayer().plus("\nEnter a letter")
        lettersView.text = ""
        nextLetterView.text.clear()
        nextLetterView.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(1), InputFilter.AllCaps())
    }

    fun showDialog(title: String, message: String, finish: Boolean = false) {
        val alert = AlertDialog.Builder(this@Game)

        alert.setTitle(title)
        alert.setMessage(message)

        alert.setPositiveButton("Continue") { _dialog, _which ->
            if (finish) {
                finish()
            }
        }
        val dialog: AlertDialog = alert.create()
        dialog.show()
    }

    fun eliminatePlayer(player: Int) {
//        players?.drop(player)
        players?.removeAt(player)
        playerCount--
        advancePlayer()
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
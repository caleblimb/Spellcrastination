package com.caleblimb.scrabblelookup

import android.content.Context
import java.io.IOException
import java.io.InputStream

class Dictionary(context: Context) {
    // Define name of dictionary file
    val fileName: String = "words.txt"

    // Create a set to hold the words
    val words: Set<String>

    init {
        // String to hold file
        var string: String = ""
        // Each line is a single word
        val delimiter = "\r\n"
        try {
            // Open and Real File
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Populate the set with the words from the file
        words = string.lowercase().split(delimiter).toSet()
    }

    fun isWord(word: String): Boolean {
        // Filter user input to standardize to lowercase and remove white space.
        // Return true if the input is a word and false if it isn't found.
        return (word.lowercase().filter{!it.isWhitespace()} in words)
    }
}


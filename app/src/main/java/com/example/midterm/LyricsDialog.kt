package com.example.midterm

import android.app.Dialog
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

class LyricsDialog(context: Context, var lyricsRawId: Int) : Dialog(context) {
    private val lyricsTv: TextView
    private val closeBtn: ImageView

    init {
        setContentView(R.layout.lyrics_dialog)
        lyricsTv = findViewById(R.id.lyricsText)
        closeBtn = findViewById(R.id.lyricsCloseBtn)
        closeBtn.setOnClickListener { hide() }
    }

    fun showLyrics() {
        lyricsTv.text = readFile()
        this.show()
    }

    private fun readFile(): String {
        val inputStream: InputStream = context.resources.openRawResource(lyricsRawId)
        val reader = BufferedReader(InputStreamReader(inputStream))

        var readMessage = ""

        var i = reader.read()

        try {
            while (i != -1) {
                readMessage += i.toChar()
                i = reader.read()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println(readMessage)
        return readMessage
    }
}
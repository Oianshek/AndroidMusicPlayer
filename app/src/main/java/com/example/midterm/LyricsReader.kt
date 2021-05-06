package com.example.midterm

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

class LyricsReader {
    private fun readFile(rawId: Int, context: Context): String {
        val inputStream: InputStream = context.resources.openRawResource(rawId)
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
        return readMessage
    }
}
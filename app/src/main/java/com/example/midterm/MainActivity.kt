package com.example.midterm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var playBtn: FloatingActionButton
    private lateinit var pauseBtn: FloatingActionButton
    private lateinit var prevBtn: FloatingActionButton
    private lateinit var nextBtn: FloatingActionButton
    private lateinit var lyricsBtn: FloatingActionButton
    private lateinit var seekBar: SeekBar
    private lateinit var musicNameTv: TextView
    private lateinit var singerTv: TextView
    private lateinit var durationTv: TextView
    private lateinit var musicPoster:ImageView

    private var mediaPlayer: MediaPlayer? = null

    private var currentMusic = MusicList.dataSet[0]
    private lateinit var lyricsDialog: LyricsDialog

    private fun initVars() {
        playBtn = findViewById(R.id.fab_play)
        pauseBtn = findViewById(R.id.fab_pause)
        prevBtn = findViewById(R.id.fab_prev)
        nextBtn = findViewById(R.id.fab_next)
        lyricsBtn = findViewById(R.id.fab_show_lyrics)
        seekBar = findViewById(R.id.seekbar)
        musicNameTv = findViewById(R.id.music_name)
        singerTv = findViewById(R.id.singer)
        durationTv = findViewById(R.id.duration)
        lyricsDialog = LyricsDialog(this, currentMusic.lyricsRawId)
        musicPoster = findViewById(R.id.poster)
    }

    private fun initOnClickListeners() {
        playBtn.setOnClickListener { play() }
        pauseBtn.setOnClickListener { mediaPlayer?.pause() }
        prevBtn.setOnClickListener { prevMusic() }
        nextBtn.setOnClickListener { nextMusic() }
        lyricsBtn.setOnClickListener { lyricsDialog.showLyrics() }
    }

    private fun init() {
        initVars()
        initOnClickListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        singerTv.text = currentMusic.singer
        musicNameTv.text = currentMusic.name
        musicPoster.setImageResource(currentMusic.imageUrl)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    setDurationText(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun createSeekBarForMusic() {
        seekBar.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    val currentPosition = mediaPlayer!!.currentPosition
                    seekBar.progress = currentPosition
                    handler.postDelayed(this, 1000)
                    setDurationText(currentPosition)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }
            }
        }, 0)
    }

    private fun setDurationText(millSeconds: Int) {
        var seconds = millSeconds / 1000
        val minutes = seconds / 60
        seconds %= 60
        durationTv.text = "$minutes:${if (seconds / 10 == 0) 0 else ""}$seconds"
    }

    private fun play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, currentMusic.rawId)
            mediaPlayer?.setOnCompletionListener {
                nextMusic()
            }
            createSeekBarForMusic()
            singerTv.text = currentMusic.singer
            musicNameTv.text = currentMusic.name
            musicPoster.setImageResource(currentMusic.imageUrl)
        }
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    private fun stopPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        setDurationText(0)
    }

    private fun prevMusic() {
        val musicIndex = currentMusic.id - 2
        changeMusic(if (musicIndex == -1) MusicList.dataSet.size - 1 else musicIndex)
    }

    private fun nextMusic() {
        val musicIndex = currentMusic.id
        changeMusic(if (musicIndex == MusicList.dataSet.size) 0 else musicIndex)
    }

    private fun changeMusic(index: Int) {
        currentMusic = MusicList.dataSet[index]
        lyricsDialog.lyricsRawId = currentMusic.lyricsRawId
        stopPlayer()
        play()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayer()
        setNotification()
    }

    private fun setNotification() {
        val channel = NotificationChannel(
            "listenMusicNotificationId",
            "ListenMusicReminderChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val broadcastIntent = Intent(this, ReminderBroadcast::class.java)
        val pendingBroadcast = PendingIntent.getBroadcast(this, 200, broadcastIntent, 0)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 1)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingBroadcast)
    }

}
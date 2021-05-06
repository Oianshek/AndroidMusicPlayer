package com.example.midterm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class ReminderBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val notificationActivityIntent = Intent(context, MainActivity::class.java)
        val pendingActivityIntent = PendingIntent.getActivity(context, 200, notificationActivityIntent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, "listenMusicNotificationId")
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle("Player reminder")
            .setContentText("Let's go to listen music!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingActivityIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(200, notificationBuilder.build())
    }
}
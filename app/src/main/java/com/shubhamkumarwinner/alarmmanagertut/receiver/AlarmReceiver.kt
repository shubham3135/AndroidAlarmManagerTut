package com.shubhamkumarwinner.alarmmanagertut.receiver

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shubhamkumarwinner.alarmmanagertut.MainActivity
import com.shubhamkumarwinner.alarmmanagertut.R

const val CHANNEL_ID = "ChannelId"
class AlarmReceiver: BroadcastReceiver() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action!! == "com.shubhamkumarwinner.alarmmanager"){
            mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI)
            mediaPlayer.start()
            Toast.makeText(context, "alarm started", Toast.LENGTH_SHORT).show()
            createNotification(context)
        }


    }

    private fun createNotification(context: Context){
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        intent.action = mediaPlayer.pause().toString()
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Alarm")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("Get up babe")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, notification)
    }

}
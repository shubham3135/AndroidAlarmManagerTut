package com.shubhamkumarwinner.alarmmanagertut

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.shubhamkumarwinner.alarmmanagertut.databinding.ActivityMainBinding
import com.shubhamkumarwinner.alarmmanagertut.receiver.AlarmReceiver
import com.shubhamkumarwinner.alarmmanagertut.receiver.CHANNEL_ID
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var picker: MaterialTimePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        binding.btnSelectTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnSetAlarm.setOnClickListener {
            setAlarm()
        }

        binding.btnCancelAlarm.setOnClickListener {
            cancelAlarm()
        }
    }

    private fun cancelAlarm() {
        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent,
                0)
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "com.shubhamkumarwinner.alarmmanager"
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES/*86340000 for daily one min early*/, pendingIntent
        )
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(supportFragmentManager, CHANNEL_ID)
        picker.addOnPositiveButtonClickListener {
            if (picker.hour>12){
                binding.timeText.text = String.format("%02d", picker.hour - 12) + " : " + String.format("%02d", picker.minute) + " PM"
            }
            else{
                binding.timeText.text = String.format("%02d", picker.hour) + " : " + String.format("%02d", picker.minute) + " AM"
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "alarmManagerChannel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Channel for alarm manager"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
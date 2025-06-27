package pet_projects.daybyday.ui.habits

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import pet_projects.daybyday.R

class HabitAlarmReceiver: BroadcastReceiver() {
    private var habitId = -1
    private var habitName = ""
    private var habitDesc = ""

    override fun onReceive(context: Context?, intent: Intent?) {
        habitId = intent!!.getIntExtra(EXTRA_HABIT_ID, 0)
        habitName = intent.getStringExtra(EXTRA_HABIT_NAME)!!
        habitDesc = intent.getStringExtra(EXTRA_HABIT_DESC)!!

        showNotification(context!!, habitId, habitName, habitDesc)
    }

    fun showNotification(context: Context, id: Int, name: String, desc: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "habit_reminders_channel"
        val channel = NotificationChannel(
            channelId,
            "Выполнить привычку!!!",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(habitName)
            .setContentText(habitDesc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(habitId, notification)
    }

    companion object {
        const val EXTRA_HABIT_ID = "habit_id"
        const val EXTRA_HABIT_NAME = "habit_name"
        const val EXTRA_HABIT_DESC = "habit_desc"
    }
}
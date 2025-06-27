package pet_projects.daybyday.util.schedulers

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import pet_projects.daybyday.database.data.Habit
import pet_projects.daybyday.ui.habits.HabitAlarmReceiver
import java.time.ZoneId

class HabitSchedulerImpl(private val context: Context): HabitScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun schedule(habit: Habit) {
        val intent = Intent(context, HabitAlarmReceiver::class.java).apply {
            putExtra(HabitAlarmReceiver.EXTRA_HABIT_ID, habit.id)
            putExtra(HabitAlarmReceiver.EXTRA_HABIT_NAME, habit.name)
            putExtra(HabitAlarmReceiver.EXTRA_HABIT_DESC, habit.desc)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habit.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAtMillis = habit.date.atTime(habit.time)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli() - (5 * 60 * 1000)

        if (triggerAtMillis < System.currentTimeMillis()) return

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    override fun cancel(habit: Habit) {
        val intent = Intent(context, HabitAlarmReceiver::class.java)
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                habit.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
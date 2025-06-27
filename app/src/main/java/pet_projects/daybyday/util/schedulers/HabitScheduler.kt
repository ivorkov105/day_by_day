package pet_projects.daybyday.util.schedulers

import pet_projects.daybyday.database.data.Habit

interface HabitScheduler {
    fun schedule(habit: Habit)
    fun cancel(habit: Habit)
}
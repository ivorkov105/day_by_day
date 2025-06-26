package pet_projects.daybyday.repositories

import kotlinx.coroutines.flow.Flow
import pet_projects.daybyday.database.data.Habit
import java.time.LocalDate

interface HabitRepository {
    fun getHabitsForDate(date: LocalDate): Flow<List<Habit>>
    suspend fun insert(habit: Habit)
    suspend fun update(habit: Habit)
    suspend fun delete(habit: Habit)
}
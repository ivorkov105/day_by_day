package pet_projects.daybyday.repositories

import kotlinx.coroutines.flow.Flow
import pet_projects.daybyday.database.daos.HabitDao
import pet_projects.daybyday.database.data.Habit
import java.time.LocalDate

class HabitRepositoryImpl(private val habitDao: HabitDao): HabitRepository {

    override fun getHabitsForDate(date: LocalDate): Flow<List<Habit>> {
        return habitDao.getHabitsForDate(date)
    }

    override suspend fun insert(habit: Habit) {
        habitDao.insert(habit)
    }

    override suspend fun update(habit: Habit) {
        habitDao.update(habit)
    }

    override suspend fun delete(habit: Habit) {
        habitDao.delete(habit)
    }
}
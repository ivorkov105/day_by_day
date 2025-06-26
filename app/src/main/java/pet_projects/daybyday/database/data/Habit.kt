package pet_projects.daybyday.database.data

import java.time.LocalDate
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val desc: String,
    val isCompleted: Boolean = false,
    val date: LocalDate,
    val time: LocalTime
)
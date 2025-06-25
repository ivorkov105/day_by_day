package pet_projects.daybyday.data

data class Habit(
    val id: Int,
    val name: String,
    val desc: String,
    val isCompleted: Boolean = false
)
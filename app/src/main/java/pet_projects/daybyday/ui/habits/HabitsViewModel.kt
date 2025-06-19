package pet_projects.daybyday.ui.habits

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pet_projects.daybyday.data.Habit

class HabitsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<List<Habit>>(emptyList())
    val uiState: StateFlow<List<Habit>> = _uiState.asStateFlow()

    init {
        loadHabits()
    }

    fun loadHabits() {
        val habitsList = listOf(Habit(id = 1, name = "1", desc = "1"),
            Habit(id = 2, name = "2", desc = "2"),
            Habit(id = 3, name = "3", desc = "3", isCompleted = true),
            Habit(id = 4, name = "4", desc = "4"),
            Habit(id = 5, name = "5", desc = "5"))
        _uiState.value = habitsList
    }
}
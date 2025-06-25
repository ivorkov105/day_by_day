package pet_projects.daybyday.ui.habits

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pet_projects.daybyday.data.Habit

class HabitsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<HabitsUiState>(HabitsUiState.Loading)
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

    init {
        loadHabits()
    }

    fun loadHabits() {
        _uiState.value = HabitsUiState.Loading
        try {
            val habitsList = listOf(Habit(id = 1, name = "1", desc = "1"),
                Habit(id = 2, name = "2", desc = "2"),
                Habit(id = 3, name = "3", desc = "3", isCompleted = true),
                Habit(id = 4, name = "4", desc = "4"),
                Habit(id = 5, name = "5", desc = "5"))
            if (habitsList.isEmpty()) {
                _uiState.value = HabitsUiState.Empty
            } else {
                _uiState.value = HabitsUiState.Success(habitsList)
            }
        } catch (e: Exception) {
            _uiState.value = HabitsUiState.Error("Ошибка ${e.message}")
        }
    }

    fun onItemClick(clickedHabit: Habit) {
        Log.d("OnItemClicked", "True")
        _uiState.update { currentState ->
            when (currentState) {
                is HabitsUiState.Success -> {
                    val updatedHabits = currentState.habits.map { habit ->
                        if (habit.id == clickedHabit.id)
                            habit.copy(isCompleted = !habit.isCompleted)
                        else
                            habit
                    }
                    HabitsUiState.Success(updatedHabits)
                }
                else -> currentState
            }
        }
        Log.d("IsCompleted", "${clickedHabit.isCompleted}")
    }
}
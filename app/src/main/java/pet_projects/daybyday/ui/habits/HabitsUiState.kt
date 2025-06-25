package pet_projects.daybyday.ui.habits

import pet_projects.daybyday.data.Habit

sealed interface HabitsUiState {
    data object Loading: HabitsUiState
    data object Empty: HabitsUiState
    data class Success(val habits: List<Habit>): HabitsUiState
    data class Error(val message: String): HabitsUiState
}
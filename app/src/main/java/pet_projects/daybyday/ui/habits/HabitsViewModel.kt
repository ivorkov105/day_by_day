package pet_projects.daybyday.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pet_projects.daybyday.repositories.HabitRepository
import pet_projects.daybyday.database.data.Habit
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _viewingHabitDetails = MutableStateFlow<Habit?>(null)
    val viewingHabitDetails: StateFlow<Habit?> = _viewingHabitDetails.asStateFlow()


    private val _habitTime = MutableStateFlow(LocalTime.now())
    val habitTime: StateFlow<LocalTime> = _habitTime.asStateFlow()

    fun onTimeSelected(hour: Int, minute: Int) {
        _habitTime.value = LocalTime.of(hour, minute)
    }

    private val _isAddDialogShown = MutableStateFlow(false)
    val isAddDialogShown: StateFlow<Boolean> = _isAddDialogShown.asStateFlow()

    private val _habitName = MutableStateFlow("")
    val habitName: StateFlow<String> = _habitName.asStateFlow()

    private val _habitDescription = MutableStateFlow("")
    val habitDescription: StateFlow<String> = _habitDescription.asStateFlow()

    private val _habitToDelete = MutableStateFlow<Habit?>(null)
    val habitToDelete: StateFlow<Habit?> = _habitToDelete.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HabitsUiState> = _selectedDate
        .flatMapLatest { date ->
            repository.getHabitsForDate(date)
                .map { habits ->
                    if (habits.isEmpty()) HabitsUiState.Empty else HabitsUiState.Success(habits)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HabitsUiState.Loading
        )

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onItemClick(habit: Habit) = viewModelScope.launch {
        _viewingHabitDetails.value = habit
    }

    fun toggleHabitCompletion(habit: Habit) = viewModelScope.launch {
        repository.update(habit.copy(isCompleted = !habit.isCompleted))
        _viewingHabitDetails.update { it?.copy(isCompleted = !it.isCompleted) }
    }

    fun dismissHabitDetails() {
        _viewingHabitDetails.value = null
    }

    fun showAddHabitDialog() {
        _isAddDialogShown.value = true
    }

    fun dismissAddHabitDialog() {
        _isAddDialogShown.value = false
        _habitName.value = ""
        _habitDescription.value = ""
        _habitTime.value = LocalTime.now()
    }

    fun onNameChange(newName: String) {
        _habitName.value = newName
    }

    fun onDescriptionChange(newDescription: String) {
        _habitDescription.value = newDescription
    }

    fun saveNewHabit() = viewModelScope.launch {
        if (_habitName.value.isBlank()) return@launch
        val newHabit = Habit(
            name = _habitName.value,
            desc = _habitDescription.value,
            date = _selectedDate.value,
            time = _habitTime.value
        )
        repository.insert(newHabit)
        dismissAddHabitDialog()
    }

    fun requestDeletion(habit: Habit) {
        _habitToDelete.value = habit
    }

    fun dismissDeletion() {
        _habitToDelete.value = null
    }

    fun confirmDeletion() = viewModelScope.launch {
        _habitToDelete.value?.let { habitToRemove ->
            repository.delete(habitToRemove)
            dismissDeletion()
        }
    }
}
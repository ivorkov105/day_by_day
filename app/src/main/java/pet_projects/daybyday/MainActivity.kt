package pet_projects.daybyday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import pet_projects.daybyday.ui.calendar.CalendarScreen
import pet_projects.daybyday.ui.habits.AddHabitDialogContent
import pet_projects.daybyday.ui.habits.HabitsViewModel
import pet_projects.daybyday.ui.theme.DayByDayTheme
import androidx.compose.ui.window.DialogProperties
import pet_projects.daybyday.ui.habits.HabitDetailsDialogContent
import pet_projects.daybyday.ui.habits.TimePickerDialog

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DayByDayTheme {
                val viewModel: HabitsViewModel = viewModel()

                val isAddDialogShown by viewModel.isAddDialogShown.collectAsStateWithLifecycle()
                val habitName by viewModel.habitName.collectAsStateWithLifecycle()
                val habitDescription by viewModel.habitDescription.collectAsStateWithLifecycle()

                val habitToDelete by viewModel.habitToDelete.collectAsStateWithLifecycle()

                val habitTime by viewModel.habitTime.collectAsStateWithLifecycle()
                var showTimePicker by remember { mutableStateOf(false) }

                val viewingHabit by viewModel.viewingHabitDetails.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = viewModel::showAddHabitDialog) {
                            Icon(Icons.Default.Add, "Добавить привычку")
                        }
                    }
                ) { innerPadding ->
                    CalendarScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }

                if (isAddDialogShown) {
                    Dialog(
                        onDismissRequest = viewModel::dismissAddHabitDialog,
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        Card(shape = RoundedCornerShape(16.dp)) {
                            AddHabitDialogContent(
                                name = habitName,
                                description = habitDescription,
                                onNameChange = viewModel::onNameChange,
                                onDescriptionChange = viewModel::onDescriptionChange,
                                onSaveClick = viewModel::saveNewHabit,
                                onCancelClick = viewModel::dismissAddHabitDialog,
                                time = habitTime,
                                onTimeClick = {
                                    showTimePicker = true
                                }
                            )
                        }
                    }
                }

                TimePickerDialog(
                    show = showTimePicker,
                    onDismiss = { showTimePicker = false },
                    onTimeSelected = { hour, minute ->
                        viewModel.onTimeSelected(hour, minute)
                    }
                )

                if (habitToDelete != null) {
                    AlertDialog(
                        onDismissRequest = viewModel::dismissDeletion,
                        title = { Text("Подтвердите удаление") },
                        text = { Text("Вы уверены, что хотите удалить привычку \"${habitToDelete?.name}\"?") },
                        confirmButton = { Button(onClick = viewModel::confirmDeletion) { Text("Удалить") } },
                        dismissButton = { Button(onClick = viewModel::dismissDeletion) { Text("Отмена") } }
                    )
                }

                if (viewingHabit != null) {
                    Dialog(onDismissRequest = viewModel::dismissHabitDetails) {
                        Card(shape = RoundedCornerShape(16.dp)) {
                            HabitDetailsDialogContent(
                                habit = viewingHabit!!,
                                onDismiss = viewModel::dismissHabitDetails,
                                onToggleComplete = { viewModel.toggleHabitCompletion(viewingHabit!!) },
                                onDelete = {
                                    viewModel.dismissHabitDetails()
                                    viewModel.requestDeletion(viewingHabit!!)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
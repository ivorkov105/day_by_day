package pet_projects.daybyday.ui.habits

import android.app.TimePickerDialog
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pet_projects.daybyday.database.data.Habit
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun HabitScreen(
    modifier: Modifier = Modifier,
    viewModel: HabitsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HabitsScreenContent(
        uiState = uiState,
        modifier = modifier,
        onItemClick = viewModel::onItemClick,
        onDeleteClick = viewModel::requestDeletion
    )
}

@Composable
fun HabitsScreenContent(
    uiState: HabitsUiState,
    modifier: Modifier = Modifier,
    onItemClick: (Habit) -> Unit,
    onDeleteClick: (Habit) -> Unit
) {
    when (uiState) {
        is HabitsUiState.Loading ->
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        is HabitsUiState.Success ->
            HabitList(
                habits = uiState.habits,
                onHabitClick = onItemClick,
                onDeleteClick = onDeleteClick,
                modifier = modifier
            )
        is HabitsUiState.Error ->
            Box(modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.message)
            }
        is HabitsUiState.Empty ->
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "На этот день привычек нет")
            }
    }
}

@Composable
fun HabitList(
    habits: List<Habit>,
    onHabitClick: (Habit) -> Unit,
    onDeleteClick: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        items(habits) { habit ->
            HabitItem(
                habit = habit,
                onItemClick = onHabitClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

private val HabitItemShape = RoundedCornerShape(12.dp)

@Composable
fun HabitItem(
    habit: Habit,
    onItemClick: (Habit) -> Unit,
    onDeleteClick: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (habit.isCompleted)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (habit.isCompleted) MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(HabitItemShape)
            .clickable { onItemClick(habit) }
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = habit.name, color = textColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (habit.desc.isNotBlank()) {
                Text(text = habit.desc, color = textColor, style = MaterialTheme.typography.bodyMedium)
            }
        }
        val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
        Text(
            text = "${habit.time.format(formatter)}",
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AddHabitDialogContent(
    name: String,
    description: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    time: LocalTime,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Новая привычка",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Время привычки:",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onTimeClick) {
                val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
                Text(text = time.format(formatter))
            }
        }
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End)
        {
            Button(
                onClick = onCancelClick
            ) {
                Text("Отмена")
            }
            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Button(
                onClick = onSaveClick
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    if (show) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = remember {
            TimePickerDialog(
                context,
                { _, selectedHour: Int, selectedMinute: Int ->
                    onTimeSelected(selectedHour, selectedMinute)
                },
                hour,
                minute,
                true
            )
        }

        LaunchedEffect(Unit) {
            timePickerDialog.show()
        }

        timePickerDialog.setOnDismissListener {
            onDismiss()
        }
    }
}

@Composable
fun HabitDetailsDialogContent(
    habit: Habit,
    onDismiss: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.headlineSmall
            )
            val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
            Text(
                text = habit.time.format(formatter),
                style = MaterialTheme.typography.titleMedium
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (habit.desc.isNotBlank()) {
            Text(
                text = habit.desc,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onToggleComplete) {
                Text(if (habit.isCompleted) "Не выполнено" else "Выполнено")
            }

            OutlinedButton(onClick = onDelete) {
                Text("Удалить")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onDismiss) {
            Text("Закрыть")
        }
    }
}
package pet_projects.daybyday.ui.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import pet_projects.daybyday.data.Habit
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pet_projects.daybyday.ui.theme.DayByDayTheme

@Composable
fun HabitScreen(
    modifier: Modifier = Modifier,
    viewModel: HabitsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is HabitsUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is HabitsUiState.Success -> {
            HabitList(
                habits = state.habits,
                onHabitClick = { habit ->
                    viewModel.onItemClick(habit)
                },
                modifier = modifier
            )
        }
        is HabitsUiState.Error -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message)
                    Button(
                        onClick = {
                        viewModel.loadHabits()
                    }) {
                        Text("Попробовать снова")
                    }
                }
            }
        }

        is HabitsUiState.Empty -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Добавьте привычку!")

            }
        }
    }
}

@Composable
fun HabitList(
    habits: List<Habit>,
    onHabitClick: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        items(habits) { habit ->
            HabitItem(
                habit = habit,
                onItemClick = onHabitClick
            )
        }
    }
}

private val HabitItemShape = RoundedCornerShape(8.dp)

@Composable
fun HabitItem(
    habit: Habit,
    onItemClick: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (habit.isCompleted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }
    val textColor = if (habit.isCompleted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onTertiaryContainer
    }

    Text(
        text = "Привычка #${habit.id}, \"${habit.name}\", описание: ${habit.desc}, статус: ${habit.isCompleted}",
        modifier = modifier
            .fillMaxWidth()
            .clip(HabitItemShape)
            .clickable {onItemClick(habit)}
            .background(color = backgroundColor)
            .padding(8.dp),
        color = textColor,
        maxLines = 2)
}

@Preview(showBackground = true, name = "Обычная привычка")
@Composable
private fun HabitItemPreview() {
    DayByDayTheme {
        val habit = Habit(
            id = 1,
            name = "Прочитать 20 страниц книги",
            desc = "Некое подробное описание",
            isCompleted = false
        )
        HabitItem(
            habit = habit,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HabitListPreview() {
    DayByDayTheme {
        val habits = listOf(Habit(id = 1, name = "1", desc = "1"),
            Habit(id = 2, name = "2", desc = "2"),
            Habit(id = 3, name = "3", desc = "3", isCompleted = true),
            Habit(id = 4, name = "4", desc = "4"),
            Habit(id = 5, name = "5", desc = "5"))
            HabitList(
                habits = habits,
                onHabitClick = {}
            )
    }
}

@Preview(showBackground = true, name = "Состояние загрузки")
@Composable
private fun HabitScreenPreview_Loading() {
    DayByDayTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true, name = "Состояние ошибки")
@Composable
private fun HabitScreenPreview_Error() {
    DayByDayTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Не удалось загрузить привычки")
        }
    }
}

@Preview(showBackground = true, name = "Пустой список")
@Composable
private fun HabitScreenPreview_Empty() {
    DayByDayTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "У вас пока нет привычек. Добавьте первую!")
        }
    }
}
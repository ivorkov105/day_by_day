package pet_projects.daybyday.ui.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pet_projects.daybyday.ui.theme.DayByDayTheme

@Composable
fun HabitScreen(
    modifier: Modifier = Modifier,
    viewModel: HabitsViewModel = viewModel()
) {
    val habits by viewModel.uiState.collectAsStateWithLifecycle()
    HabitList(
        habits,
        { habit ->
            viewModel.onItemClick(habit)
        },
        modifier)
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

@Composable
fun HabitItem(
    habit: Habit,
    onItemClick: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Привычка #${habit.id}, \"${habit.name}\", описание: ${habit.desc}, статус: ${habit.isCompleted}",
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {onItemClick(habit)}
            .padding(8.dp),
        color = MaterialTheme.colorScheme.onTertiaryContainer)
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
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HabitListPreview() {
    val habits = listOf(Habit(id = 1, name = "1", desc = "1"),
        Habit(id = 2, name = "2", desc = "2"),
        Habit(id = 3, name = "3", desc = "3", isCompleted = true),
        Habit(id = 4, name = "4", desc = "4"),
        Habit(id = 5, name = "5", desc = "5"))
    LazyColumn(
        Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(habits) { habit ->
            HabitItem(
                habit = habit,
                {},
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
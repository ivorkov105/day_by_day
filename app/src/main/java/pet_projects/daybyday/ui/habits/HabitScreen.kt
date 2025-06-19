package pet_projects.daybyday.ui.habits

import android.R
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pet_projects.daybyday.ui.theme.DayByDayTheme

@Composable
fun HabitItem(
    habit: Habit,
    modifier: Modifier
) {
    Text(text = "Привычка #${habit.id}, \"${habit.name}\", описание: ${habit.desc}, статус: ${habit.isCompleted}",
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.holo_purple),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        color = colorResource(R.color.white))
}

@Composable
fun HabitScreen(
    modifier: Modifier,
    viewModel: HabitsViewModel = viewModel()
) {
    val habits by viewModel.uiState.collectAsStateWithLifecycle()
    HabitList(habits, modifier)
}

@Composable
fun HabitList(habits: List<Habit>, modifier: Modifier) {
    LazyColumn(
        modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        items(habits) { habit ->
            HabitItem(habit = habit, modifier = modifier)
        }
    }
}

@Preview(showBackground = true, name = "Обычная привычка")
@Composable
private fun HabitItemPreview() {
    DayByDayTheme {
        val habit = Habit(id = 1, name = "Прочитать 20 страниц книги", desc = "Некое подробное описание",isCompleted = false)
        HabitItem(habit = habit, modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.holo_purple),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HabitListPreview() {
    val viewModel: HabitsViewModel = viewModel()
    val habits by viewModel.uiState.collectAsStateWithLifecycle()
    LazyColumn(
        Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(habits) { habit ->
            HabitItem(habit = habit, modifier = Modifier.padding(8.dp))
        }
    }
}
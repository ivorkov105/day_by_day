package pet_projects.daybyday.ui.calendar

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import pet_projects.daybyday.ui.habits.HabitScreen
import pet_projects.daybyday.ui.habits.HabitsViewModel
import java.time.LocalDate
import android.view.ViewGroup
import pet_projects.daybyday.R

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: HabitsViewModel
) {
    Column(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                CalendarView(ContextThemeWrapper(context, R.style.CustomCalendar)).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                      },
            modifier = Modifier.wrapContentHeight(),
            update = { calendarView ->
                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    viewModel.onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                }
            }
        )
        HabitScreen(viewModel = viewModel)
    }
}
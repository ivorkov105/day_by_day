package pet_projects.daybyday

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import pet_projects.daybyday.ui.habits.HabitScreen
import pet_projects.daybyday.ui.theme.DayByDayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DayByDayTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize())
                { innerPadding ->
                    Log.i("InnerPadding", innerPadding.toString())
                    HabitScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
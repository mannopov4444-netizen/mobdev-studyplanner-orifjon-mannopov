package uz.ttpu.studyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.room.Room
import uz.ttpu.studyplanner.data.AppDatabase
import uz.ttpu.studyplanner.data.TaskRepository
import uz.ttpu.studyplanner.ui.TaskListScreen
import uz.ttpu.studyplanner.ui.TaskViewModel
import uz.ttpu.studyplanner.ui.theme.StudyPlannerTheme

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "study_planner_db"
        ).build()
    }

    private val repository by lazy {
        TaskRepository(database.taskDao())
    }

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.provideFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyPlannerTheme {
                TaskListScreen(viewModel = viewModel)
            }
        }
    }
}

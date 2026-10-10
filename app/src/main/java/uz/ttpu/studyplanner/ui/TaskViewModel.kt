package uz.ttpu.studyplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.ttpu.studyplanner.data.TaskEntity
import uz.ttpu.studyplanner.data.TaskRepository

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val uiState: StateFlow<List<TaskEntity>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addTask(
        title: String,
        description: String,
        subject: String,
        dueDate: Long,
        priority: Int
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                description = description,
                subject = subject,
                dueDate = dueDate,
                priority = priority,
                isCompleted = false
            )
            repository.insertTask(task)
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    companion object {
        fun provideFactory(repository: TaskRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(repository) as T
                }
            }
    }
}

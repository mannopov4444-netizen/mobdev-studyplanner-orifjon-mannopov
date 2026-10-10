package uz.ttpu.studyplanner.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.ttpu.studyplanner.data.TaskEntity
import uz.ttpu.studyplanner.data.TaskRepository
import uz.ttpu.studyplanner.worker.NotificationWorker
import java.util.concurrent.TimeUnit

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val filterPriority = MutableStateFlow<Int?>(null) // null = All

    val uiState: StateFlow<List<TaskEntity>> = combine(
        repository.allTasks,
        searchQuery,
        filterPriority
    ) { tasks, query, priority ->
        tasks.filter { task ->
            val matchesQuery = query.isBlank() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.subject.contains(query, ignoreCase = true)
            val matchesPriority = priority == null || task.priority == priority
            matchesQuery && matchesPriority
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    fun onPriorityFilterChanged(priority: Int?) {
        filterPriority.value = priority
    }

    fun addTask(
        title: String,
        description: String,
        subject: String,
        dueDate: Long,
        priority: Int,
        context: Context? = null
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

            context?.let { scheduleNotification(it, title, subject) }
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

    private fun scheduleNotification(context: Context, title: String, subject: String) {
        val inputData = Data.Builder()
            .putString(NotificationWorker.KEY_TITLE, title)
            .putString(NotificationWorker.KEY_SUBJECT, subject)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(inputData)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
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

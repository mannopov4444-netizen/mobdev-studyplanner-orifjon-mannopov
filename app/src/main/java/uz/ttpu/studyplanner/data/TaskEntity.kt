package uz.ttpu.studyplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val subject: String,
    val dueDate: Long,
    val priority: Int,
    val isCompleted: Boolean = false
)

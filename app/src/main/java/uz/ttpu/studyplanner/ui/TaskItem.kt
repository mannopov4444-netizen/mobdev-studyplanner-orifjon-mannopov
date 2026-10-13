package uz.ttpu.studyplanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.ttpu.studyplanner.data.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    task: TaskEntity,
    onTaskClick: (TaskEntity) -> Unit = {},
    onTaskCompletionToggle: (TaskEntity, Boolean) -> Unit,
    onTaskDelete: (TaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val (priorityLabel, priorityColor) = when (task.priority) {
        1 -> "Low" to Color(0xFF2E7D32)
        2 -> "Medium" to Color(0xFFEF6C00)
        3 -> "High" to Color(0xFFC62828)
        else -> "Low" to Color(0xFF2E7D32)
    }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(task.dueDate))

    // Soft Lavender / Purple color for Subject Tag
    val lavenderBg = Color(0xFFEDE7F6)
    val lavenderText = Color(0xFF4A148C)

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onTaskClick(task) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Left border priority indicator line
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(priorityColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Header Row: Checkbox, Title (1 line max), Delete Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { isChecked ->
                            onTaskCompletionToggle(task, isChecked)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        else
                            MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { onTaskDelete(task) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Description (2 lines max with Ellipsis for uniform card height)
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Metadata Row: Subject Tag + Priority Tag on Left, Due Date on Right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 42.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        if (task.subject.isNotBlank()) {
                            Surface(
                                color = lavenderBg,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = task.subject,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = lavenderText,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Surface(
                            color = priorityColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = priorityLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = priorityColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Horizontal Due Date in Bottom Right
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Due Date",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Due: $formattedDate",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggleCompletion: (TaskEntity) -> Unit,
    onDelete: (TaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    TaskItem(
        task = task,
        onTaskClick = {},
        onTaskCompletionToggle = { entity, _ -> onToggleCompletion(entity) },
        onTaskDelete = onDelete,
        modifier = modifier
    )
}

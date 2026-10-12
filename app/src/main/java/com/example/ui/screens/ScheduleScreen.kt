package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BrainstormIdea
import com.example.data.model.ScheduleTask
import com.example.engine.ReminderManager
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicDebossedBox
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.components.UndulatingInkUnderline
import com.example.ui.theme.FaintInk
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperCard
import com.example.ui.theme.PaperDebossed
import com.example.ui.theme.PaperSurface
import com.example.ui.theme.SwotStrengthTint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ScheduleScreen(
    idea: BrainstormIdea?,
    tasks: List<ScheduleTask>,
    onToggleCompleted: (taskId: Long, isCompleted: Boolean) -> Unit,
    onDeleteTask: (taskId: Long) -> Unit,
    onRecalculateSchedule: () -> Unit,
    onAddTask: (title: String, description: String, estimatedHours: Float, phase: String, effort: String) -> Unit,
    onExportToCloud: () -> Unit,
    onSetReminder: (taskId: Long, triggerAtMillis: Long) -> Unit = { _, _ -> },
    onClearReminder: (taskId: Long) -> Unit = {},
    onOpenCalendar: (taskId: Long, startMillis: Long, endMillis: Long) -> Unit = { _, _, _ -> },
    onSyncToCalendar: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var reminderTaskToEdit by remember { mutableStateOf<ScheduleTask?>(null) }
    var taskToDelete by remember { mutableStateOf<ScheduleTask?>(null) }

    val completedCount = remember(tasks) { tasks.count { it.isCompleted } }
    val totalCount = tasks.size
    val totalHours = remember(tasks) { tasks.sumOf { it.estimatedHours.toDouble() } }
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f

    val sarcasticStatus = when {
        totalCount == 0 -> "Your hit list is completely empty. Don't stare at me, tap 'Plan for Me' or add your first task."
        completedCount == totalCount -> "All $totalCount tasks done! Did you actually build it, or just click the boxes to feel good? Either way, go celebrate."
        completedCount == 0 -> "0 of $totalCount completed. Stop tweaking settings and go build something."
        else -> "$completedCount of $totalCount done. Look at you actually making progress! Don't ruin it by redesigning the whole thing now."
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBase)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Clean Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "HIT LIST",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                        Text(
                            text = "5-step sustainable realization roadmap",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = MutedInk
                        )
                    }

                    // Action buttons in header
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        NeumorphicButton(
                            onClick = onRecalculateSchedule,
                            modifier = Modifier.testTag("recalculate_schedule_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = 8.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoMode,
                                    contentDescription = "Plan for Me",
                                    tint = InkBlack,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Plan for Me",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }

                        NeumorphicButton(
                            onClick = onSyncToCalendar,
                            modifier = Modifier.testTag("sync_calendar_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = 8.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = "Sync Calendar",
                                    tint = InkBlack,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Calendar",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }

                        NeumorphicButton(
                            onClick = onExportToCloud,
                            modifier = Modifier.testTag("export_to_cloud_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = 8.dp
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Export",
                                tint = InkBlack,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )
            }

            // Simplified Progress Card
            item {
                NeumorphicRaisedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("schedule_metrics_card"),
                    shape = RoundedCornerShape(14.dp),
                    backgroundColor = PaperSurface,
                    elevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Progress: $completedCount / $totalCount done",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = InkBlack
                            )

                            if (totalHours > 0) {
                                Text(
                                    text = String.format(Locale.US, "Est: %.1f hrs total", totalHours),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = GhostInk
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Progress bar
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = InkBlack,
                            trackColor = PaperDebossed,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Loghy's witty status
                        Text(
                            text = sarcasticStatus,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.SansSerif,
                                lineHeight = 18.sp
                            ),
                            color = MutedInk
                        )
                    }
                }
            }

            // Add Task Button
            item {
                NeumorphicButton(
                    onClick = { showAddTaskDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_task_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 10.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            tint = InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Add to Hit List",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }
            }

            // Task Items
            if (tasks.isEmpty()) {
                item {
                    NeumorphicDebossedBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = SwotStrengthTint,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hit list is empty",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = InkBlack,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap 'Plan for Me' or add a task to begin your roadmap.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = MutedInk,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { onToggleCompleted(task.id, !task.isCompleted) },
                        onDelete = { taskToDelete = task },
                        onOpenReminder = { reminderTaskToEdit = task }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        if (showAddTaskDialog) {
            SimpleAddTaskDialog(
                onDismiss = { showAddTaskDialog = false },
                onAddTask = { title, desc, hours ->
                    onAddTask(title, desc, hours, "General", "Medium")
                    showAddTaskDialog = false
                }
            )
        }

        val activeReminderTask = reminderTaskToEdit
        if (activeReminderTask != null) {
            TaskReminderDialog(
                task = activeReminderTask,
                onDismiss = { reminderTaskToEdit = null },
                onSetReminder = { triggerAtMillis ->
                    onSetReminder(activeReminderTask.id, triggerAtMillis)
                    reminderTaskToEdit = null
                },
                onClearReminder = {
                    onClearReminder(activeReminderTask.id)
                    reminderTaskToEdit = null
                },
                onAddToCalendar = { startMillis, endMillis ->
                    onOpenCalendar(activeReminderTask.id, startMillis, endMillis)
                    reminderTaskToEdit = null
                }
            )
        }

        val activeDeleteTask = taskToDelete
        if (activeDeleteTask != null) {
            Dialog(onDismissRequest = { taskToDelete = null }) {
                NeumorphicRaisedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = PaperSurface,
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Delete Hit List Task?",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            color = InkBlack
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Remove '${activeDeleteTask.title}' from your roadmap?",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.SansSerif,
                                lineHeight = 18.sp
                            ),
                            color = MutedInk
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            NeumorphicButton(
                                onClick = { taskToDelete = null },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = 8.dp
                            ) {
                                Text(
                                    text = "Cancel",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = InkBlack
                                )
                            }

                            NeumorphicButton(
                                onClick = {
                                    val idToDelete = activeDeleteTask.id
                                    taskToDelete = null
                                    onDeleteTask(idToDelete)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                                contentPadding = 8.dp
                            ) {
                                Text(
                                    text = "Delete",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: ScheduleTask,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onOpenReminder: () -> Unit
) {
    NeumorphicRaisedBox(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("task_item_${task.id}"),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = if (task.isCompleted) PaperCard else PaperSurface,
        elevation = if (task.isCompleted) 1.dp else 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tactile Checkbox with 44dp interactive touch target
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onToggle() }
                    .testTag("task_checkbox_${task.id}"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (task.isCompleted) InkBlack else PaperDebossed)
                        .border(1.dp, if (task.isCompleted) InkBlack else FaintInk, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done",
                            tint = PaperSurface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Title and Description
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (task.isCompleted) GhostInk else InkBlack
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.SansSerif,
                            lineHeight = 16.sp
                        ),
                        color = if (task.isCompleted) GhostInk else MutedInk
                    )
                }

                if (task.reminderTimestamp != null) {
                    val reminderText = remember(task.reminderTimestamp) {
                        val sdf = SimpleDateFormat("EEE, MMM d · h:mm a", Locale.getDefault())
                        sdf.format(Date(task.reminderTimestamp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PaperDebossed)
                            .clickable { onOpenReminder() }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                            .testTag("task_reminder_badge_${task.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Active Reminder",
                            tint = InkBlack,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = reminderText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Time estimate pill
            if (task.estimatedHours > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PaperDebossed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = String.format(Locale.US, "%.1fh", task.estimatedHours),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp
                        ),
                        color = if (task.isCompleted) GhostInk else InkBlack
                    )
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            // Reminder Bell Button with 44dp interactive touch target
            IconButton(
                onClick = onOpenReminder,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("reminder_task_${task.id}")
            ) {
                Icon(
                    imageVector = if (task.reminderTimestamp != null) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                    contentDescription = "Set Reminder",
                    tint = if (task.reminderTimestamp != null) InkBlack else GhostInk,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Delete action with 44dp interactive touch target
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("delete_task_${task.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = GhostInk,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun SimpleAddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (title: String, desc: String, hours: Float) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var selectedHours by remember { mutableFloatStateOf(2.0f) }

    val quickHours = listOf(0.5f, 1.0f, 2.0f, 4.0f, 8.0f)

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = PaperSurface,
            elevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                Text(
                    text = "ADD TO HIT LIST",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = InkBlack
                )

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 2.dp
                )

                Text(
                    text = "What needs to be done?",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(4.dp))

                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .testTag("add_task_dialog_title_input"),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            color = InkBlack,
                            fontFamily = FontFamily.SansSerif
                        ),
                        cursorBrush = SolidColor(InkBlack),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) {
                                Text(
                                    text = "e.g. Build ugly working prototype",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = MutedInk
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Quick note or detail (optional)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(4.dp))

                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    BasicTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .testTag("add_task_dialog_desc_input"),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            color = InkBlack,
                            fontFamily = FontFamily.SansSerif
                        ),
                        cursorBrush = SolidColor(InkBlack),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (desc.isEmpty()) {
                                Text(
                                    text = "e.g. Keep it barebones, test with 2 users",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = MutedInk
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Estimated Time",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Time Selection Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickHours.forEach { hrs ->
                        val isSelected = selectedHours == hrs
                        val label = if (hrs < 1.0f) "30m" else if (hrs == 8.0f) "1 Day" else "${hrs.toInt()}h"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) InkBlack else PaperCard)
                                .clickable { selectedHours = hrs }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = if (isSelected) PaperSurface else InkBlack
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeumorphicButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = PaperCard,
                        contentPadding = 8.dp
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = GhostInk
                        )
                    }

                    NeumorphicButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                onAddTask(title.trim(), desc.trim(), selectedHours)
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("save_task_button"),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = 8.dp
                    ) {
                        Text(
                            text = "Add to Hit List",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskReminderDialog(
    task: ScheduleTask,
    onDismiss: () -> Unit,
    onSetReminder: (triggerAtMillis: Long) -> Unit,
    onClearReminder: () -> Unit,
    onAddToCalendar: (startMillis: Long, endMillis: Long) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Permission result handled */ }

    var selectedPresetIndex by remember { mutableIntStateOf(1) } // Default: Tomorrow 9 AM
    var selectedHour by remember { mutableIntStateOf(9) }
    var selectedDayOffset by remember { mutableIntStateOf(1) } // 0=Today, 1=Tomorrow, 2=In 2 Days, 7=In 1 Week

    val computedTriggerTime = remember(selectedDayOffset, selectedHour) {
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, selectedDayOffset)
            set(Calendar.HOUR_OF_DAY, selectedHour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (selectedDayOffset == 0 && timeInMillis <= System.currentTimeMillis()) {
                timeInMillis = System.currentTimeMillis() + 30 * 60 * 1000L
            }
        }.timeInMillis
    }

    val formattedTarget = remember(computedTriggerTime) {
        val sdf = SimpleDateFormat("EEEE, MMM d 'at' h:mm a", Locale.getDefault())
        sdf.format(Date(computedTriggerTime))
    }

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("task_reminder_dialog"),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = PaperSurface,
            elevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = InkBlack,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WORK REMINDER & CALENDAR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = InkBlack
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = InkBlack
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = MutedInk,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                UndulatingInkUnderline(modifier = Modifier.fillMaxWidth(), heightDp = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Quick Presets
                Text(
                    text = "Quick Presets",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(6.dp))

                val presets = listOf(
                    Triple("+1 Hour", 0, -1),
                    Triple("Tomorrow 9 AM", 1, 9),
                    Triple("Tomorrow 2 PM", 1, 14),
                    Triple("In 2 Days", 2, 9)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.forEachIndexed { index, preset ->
                        val isSelected = selectedPresetIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) InkBlack else PaperCard)
                                .clickable {
                                    selectedPresetIndex = index
                                    if (preset.third == -1) {
                                        selectedDayOffset = 0
                                        val cal = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }
                                        selectedHour = cal.get(Calendar.HOUR_OF_DAY)
                                    } else {
                                        selectedDayOffset = preset.second
                                        selectedHour = preset.third
                                    }
                                }
                                .padding(vertical = 8.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = preset.first,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = if (isSelected) PaperSurface else InkBlack,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Day Selection
                Text(
                    text = "Schedule Day",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(6.dp))

                val days = listOf(
                    "Today" to 0,
                    "Tomorrow" to 1,
                    "+2 Days" to 2,
                    "+3 Days" to 3,
                    "+1 Wk" to 7
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    days.forEach { (label, offset) ->
                        val isSelected = selectedDayOffset == offset
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) InkBlack else PaperDebossed)
                                .clickable {
                                    selectedDayOffset = offset
                                    selectedPresetIndex = -1
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = if (isSelected) PaperSurface else InkBlack
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Target Preview Box
                NeumorphicDebossedBox(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = PaperCard
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = InkBlack,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formattedTarget,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons: Notification & Calendar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeumorphicButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !ReminderManager.hasNotificationPermission(context)) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            onSetReminder(computedTriggerTime)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("set_notification_confirm_button"),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = 8.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = InkBlack,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Notify Me",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = InkBlack
                            )
                        }
                    }

                    NeumorphicButton(
                        onClick = {
                            val durationMillis = (task.estimatedHours * 3600000).toLong().coerceAtLeast(1800000L)
                            onAddToCalendar(computedTriggerTime, computedTriggerTime + durationMillis)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("add_to_calendar_button"),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = 8.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                tint = InkBlack,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "To Calendar",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                color = InkBlack
                            )
                        }
                    }
                }

                if (task.reminderTimestamp != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    NeumorphicButton(
                        onClick = onClearReminder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("clear_reminder_button"),
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = PaperCard,
                        contentPadding = 6.dp
                    ) {
                        Text(
                            text = "Clear Existing Reminder",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = GhostInk
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                NeumorphicButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = PaperCard,
                    contentPadding = 6.dp
                ) {
                    Text(
                        text = "Dismiss",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = GhostInk
                    )
                }
            }
        }
    }
}

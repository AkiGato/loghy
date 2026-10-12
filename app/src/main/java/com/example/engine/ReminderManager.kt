package com.example.engine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import com.example.data.model.ScheduleTask

object ReminderManager {

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun scheduleTaskReminder(context: Context, task: ScheduleTask, triggerAtMillis: Long): Boolean {
        ReminderBroadcastReceiver.ensureChannelCreated(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return false

        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderBroadcastReceiver.EXTRA_TASK_ID, task.id)
            putExtra(ReminderBroadcastReceiver.EXTRA_TASK_TITLE, "Focus: ${task.title}")
            val desc = if (task.description.isNotBlank()) {
                "${task.description} (${task.phase})"
            } else {
                "Time for your scheduled ${task.phase} session."
            }
            putExtra(ReminderBroadcastReceiver.EXTRA_TASK_DESC, desc)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Inexact on purpose. SCHEDULE_EXACT_ALARM is a policy-restricted permission on
        // Play: Google grants it essentially only to alarm clocks and calendar apps, and
        // a focus nudge is neither. setAndAllowWhileIdle still fires through Doze, just
        // within a window rather than to the second, which is all a work reminder needs.
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
            true
        } catch (_: SecurityException) {
            false
        }
    }

    fun cancelTaskReminder(context: Context, taskId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun createCalendarEventIntent(
        task: ScheduleTask,
        startMillis: Long,
        endMillis: Long,
        ideaTitle: String? = null
    ): Intent {
        val titlePrefix = if (!ideaTitle.isNullOrBlank()) "[$ideaTitle] " else ""
        return Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "$titlePrefix${task.title}")
            val fullDesc = buildString {
                if (task.description.isNotBlank()) appendLine(task.description)
                appendLine()
                appendLine("Phase: ${task.phase}")
                appendLine("Effort: ${task.effortLevel} · ${task.estimatedHours} hrs")
                if (!ideaTitle.isNullOrBlank()) appendLine("Project: $ideaTitle")
                appendLine("Scheduled via Loghy")
            }
            putExtra(CalendarContract.Events.DESCRIPTION, fullDesc)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            putExtra(CalendarContract.Events.HAS_ALARM, 1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun createRoadmapCalendarIntent(
        roadmapTitle: String,
        summaryDescription: String,
        startMillis: Long
    ): Intent {
        return Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, roadmapTitle)
            putExtra(CalendarContract.Events.DESCRIPTION, summaryDescription)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + 3600000L)
            putExtra(CalendarContract.Events.HAS_ALARM, 1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}

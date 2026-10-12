package com.example.engine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

/**
 * BroadcastReceiver triggered by AlarmManager to post gentle, distraction-free
 * work notifications for hit-list tasks and creative milestones.
 */
class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, 0L)
        val taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Work Reminder"
        val taskDesc = intent.getStringExtra(EXTRA_TASK_DESC) ?: "Time for your scheduled deep work session."

        ensureChannelCreated(context)

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to_tab", "HIT_LIST")
            putExtra("selected_task_id", taskId)
        }

        val pendingTapIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(taskTitle)
            .setContentText(taskDesc)
            .setStyle(NotificationCompat.BigTextStyle().bigText(taskDesc))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingTapIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(taskId.toInt(), notification)
        } catch (_: SecurityException) {
            // Gracefully ignore if notification permission is revoked
        }
    }

    companion object {
        const val CHANNEL_ID = "loghy_work_reminders"
        const val CHANNEL_NAME = "Work & Calendar Reminders"
        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TASK_TITLE = "extra_task_title"
        const val EXTRA_TASK_DESC = "extra_task_desc"

        fun ensureChannelCreated(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                val existingChannel = notificationManager?.getNotificationChannel(CHANNEL_ID)
                if (existingChannel == null) {
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = "Gentle notifications for scheduled tasks and creative focus sessions"
                        enableVibration(true)
                    }
                    notificationManager?.createNotificationChannel(channel)
                }
            }
        }
    }
}

package de.malteharms.check.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import de.malteharms.check.data.NOTIFICATION_ID
import de.malteharms.check.data.NOTIFICATION_MESSAGE
import de.malteharms.check.data.NOTIFICATION_TITLE
import de.malteharms.check.data.notification.dataclasses.NotificationResult
import de.malteharms.check.data.notification.dataclasses.NotificationState
import de.malteharms.check.data.broadcastReceiver.ReminderNotificationReceiver
import de.malteharms.check.data.notification.dataclasses.AlarmItem
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.domain.AlarmScheduler
import java.time.ZoneId
import kotlin.random.Random

class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    companion object {
        private val TAG: String? = AndroidAlarmScheduler::class.simpleName
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(notificationId: Int?, item: AlarmItem): NotificationResult {
        val nId: Int = notificationId ?: Random.nextInt(1, Int.MAX_VALUE)

        val intent: Intent = when (item.channel) {
            NotificationChannel.REMINDER -> Intent(context, ReminderNotificationReceiver::class.java)
                .putExtra(NOTIFICATION_TITLE, item.title)
                .putExtra(NOTIFICATION_MESSAGE, item.message)
                .putExtra(NOTIFICATION_ID, nId)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, nId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            pendingIntent
        )

        Log.i(TAG, "Scheduled notification with ID $nId at ${item.time} for title ${item.title}")
        return NotificationResult(NotificationState.SUCCESS, nId)
    }

    override fun cancel(nId: Int): NotificationResult {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context, nId,
                Intent(context, ReminderNotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        Log.i(TAG, "Canceled notification with ID $nId from operating system")
        return NotificationResult(NotificationState.SUCCESS, nId)
    }

}
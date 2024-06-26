package de.malteharms.check.pages.reminder.domain

import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.NotificationItem
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderNotificationInterval
import java.time.LocalDateTime


sealed interface ReminderEvent {

    data object SaveItem: ReminderEvent
    data class UpdateItem(val itemToUpdate: ReminderItem): ReminderEvent
    data class RemoveItem(val item: ReminderItem): ReminderEvent

    data object MoveFromOrToDetailsScreen: ReminderEvent

    data class SetTitle(val title: String): ReminderEvent
    data class SetDueDate(val dueDate: LocalDateTime): ReminderEvent

    data class SetCategory(val category: ReminderCategory): ReminderEvent

    data class AddDummyNotification(val value: Int, val interval: ReminderNotificationInterval): ReminderEvent
    data class RemoveNotification(val notification: NotificationItem): ReminderEvent

    data object ShowNewDialog: ReminderEvent
    data class ShowEditDialog(val item: ReminderItem): ReminderEvent

    data object HideDialog: ReminderEvent
    data class AddOrRemoveFilterCategory(val filter: ReminderCategory): ReminderEvent
}

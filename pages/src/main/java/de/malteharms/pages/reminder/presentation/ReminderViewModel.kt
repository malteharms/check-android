package de.malteharms.pages.reminder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.database.CheckDao
import de.malteharms.pages.reminder.data.ReminderState
import de.malteharms.pages.reminder.domain.ReminderEvent
import de.malteharms.database.tables.NotificationChannel
import de.malteharms.database.tables.NotificationItem
import de.malteharms.notification.data.NotificationHandler.Companion.scheduleNotification
import de.malteharms.notification.domain.AlarmScheduler
import de.malteharms.utils.model.DateExt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class ReminderViewModel(
    private val dao: CheckDao,
    private val notificationScheduler: AlarmScheduler
): ViewModel() {

    companion object {
        private val TAG: String? = ReminderViewModel::class.simpleName
    }

    private val _reminderFilter: MutableStateFlow<List<de.malteharms.database.tables.ReminderCategory>> = MutableStateFlow(
        emptyList()
    )

    private val _allReminderItems = _reminderFilter
        .flatMapLatest { filterList ->
            when (filterList.isEmpty()) {
                true -> dao.getAllReminderItems()
                false -> dao.getFilteredReminderItems(filterList)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _reminderItems = _reminderFilter
        .flatMapLatest { filterList ->
            when (filterList.isEmpty()) {
                true -> dao.getAllReminderItemsLimited(limit = 5)
                false -> dao.getFilteredReminderItemsLimited(filterList, limit = 5)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _reminderState = MutableStateFlow(ReminderState())

    val state = combine(
        _reminderState,
        _reminderFilter,
        _reminderItems,
        _allReminderItems
    ) { state, filter, items, allItems ->
        state.copy(
            items = items,
            allItems = allItems,
            filter = filter
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReminderState())

    fun onEvent(event: ReminderEvent) {
        when (event) {

            ReminderEvent.ShowNewDialog -> {
                Log.i(TAG, "Open 'new reminder' sheet")
                resetState()

                _reminderState.update { it.copy(
                    isAddingItem = true
                ) }
            }

            is ReminderEvent.ShowEditDialog -> {
                Log.i(TAG, "Open 'edit reminder' sheet")

                viewModelScope.launch {
                    _reminderState.update { it.copy(
                        title = event.item.title,
                        dueDate = event.item.dueDate,
                        category = event.item.category,
                        isEditingItem = true,
                        notifications = dao.getNotificationsForConnectedItem(
                            channel = NotificationChannel.REMINDER,
                            itemId = event.item.id
                        )
                    ) }
                }
            }

            ReminderEvent.HideDialog -> {
                Log.i(TAG, "Hide add / edit reminder sheet")
                resetState()
            }

            ReminderEvent.SaveItem -> {
                val title = state.value.title.trim()
                val dueDate = state.value.dueDate
                val category = state.value.category

                val notifications = state.value.newNotifications

                if (title.isBlank()) {
                    Log.w(TAG, "Cannot save reminder item with due date $dueDate, because the title is empty!")
                    return
                }

                val now = DateExt.now()

                val newReminderItem = de.malteharms.database.tables.ReminderItem(
                    title = title,
                    dueDate = dueDate,
                    category = category,
                    lastUpdate = now,
                    creationDate = now
                )

                viewModelScope.launch {
                    // add reminder item into database
                    val newItemId: Long = dao.insertReminderItem(newReminderItem)
                    Log.i(TAG, "Inserted reminder $title with id $newItemId")

                    // add notifications to database and schedule them
                    notifications.forEach { reminderNotification ->
                        val notification: NotificationItem? = scheduleNotification(
                            alarmScheduler = notificationScheduler,
                            type = NotificationChannel.REMINDER,
                            connectedItem = newReminderItem,
                            notificationDate = reminderNotification.notificationDate
                        )

                        if (notification != null) {
                            dao.insertNotification(notification)
                        }
                    }
                }

                resetState()
            }

            is ReminderEvent.UpdateItem -> {
                val title: String = state.value.title
                val dueDate: DateExt = state.value.dueDate
                val category: de.malteharms.database.tables.ReminderCategory = state.value.category

                val notifications = state.value.notifications
                val notificationsToRemove = state.value.notificationsToDelete

                if (title.isBlank()) {
                    Log.w(TAG, "Cannot update reminder item with due date $dueDate, because the title is empty!")
                    return
                }

                val updatedReminderItem = de.malteharms.database.tables.ReminderItem(
                    id = event.itemToUpdate.id,
                    title = title,
                    dueDate = dueDate,
                    category = category,
                    birthdayRelation = event.itemToUpdate.birthdayRelation,
                    creationDate = event.itemToUpdate.creationDate,
                    lastUpdate = DateExt.now()
                )

                viewModelScope.launch {
                    // update reminder item in database
                    dao.updateReminderItem(updatedReminderItem)
                    Log.i(TAG, "Updated reminder $title with id ${event.itemToUpdate.id}")

                    // remove deleted notifications
                    notificationsToRemove.forEach { reminderNotification ->
                        // cancel already scheduled notification
                        notificationScheduler.cancel(reminderNotification.notificationId)
                        // remove reminder from database
                        dao.removeNotification(reminderNotification)

                        Log.i(TAG, "Removed and canceled notification scheduled for ${reminderNotification.notificationDate}")
                    }

                    // (add new) / (update existing) notifications to database and schedule them
                    notifications.forEach { reminderNotification ->

                        val notificationId: Int? = if (reminderNotification.notificationId <= 0) {
                            null
                        } else reminderNotification.notificationId

                        val notification: NotificationItem? = scheduleNotification(
                            alarmScheduler = notificationScheduler,
                            type = NotificationChannel.REMINDER,
                            connectedItem = updatedReminderItem,
                            notificationDate = reminderNotification.notificationDate,
                            notificationId = notificationId
                        )

                        if (notificationId == null && notification != null) {
                            dao.insertNotification(notification)
                        }
                    }
                }

                resetState()
            }

            is ReminderEvent.RemoveItem -> {
                viewModelScope.launch {
                    dao.removeNotificationsForConnectedItem(
                        channel = NotificationChannel.REMINDER,
                        connectedItemId = event.item.id
                    )
                    dao.removeReminderItem(reminderItem = event.item)
                }

                Log.i(TAG, "Removed all data which is related to reminder item ID ${event.item.id}")
                resetState()
            }


            is ReminderEvent.SetTitle -> {
                _reminderState.update { it.copy(
                    title = event.title
                ) }
            }

            is ReminderEvent.SetDueDate -> {
                _reminderState.update { it.copy(
                    dueDate = event.dueDate
                ) }
                Log.i(TAG, "Due date changed to ${event.dueDate}")
            }

            is ReminderEvent.SetCategory -> {
                _reminderState.update { it.copy(
                    category = event.category
                ) }
                Log.i(TAG, "Category changed to ${event.category}")
            }

            is ReminderEvent.AddOrRemoveFilterCategory -> {
                _reminderFilter.value = if (_reminderFilter.value.contains(event.filter)) {
                    _reminderFilter.value.minus(event.filter)
                } else _reminderFilter.value.plus(event.filter)
            }

            is ReminderEvent.AddDummyNotification -> {
                val notificationDate: DateExt = _reminderState.value.dueDate.subtractTimeUnit(
                    value = event.value,
                    timeUnit = event.timeUnit
                )

                val dummyReminderNotification = NotificationItem(
                    connectedItem = -1, // will be set on save
                    channel = NotificationChannel.REMINDER,
                    notificationId = -1, // will be set on save
                    notificationDate = notificationDate,
                    valueBeforeDue = event.value.toLong(),
                    timeUnit = event.timeUnit
                )

                _reminderState.update { it.copy(
                    notifications = it.notifications.plus(dummyReminderNotification),
                    newNotifications = it.newNotifications.plus(dummyReminderNotification)
                ) }
            }

            is ReminderEvent.RemoveNotification -> {
                _reminderState.update { it.copy(
                    notifications = it.notifications.minus(event.notification),
                    newNotifications = it.newNotifications.minus(event.notification),
                    notificationsToDelete = it.notificationsToDelete.plus(event.notification)
                ) }
            }

            ReminderEvent.MoveFromOrToDetailsScreen -> {
                _reminderFilter.value = emptyList()
            }

        }
    }

    private fun resetState() {
        _reminderState.update { it.copy(
            title = "",
            dueDate = DateExt.now(),
            category = de.malteharms.database.tables.ReminderCategory.GENERAL,
            notifications = listOf(),
            newNotifications = listOf(),
            notificationsToDelete = listOf(),
            isAddingItem = false,
            isEditingItem = false
        ) }
        Log.i(TAG, "Reset internal cache for current reminder item")
    }

    fun hasNotifications(id: Long): Boolean {
        var hasNotifications = false

        viewModelScope.launch {
            hasNotifications = dao.getNotificationsForConnectedItem(
                channel = NotificationChannel.REMINDER,
                itemId = id
            ).isNotEmpty()
        }

        return hasNotifications
    }

}

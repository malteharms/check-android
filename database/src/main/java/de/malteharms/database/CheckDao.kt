package de.malteharms.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.malteharms.database.tables.Birthday
import de.malteharms.database.tables.NotificationChannel
import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.ReminderCategory
import de.malteharms.database.tables.ReminderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckDao {

    /* REMINDER ITEM QUERY'S */
    @Insert
    suspend fun insertReminderItem(reminderItem: ReminderItem): Long

    @Update
    suspend fun updateReminderItem(reminderItem: ReminderItem)

    @Delete
    suspend fun removeReminderItem(reminderItem: ReminderItem)

    @Query("SELECT * FROM reminder_items ORDER BY dueDate ASC")
    fun getAllReminderItems(): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items ORDER BY dueDate ASC LIMIT :limit")
    fun getAllReminderItemsLimited(limit: Int): Flow<List<ReminderItem>>

    @Query("SELECT * FROM reminder_items WHERE category IN (:categories) ORDER BY dueDate ASC")
    fun getFilteredReminderItems(categories: List<ReminderCategory>): Flow<List<de.malteharms.database.tables.ReminderItem>>

    @Query("SELECT * FROM reminder_items WHERE category IN (:categories) ORDER BY dueDate ASC LIMIT :limit")
    fun getFilteredReminderItemsLimited(categories: List<ReminderCategory>, limit: Int): Flow<List<de.malteharms.database.tables.ReminderItem>>

    @Query("SELECT * FROM reminder_items WHERE birthdayRelation = :birthdayId LIMIT 1")
    fun getReminderItemForBirthdayId(birthdayId: Long): ReminderItem?

    @Query("SELECT * FROM reminder_items WHERE id = :reminderId")
    fun getReminderItemById(reminderId: Long): ReminderItem?

    /* NOTIFICATION QUERY'S */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: NotificationItem)

    @Delete
    suspend fun removeNotification(notification: NotificationItem)

    @Query("SELECT * FROM notifications")
    fun getAllNotifications(): List<NotificationItem>

    @Query("DELETE FROM notifications WHERE connectedItem = :connectedItemId AND channel = :channel")
    fun removeNotificationsForConnectedItem(channel: NotificationChannel, connectedItemId: Long)

    @Query("SELECT * FROM notifications WHERE connectedItem = :itemId  AND channel = :channel ORDER BY notificationDate")
    fun getNotificationsForConnectedItem(channel: NotificationChannel, itemId: Long): List<NotificationItem>

    @Query("SELECT * FROM notifications WHERE notificationDate < :timestamp")
    fun getOverdueNotifications(timestamp: Long): List<NotificationItem>

    @Insert
    suspend fun insertBirthday(item: Birthday)

    @Query("SELECT * FROM birthdays WHERE id = :birthdayId")
    fun getBirthday(birthdayId: Long): Birthday?

}
